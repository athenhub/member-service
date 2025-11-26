package com.athenhub.memberservice.member.domain.dto.response;

import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.MemberRole;
import com.athenhub.memberservice.member.domain.MemberStatus;
import com.athenhub.memberservice.member.domain.OrganizationType;
import java.util.UUID;

/**
 * 회원 가입 완료 후 클라이언트에 반환할 회원 정보 응답 DTO.
 *
 * <p>민감 정보(비밀번호 등)는 포함하지 않으며, 식별자와 표시용 정보 및 상태만 노출한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
public record MemberRegisterResponse(
    UUID id,
    String username,
    String name,
    String slackId,
    MemberRole role,
    MemberStatus status,
    OrganizationType organizationType,
    UUID organizationId,
    String organizationName) {

  /**
   * {@link Member} 엔터티로부터 {@link MemberRegisterResponse}를 생성한다.
   *
   * @param member 응답으로 변환할 회원 엔터티
   * @return 변환된 회원 등록 응답 DTO
   */
  public static MemberRegisterResponse from(Member member) {
    UUID orgId = member.getOrganizationId() == null ? null : member.getOrganizationId().toUuid();

    return new MemberRegisterResponse(
        member.getId().toUuid(),
        member.getUsername(),
        member.getName(),
        member.getSlackId(),
        member.getRole(),
        member.getStatus(),
        member.getOrganizationType(),
        orgId,
        member.getOrganizationName());
  }
}
