package com.athenhub.memberservice.member.domain;

import com.athenhub.memberservice.global.domain.AbstractAuditEntity;
import com.athenhub.memberservice.member.domain.dto.request.MemberMasterUpdateRequest;
import com.athenhub.memberservice.member.domain.dto.request.MemberRegisterRequest;
import com.athenhub.memberservice.member.domain.dto.request.MemberUpdateRequest;
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

@Table(name = "p_member")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractAuditEntity {

  @EmbeddedId
  private MemberId id;

  @Column(nullable = false, unique = true)
  private String keycloakUserId;

  @Embedded
  private OrganizationId organizationId;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
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

  // 회원 등록
  public static Member signUp(
      MemberRegisterRequest registerRequest,
      String keycloakUserId,
      MemberExistenceChecker memberExistenceChecker) {

    // 중복 검증
    checkDuplicateMember(registerRequest, memberExistenceChecker);

    OrganizationId organizationId = OrganizationId.of(registerRequest.organizationId().toUuid());

    Member member = new Member();


    member.keycloakUserId = Objects.requireNonNull(keycloakUserId);
    member.organizationId = organizationId;
    member.name = registerRequest.name();
    member.username = registerRequest.username();
    member.slackId = registerRequest.slackId();
    member.role = registerRequest.role();
    member.organizationType = registerRequest.organizationType();
    member.organizationName = registerRequest.organizationName();
    member.status = MemberStatus.PENDING;

    return member;
  }

  // 일반 회원의 회원 정보 수정
  public void updateInfo(MemberUpdateRequest updateRequest) {

    requireStatus(MemberStatus.ACTIVATED, MemberErrorCode.INVALID_STATUS_FOR_UPDATE);

    validateNotDeleted();

    this.slackId = updateRequest.slackId();
  }

  // MASTER_MANAGER 권한 정보 수정
  public void updateByMaster(MemberMasterUpdateRequest updateRequest, UUID requestId, PermissionChecker permissionChecker) {

    validateMasterOperation(updateRequest, requestId, permissionChecker);

    this.name = updateRequest.name();
    this.role = updateRequest.role();
    this.organizationType = updateRequest.organizationType();
  }

  // 가입 승인
  public void approve(UUID requestId, PermissionChecker permissionChecker) {

    checkMemberManagePermission(this.id.toUuid(), permissionChecker, requestId);

    validateNotDeleted();

    requireStatus(MemberStatus.PENDING, MemberErrorCode.INVALID_APPROVE_STATUS);
    this.status = MemberStatus.ACTIVATED;
  }

  // 가입 거절
  public void reject(UUID requestId, PermissionChecker permissionChecker) {

    checkMemberManagePermission(this.id.toUuid(), permissionChecker, requestId);

    validateNotDeleted();

    requireStatus(MemberStatus.PENDING, MemberErrorCode.INVALID_APPROVE_STATUS);
    this.status = MemberStatus.REJECTED;
  }

  // 소프트 삭제 (회원 삭제/탈퇴)
  public void deleteMember(String deletedBy, PermissionChecker permissionChecker,
      UUID requestId) {

    checkMemberManagePermission(this.id.toUuid(), permissionChecker, requestId);

    validateNotDeleted();

    this.status = MemberStatus.DEACTIVATED;
    super.delete(deletedBy);
  }

  // ======== 헬퍼 메서드 ==========//

  // 마스터 매니저 공통 메서드
  private void validateMasterOperation(MemberMasterUpdateRequest updateRequest, UUID requestId,
      PermissionChecker permissionChecker) {
    checkMemberManagePermission(this.id.toUuid(), permissionChecker, requestId);
    validateNotDeleted();
  }

  // 중복 체크 검증 메서드(username, slackId 중복 체크)
  private static void checkDuplicateMember(MemberRegisterRequest registerRequest,
      MemberExistenceChecker memberExistenceChecker) {
    // username 중복 체크
    if (memberExistenceChecker.existsByUsername(registerRequest.username())) {
      throw new MemberException(
          MemberErrorCode.USED_MEMBER_INFO
      );
    }
    // slackId 중복 체크
    if (memberExistenceChecker.existsBySlackId(registerRequest.slackId())) {
      throw new MemberException(
          MemberErrorCode.USED_MEMBER_INFO
      );
    }
  }

  // 회원에 대한 관리 권한(마스터 권한) 확인
  private static void checkMemberManagePermission(
      UUID memberId, PermissionChecker permissionChecker, UUID requesterId) {
    if (!permissionChecker.hasMasterPermission(requesterId, MemberId.of(memberId))) {
      throw new PermissionException(PermissionErrorCode.HAS_NOT_MANAGE_PERMISSION);
    }
  }


  // 들어오는 Status값이 같이 않을 경우
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

  /** soft delete 여부: deletedAt 이 설정되었는지로 판단 */
  public boolean isDeleted() {
    return getDeletedAt() != null;
  }

}
