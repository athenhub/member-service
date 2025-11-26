package com.athenhub.memberservice.member.presentation.webapi.dto;

import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.MemberRole;
import com.athenhub.memberservice.member.domain.MemberStatus;
import com.athenhub.memberservice.member.domain.OrganizationType;

/**
 * 마스터 권한에 의해 수정된 회원 정보를 반환하는 응답 DTO.
 *
 * <p>마스터가 회원의 역할, 소속 정보 등을 변경한 이후의 최종 상태를 클라이언트에 전달한다.
 *
 * @param memberId 회원 식별자(UUID)
 * @param name 회원 이름
 * @param username 계정 ID
 * @param slackId 슬랙 ID
 * @param role 회원 역할
 * @param organizationType 소속 타입 (예: HUB, VENDOR 등)
 * @param organizationName 소속 이름
 * @param status 회원 상태
 * @author 박성준
 * @since 1.0.0
 */
public record MemberMasterUpdateResponse(
    String name,
    String username,
    String slackId,
    MemberRole role,
    OrganizationType organizationType,
    String organizationName,
    MemberStatus status) {

  /**
   * {@link Member} 엔터티로부터 {@link MemberMasterUpdateResponse}를 생성한다.
   *
   * @param member 응답으로 변환할 회원 엔터티
   * @return 변환된 응답 DTO
   */
  public static MemberMasterUpdateResponse from(Member member) {
    return new MemberMasterUpdateResponse(
        member.getName(),
        member.getUsername(),
        member.getSlackId(),
        member.getRole(),
        member.getOrganizationType(),
        member.getOrganizationName(),
        member.getStatus());
  }
}
