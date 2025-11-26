package com.athenhub.memberservice.member.presentation.webapi.dto;

import com.athenhub.memberservice.member.domain.Member;

public record MemberUpdateInfoRegisterResponse(String slackId) {
  public static MemberUpdateInfoRegisterResponse from(Member member) {
    return new MemberUpdateInfoRegisterResponse(member.getSlackId());
  }
}
