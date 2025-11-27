package com.athenhub.memberservice.member.presentation.webapi.token;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(@NotBlank String username, @NotBlank String password) {}
