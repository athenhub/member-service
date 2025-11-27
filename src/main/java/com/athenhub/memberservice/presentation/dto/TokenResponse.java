package com.athenhub.memberservice.presentation.dto;

public record TokenResponse(
    String accessToken,
    int expiresIn,
    int refreshExpiresIn,
    String refreshToken,
    String tokenType) {}
