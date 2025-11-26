package com.athenhub.memberservice.member.domain.exception;

import com.athenhub.commoncore.error.AbstractServiceException;
import com.athenhub.commoncore.error.ErrorCode;

/**
 * 회원 도메인에서 권한(인가) 관련 비즈니스 예외를 표현하는 클래스.
 *
 * <p>권한 검증 과정에서 {@link ErrorCode}를 기반으로 HTTP 상태 코드와 에러 코드를 함께 전달하며, 추가적인 예외 메시지 포맷팅이 필요한 경우 {@code
 * errorArgs}를 인자로 받아 처리할 수 있다.
 *
 * <p>허브/업체/회원 관리 권한이 없는 사용자가 등록, 수정, 삭제, 상태 변경 등의 작업을 시도하는 경우 이 예외를 발생시켜 애플리케이션 전역 예외 처리기에서 일관된 응답을
 * 생성하도록 한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
public class PermissionException extends AbstractServiceException {

  /**
   * 주어진 에러 코드와 에러 인자를 기반으로 권한 관련 예외를 생성한다.
   *
   * @param errorCode 권한 관련 에러 코드
   * @param errorArgs 에러 메시지 포맷팅 등에 사용할 추가 인자들
   */
  public PermissionException(ErrorCode errorCode, Object... errorArgs) {
    super(errorCode, errorArgs);
  }
}
