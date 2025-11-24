package com.athenhub.memberservice.member.domain.exception;

import com.athenhub.commoncore.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum PermissionErrorCode implements ErrorCode {
  // 등록 권한
  HAS_NOT_REGISTER_PERMISSION(HttpStatus.FORBIDDEN.value(), "HAS_NOT_REGISTER_PERMISSION"),
  // 수정, 삭제, 상태 변경 권한
  HAS_NOT_MANAGE_PERMISSION(HttpStatus.FORBIDDEN.value(), "HAS_NOT_MANAGE_PERMISSION");

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
