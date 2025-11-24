package com.athenhub.memberservice.member.domain.dto.request;

import com.athenhub.memberservice.member.domain.MemberRole;
import com.athenhub.memberservice.member.domain.MemberStatus;
import com.athenhub.memberservice.member.domain.OrganizationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MemberMasterUpdateRequest(
    @NotBlank String name,
    @NotNull MemberRole role,
    @NotNull OrganizationType organizationType,
    @NotNull UUID hubId,
    @NotBlank String organizationName,
    @NotNull MemberStatus status) {}
