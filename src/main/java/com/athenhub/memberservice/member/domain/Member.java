package com.athenhub.memberservice.member.domain;

import com.athenhub.memberservice.global.domain.AbstractAuditEntity;
import com.athenhub.memberservice.member.domain.dto.request.MemberMasterUpdateRequest;
import com.athenhub.memberservice.member.domain.dto.request.MemberRegisterRequest;
import com.athenhub.memberservice.member.domain.dto.request.MemberUpdateRequest;
import com.athenhub.memberservice.member.domain.exception.MemberErrorCode;
import com.athenhub.memberservice.member.domain.exception.MemberException;
import com.athenhub.memberservice.member.domain.vo.HubId;
import com.athenhub.memberservice.member.domain.vo.MemberId;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.Objects;
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

  @EmbeddedId private MemberId id;

  @Column(nullable = false)
  private String keycloakUserId;

  @Embedded private HubId hubId;

  @Column(nullable = false)
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
  public static Member signUp(MemberRegisterRequest registerRequest, String keycloakUserId) {
    Member member = new Member();
    HubId hubId = HubId.of(registerRequest.hubId());

    member.id = MemberId.generateId();
    member.hubId = hubId;
    member.keycloakUserId = Objects.requireNonNull(keycloakUserId);
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
    this.slackId = updateRequest.slackId();
  }

  // MASTER_MANAGER 권한 정보 수정
  public void updateByMaster(MemberMasterUpdateRequest updateRequest) {
    this.hubId = HubId.of(updateRequest.hubId());
    this.name = updateRequest.name();
    this.role = updateRequest.role();
    this.organizationType = updateRequest.organizationType();
  }

  // 가입 승인
  public void approve() {
    memberStatusRejected();
    memberStatusNotPending();
    this.status = MemberStatus.ACTIVATED;
  }

  // 가입 거절
  public void reject() {
    memberStatusRejected();
    memberStatusNotPending();
    this.status = MemberStatus.REJECTED;
  }

  // ======== 헬퍼 메서드 ==========//

  //
  private void memberStatusNotPending() {
    if (this.status != MemberStatus.PENDING) {
      throw new MemberException(MemberErrorCode.INVALID_APPROVE_STATUS, "대기 상태인 회원만 승인할 수 있습니다.");
    }
  }

  private void memberStatusRejected() {
    if (this.status == MemberStatus.REJECTED) {
      throw new MemberException(MemberErrorCode.INVALID_APPROVE_STATUS, "거절된 회원입니다.");
    }
  }
}
