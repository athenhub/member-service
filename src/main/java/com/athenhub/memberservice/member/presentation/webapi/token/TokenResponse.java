package com.athenhub.memberservice.member.presentation.webapi.token;

public record TokenResponse(
    String accessToken,
    int expiresIn,
    int refreshExpiresIn,
    String refreshToken,
    String tokenType) {}
