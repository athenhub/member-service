package com.athenhub.memberservice.member.domain;

import com.athenhub.memberservice.global.domain.AbstractAuditEntity;
import com.athenhub.memberservice.member.domain.dto.request.MemberMasterUpdateRequest;
import com.athenhub.memberservice.member.domain.dto.request.MemberRegisterRequest;
import com.athenhub.memberservice.member.domain.dto.request.MemberUpdateInfoRequest;
import com.athenhub.memberservice.member.domain.exception.MemberErrorCode;
import com.athenhub.memberservice.member.domain.exception.MemberException;
import com.athenhub.memberservice.member.domain.exception.PermissionErrorCode;
import com.athenhub.memberservice.member.domain.exception.PermissionException;
import com.athenhub.memberservice.member.domain.service.MemberExistenceChecker;
import com.athenhub.memberservice.member.domain.service.PermissionChecker;
import com.athenhub.memberservice.member.domain.vo.MemberId;
import com.athenhub.memberservice.member.domain.vo.OrganizationId;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 회원(Member) 애그리거트 루트 엔터티.
 *
 * <p>회원 등록, 수정, 승인/거절, 탈퇴(소프트 삭제)와 같은 주요 도메인 규칙을 캡슐화한다. <br>
 * 또한 {@link AbstractAuditEntity}를 상속하여 생성/수정/삭제 감사 정보를 함께 관리한다.
 *
 * <h2>주요 역할</h2>
 *
 * <ul>
 *   <li>회원 가입 시 필수 정보 설정 및 초기 상태(PENDING) 부여
 *   <li>일반 회원의 자기 정보(slackId) 수정
 *   <li>마스터 권한(MASTER_MANAGER)의 회원 정보 수정 및 상태 변경(승인/거절/비활성화)
 *   <li>소프트 삭제(탈퇴) 처리 및 삭제 여부 검사
 *   <li>상태 전이 검증 및 권한 검증(도메인 예외 발생)
 * </ul>
 *
 * @author 박성준
 * @since 1.0.0
 */
@Table(name = "p_member")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractAuditEntity {

  @EmbeddedId private MemberId id;

  @Embedded private OrganizationId organizationId;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String slackId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MemberRole role;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MemberStatus status;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrganizationType organizationType;

  @Column(name = "organization_name")
  private String organizationName;

  /**
   * 회원 가입(등록)을 수행하는 정적 팩터리 메서드.
   *
   * <p>주어진 등록 요청과 외부에서 전달된 {@code memberId}를 기반으로 신규 {@link Member}를 생성한다. 생성 시 상태는 {@link
   * MemberStatus#PENDING} 으로 설정된다.
   *
   * @param registerRequest 회원 등록 요청 정보
   * @param memberId 외부에서 발급된 회원 식별자(UUID)
   * @param memberExistenceChecker 회원 존재/중복 여부 확인 도메인 서비스
   * @return 생성된 회원 엔터티
   * @throws MemberException 도메인 규칙 위반 시 (예: 중복 정보 등)
   */
  public static Member signUp(
      MemberRegisterRequest registerRequest,
      UUID memberId,
      MemberExistenceChecker memberExistenceChecker) {

    // 중복 검증
    checkDuplicateMemberId(memberId, memberExistenceChecker);
    checkDuplicateSlackId(registerRequest.slackId(), memberExistenceChecker);
    checkDuplicateUsername(registerRequest.username(), memberExistenceChecker);

    Member member = new Member();

    member.id = MemberId.of(memberId);
    member.name = registerRequest.name();
    member.username = registerRequest.username();
    member.slackId = registerRequest.slackId();
    member.role = registerRequest.role();
    member.organizationType = registerRequest.organizationType();
    member.organizationName = registerRequest.organizationName();
    member.status = MemberStatus.PENDING;

    return member;
  }

  /**
   * 일반 회원이 자신의 정보를 수정할 때 사용하는 메서드.
   *
   * <p>현재는 Slack ID 만 수정 가능하며, 삭제되지 않았고 활성화된 ({@link MemberStatus#ACTIVATED}) 회원에 대해서만 수정이 허용된다.
   *
   * @param updateRequest 수정할 Slack ID 정보
   * @throws MemberException 상태가 유효하지 않거나 이미 삭제된 회원인 경우
   */
  public void updateInfo(
      MemberUpdateInfoRequest updateRequest, MemberExistenceChecker memberExistenceChecker) {

    validateNotDeleted();
    checkDuplicateSlackId(updateRequest.slackId(), memberExistenceChecker);
    requireStatus(MemberStatus.ACTIVATED, MemberErrorCode.INVALID_STATUS_FOR_UPDATE);

    updateSlackId(updateRequest.slackId(), memberExistenceChecker);
  }

  private void updateSlackId(String newSlackId, MemberExistenceChecker memberExistenceChecker) {

    // 값이 그대로면 중복 체크/업데이트 스킵
    if (Objects.equals(this.slackId, newSlackId)) {
      return;
    }

    checkDuplicateSlackId(newSlackId, memberExistenceChecker);

    this.slackId = newSlackId;
  }

  /**
   * 마스터 관리자(MASTER_MANAGER)가 다른 회원의 정보를 수정할 때 사용하는 메서드.
   *
   * <p>마스터 권한을 가진 요청자인지 검증한 뒤, 이름/역할/소속 유형을 수정한다.
   *
   * @param updateRequest 수정할 회원 정보(이름, 역할, 소속 유형 등)
   * @param permissionChecker 권한 검증 도메인 서비스
   * @throws PermissionException 마스터 관리 권한이 없는 경우
   * @throws MemberException 이미 삭제된 회원 등, 도메인 규칙 위반인 경우
   */
  public void updateByMaster(
      MemberMasterUpdateRequest updateRequest,
      PermissionChecker permissionChecker,
      MemberExistenceChecker memberExistenceChecker) {

    masterManagerMethod(this.id.toUuid(), permissionChecker);
    checkDuplicateUsername(updateRequest.username(), memberExistenceChecker);

    this.name = updateRequest.name();
    this.username = updateRequest.username();
    this.role = updateRequest.role();
    this.organizationType = updateRequest.organizationType();
    this.organizationName = updateRequest.organizationName();
    this.status = updateRequest.status();
  }

  /**
   * 회원 가입 승인(approve)을 수행하는 메서드.
   *
   * <p>마스터 권한을 검증한 뒤, 현재 상태가 {@link MemberStatus#PENDING} 인 경우에만 {@link MemberStatus#ACTIVATED} 로
   * 전이된다.
   *
   * @param requestId 승인 요청을 한 주체(관리자)의 ID
   * @param permissionChecker 권한 검증 도메인 서비스
   * @throws PermissionException 마스터 관리 권한이 없는 경우
   * @throws MemberException 상태가 PENDING 이 아니거나 이미 삭제된 회원인 경우
   */
  public void approve(UUID requestId, PermissionChecker permissionChecker) {

    masterManagerMethod(requestId, permissionChecker);

    requireStatus(MemberStatus.PENDING, MemberErrorCode.INVALID_APPROVE_STATUS);
    this.status = MemberStatus.ACTIVATED;
  }

  /**
   * 회원 가입 거절(reject)을 수행하는 메서드.
   *
   * <p>마스터 권한을 검증한 뒤, 현재 상태가 {@link MemberStatus#PENDING} 인 경우에만 {@link MemberStatus#REJECTED} 로
   * 전이된다.
   *
   * @param requestId 거절 요청을 한 주체(관리자)의 ID
   * @param permissionChecker 권한 검증 도메인 서비스
   * @throws PermissionException 마스터 관리 권한이 없는 경우
   * @throws MemberException 상태가 PENDING 이 아니거나 이미 삭제된 회원인 경우
   */
  public void reject(UUID requestId, PermissionChecker permissionChecker) {
    masterManagerMethod(requestId, permissionChecker);
    requireStatus(MemberStatus.PENDING, MemberErrorCode.INVALID_REJECT_STATUS);
    this.status = MemberStatus.REJECTED;
  }

  /**
   * 회원 소프트 삭제(탈퇴)를 수행하는 메서드.
   *
   * <p>마스터 권한을 검증한 뒤, 상태를 {@link MemberStatus#DEACTIVATED} 로 변경하고 상위 {@link
   * AbstractAuditEntity#delete(String)} 를 호출하여 삭제 이력을 기록한다.
   *
   * @param deletedBy 삭제 수행자 식별자(이력 기록용 문자열)
   * @param permissionChecker 권한 검증 도메인 서비스
   * @param requestId 삭제 요청을 한 주체(관리자)의 ID
   * @throws PermissionException 마스터 관리 권한이 없는 경우
   */
  public void deleteMember(String deletedBy, PermissionChecker permissionChecker, UUID requestId) {

    masterManagerMethod(requestId, permissionChecker);

    this.status = MemberStatus.DEACTIVATED;
    super.delete(deletedBy);
  }

  // ======== 헬퍼 메서드 ==========//

  // 마스터 매니저 헬퍼 메서드 묶음(Permission + validateNotDelete)
  private void masterManagerMethod(UUID requestId, PermissionChecker permissionChecker) {
    validateNotDeleted();
    checkMemberManagePermission(this.id.toUuid(), permissionChecker, requestId);
  }

  // 회원 UUID 중복 체크
  private static void checkDuplicateMemberId(
      UUID memberId, MemberExistenceChecker memberExistenceChecker) {
    if (memberExistenceChecker.hasMember(memberId)) {
      // 이미 해당 memberId 로 회원이 존재하면 '중복 정보' 에러
      throw new MemberException(MemberErrorCode.USED_MEMBER_INFO, "해당 회원의 ID가 이미 존재합니다.");
    }
  }

  // slackID 중복 체크
  private static void checkDuplicateSlackId(
      String slackId, MemberExistenceChecker memberExistenceChecker) {
    if (memberExistenceChecker.existsBySlackId(slackId)) {
      throw new MemberException(MemberErrorCode.USED_MEMBER_INFO, "Slack ID 중복입니다.");
    }
  }

  // 회원 username 중복 체크
  private static void checkDuplicateUsername(
      String username, MemberExistenceChecker memberExistenceChecker) {
    if (memberExistenceChecker.existsByUsername(username)) {
      throw new MemberException(MemberErrorCode.USED_MEMBER_INFO, "username 중복입니다.");
    }
  }

  // 회원에 대한 관리 권한(마스터 권한) 확인
  private static void checkMemberManagePermission(
      UUID memberId, PermissionChecker permissionChecker, UUID requesterId) {
    if (!permissionChecker.hasMasterPermission(requesterId, MemberId.of(memberId))) {
      throw new PermissionException(PermissionErrorCode.HAS_NOT_MANAGE_PERMISSION);
    }
  }

  // 들어오는 Status값이 같지 않을 경우
  private void requireStatus(MemberStatus expectedStatus, MemberErrorCode errorCode) {
    if (this.status != expectedStatus) {
      throw new MemberException(errorCode);
    }
  }

  // 삭제 되었는지 체크
  private void validateNotDeleted() {
    if (isDeleted()) {
      throw new MemberException(MemberErrorCode.DELETED_MEMBER);
    }
  }

  /**
   * soft delete 여부를 반환한다.
   *
   * <p>{@link AbstractAuditEntity#getDeletedAt()} 값이 설정되어 있으면 삭제된 것으로 간주한다.
   *
   * @return 삭제된 상태이면 {@code true}, 아니면 {@code false}
   */
  public boolean isDeleted() {
    return getDeletedAt() != null;
  }
}
