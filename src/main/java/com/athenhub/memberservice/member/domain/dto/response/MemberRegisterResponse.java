package com.athenhub.memberservice.member.domain.dto.response;

import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.MemberRole;
import com.athenhub.memberservice.member.domain.MemberStatus;
import com.athenhub.memberservice.member.domain.OrganizationType;
import java.util.UUID;

/**
 * 회원 가입 완료 후 클라이언트에 반환하는 응답 DTO.
 *
 * <p>신규로 등록된 회원의 주요 정보를 포함하며, 비밀번호와 같이 민감한 정보는 포함하지 않는다.
 *
 * @author 박성준
 * @since 1.0.0
 */
public record MemberRegisterResponse(
    UUID id,
    String name,
    String username,
    String slackId,
    MemberRole role,
    MemberStatus status,
    UUID organizationId,
    OrganizationType organizationType,
    String organizationName) {

  /**
   * 도메인 엔터티 {@link Member}를 기반으로 {@link MemberRegisterResponse} 인스턴스를 생성한다.
   *
   * @param member 등록이 완료된 회원 엔터티
   * @return 회원 정보를 담은 응답 DTO
   */
  public static MemberRegisterResponse from(Member member) {
    return new MemberRegisterResponse(
        member.getId().toUuid(),
        member.getName(),
        member.getUsername(),
        member.getSlackId(),
        member.getRole(),
        member.getStatus(),
        member.getOrganizationId() != null ? member.getOrganizationId().toUuid() : null,
        member.getOrganizationType(),
        member.getOrganizationName());
  }
}
