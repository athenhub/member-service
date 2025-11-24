package com.athenhub.memberservice.member.domain.exception;

import com.athenhub.commoncore.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 회원 도메인 전반에서 사용하는 에러 코드 정의.
 *
 * <p>유형별로 다음과 같이 분류된다.</p>
 * <ul>
 *   <li>요청 값/정합성 오류: {@link #INVALID_MEMBER_INFO}, {@link #USED_MEMBER_INFO}</li>
 *   <li>상태 전이/상태 제약: {@link #INVALID_APPROVE_STATUS}, {@link #INVALID_STATUS_FOR_UPDATE},
 *       {@link #INVALID_STATUS_TRANSITION}, {@link #DELETED_MEMBER}</li>
 *   <li>리소스 조회 실패: {@link #MEMBER_NOT_FOUND}, {@link #HUB_NOT_FOUND}</li>
 *   <li>권한 오류: {@link #NO_PERMISSION}</li>
 * </ul>
 */
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

  // ===== 요청 값 / 정합성 관련 =====
  INVALID_MEMBER_INFO(HttpStatus.BAD_REQUEST.value(), "INVALID_MEMBER_INFO"),
  USED_MEMBER_INFO(HttpStatus.BAD_REQUEST.value(), "USED_MEMBER_INFO"),

  // ===== 상태 전이 / 상태 제약 관련 =====
  /** 승인(approve)을 수행하기에 유효하지 않은 상태인 경우 */
  INVALID_APPROVE_STATUS(HttpStatus.BAD_REQUEST.value(), "INVALID_APPROVE_STATUS"),

  /** 회원 정보 수정(update)을 수행하기에 유효하지 않은 상태인 경우 */
  INVALID_STATUS_FOR_UPDATE(HttpStatus.BAD_REQUEST.value(), "INVALID_STATUS_FOR_UPDATE"),

  /** 허용되지 않은 상태 전이가 발생한 경우 (예: PENDING -> DEACTIVATED 등) */
  INVALID_STATUS_TRANSITION(HttpStatus.BAD_REQUEST.value(), "INVALID_STATUS_TRANSITION"),

  /** 삭제(soft delete)된 회원에 대해 동작을 시도한 경우 */
  DELETED_MEMBER(HttpStatus.BAD_REQUEST.value(), "DELETED_MEMBER"),

  // ===== 리소스 조회 실패 =====
  MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "MEMBER_NOT_FOUND"),
  HUB_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "HUB_NOT_FOUND"),

  // ===== 권한 관련 =====
  NO_PERMISSION(HttpStatus.FORBIDDEN.value(), "NO_PERMISSION"),
  ;

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
