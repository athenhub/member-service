package com.athenhub.memberservice.member.application.service;

import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.dto.request.MemberUpdateInfoRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public interface MemberManager {

  Member updateInfo(
      @NotNull UUID memberId,
      @Valid MemberUpdateInfoRequest updateRequest,
      @NotNull String slackId);
}
