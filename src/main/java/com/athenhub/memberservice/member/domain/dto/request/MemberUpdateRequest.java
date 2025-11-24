package com.athenhub.memberservice.member.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberUpdateRequest(@NotBlank String slackId) {}
