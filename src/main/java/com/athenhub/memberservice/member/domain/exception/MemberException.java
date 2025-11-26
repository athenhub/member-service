package com.athenhub.memberservice.member.domain.exception;

import com.athenhub.commoncore.error.AbstractServiceException;
import com.athenhub.commoncore.error.ErrorCode;

/**
 * 회원 도메인에서 발생하는 비즈니스 예외를 표현하는 클래스.
 *
 * <p>{@link ErrorCode}를 통해 HTTP 상태 코드 및 에러 코드를 함께 전달하며, 추가적인 예외 메시지 포맷팅이 필요한 경우 {@code errorArgs}를
 * 인자로 받아 처리할 수 있다.
 *
 * <p>회원 등록, 수정, 상태 변경, 권한 검증 등의 과정에서 도메인 규칙을 위반하는 경우 이 예외를 발생시켜 애플리케이션 전역 예외 처리기에서 일관된 응답을 생성하도록
 * 한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
public class MemberException extends AbstractServiceException {

  /**
   * 주어진 에러 코드와 에러 인자를 기반으로 회원 도메인 예외를 생성한다.
   *
   * @param errorCode 회원 도메인 에러 코드
   * @param errorArgs 에러 메시지 포맷팅 등에 사용할 추가 인자들
   */
  public MemberException(ErrorCode errorCode, Object... errorArgs) {
    super(errorCode, errorArgs);
  }
}
