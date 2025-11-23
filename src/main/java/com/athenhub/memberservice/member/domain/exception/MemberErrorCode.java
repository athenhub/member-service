package com.athenhub.memberservice.member.domain.exception;

import com.athenhub.commoncore.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

  INVALID_APPROVE_STATUS(HttpStatus.BAD_REQUEST.value(), "INVALID_APPROVE_STATUS"),
  INVALID_STATUS_TRANSITION(HttpStatus.BAD_REQUEST.value(), "INVALID_STATUS_TRANSITION");

  private final int status;
  private final String code;


  @Override
  public int getStatus() {
    return status;
  }

  @Override
  public String getCode() {
    return code;
  }
}
