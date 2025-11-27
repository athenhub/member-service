package com.athenhub.memberservice.application.service;

import com.athenhub.memberservice.application.dto.TokenInfo;

public interface TokenGenerateService {
  TokenInfo generateToken(String username, String password);
}
