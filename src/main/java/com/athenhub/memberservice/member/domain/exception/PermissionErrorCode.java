package com.athenhub.memberservice.member.domain.exception;

import com.athenhub.commoncore.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 회원 도메인에서 사용하는 권한 관련 에러 코드를 정의한 열거형.
 *
 * <p>각 에러 코드는 HTTP 상태 코드와 애플리케이션 내부 에러 코드를 함께 제공하여,
 * 인가(Authorization) 과정에서 발생하는 권한 부족 상황을 일관되게 표현할 수 있도록 한다.
 *
 * <ul>
 *   <li>{@link #HAS_NOT_REGISTER_PERMISSION} — 등록 작업에 대한 권한이 없는 경우</li>
 *   <li>{@link #HAS_NOT_MANAGE_PERMISSION} — 수정, 삭제, 상태 변경 등 관리 작업에 대한 권한이 없는 경우</li>
 * </ul>
 *
 * @author 박성준
 * @since 1.0.0
 */
@RequiredArgsConstructor
public enum PermissionErrorCode implements ErrorCode {

  /** 등록 작업에 대한 권한이 없는 경우 */
  HAS_NOT_REGISTER_PERMISSION(HttpStatus.FORBIDDEN.value(), "HAS_NOT_REGISTER_PERMISSION"),

  /** 수정, 삭제, 상태 변경 등 관리 작업에 대한 권한이 없는 경우 */
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
