package com.athenhub.memberservice.member.domain.service;

import com.athenhub.memberservice.member.domain.Member;
import java.util.UUID;

/**
 * 회원 삭제(탈퇴) 시 적용되는 도메인 정책을 정의하는 인터페이스.
 *
 * <p>회원 삭제 요청이 들어왔을 때,
 *
 * <ul>
 *   <li>해당 회원이 삭제 가능한 상태인지,
 *   <li>삭제를 요청한 주체(본인, 관리자 등)가 삭제 권한을 가지고 있는지
 * </ul>
 *
 * 등의 도메인 규칙을 검증하는 역할을 한다.
 *
 * <p>구체적인 검증 로직은 애플리케이션 요구사항에 따라 구현체에서 정의하며, 도메인 계층에서는 이 인터페이스에만 의존하도록 한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
public interface MemberDeletionPolicy {

  /**
   * 회원 삭제 요청이 유효한지 검증한다.
   *
   * @param member 삭제 대상 회원
   * @param requesterId 삭제를 요청한 주체(본인, 관리자 등)의 ID
   * @throws com.athenhub.memberservice.member.domain.exception.MemberException 삭제가 허용되지 않는 상태이거나,
   *     삭제 권한이 없는 경우
   * @throws com.athenhub.memberservice.member.domain.exception.PermissionException 삭제 권한이 부족한 경우
   *     구현체에서 선택적으로 사용할 수 있다
   */
  void validate(Member member, UUID requesterId);
}
