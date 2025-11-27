package com.athenhub.memberservice.presentation.controller;

import com.athenhub.memberservice.application.dto.TokenInfo;
import com.athenhub.memberservice.application.service.TokenGenerateService;
import com.athenhub.memberservice.presentation.dto.TokenRequest;
import com.athenhub.memberservice.presentation.dto.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

  private final TokenGenerateService tokenService;

  // 토큰 발급
  @PostMapping("token")
  public TokenResponse generateToken(@Valid @RequestBody TokenRequest req) {
    TokenInfo tokenInfo = tokenService.generateToken(req.username(), req.password());

    return new TokenResponse(
        tokenInfo.access_token(),
        tokenInfo.expires_in(),
        tokenInfo.refresh_expires_in(),
        tokenInfo.refresh_token(),
        tokenInfo.token_type());
  }
}
