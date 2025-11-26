package com.athenhub.memberservice.member.domain;

/**
 * 회원의 상태를 나타내는 열거형.
 *
 * <p>회원의 라이프사이클 전반에 걸쳐 현재 상태를 표현하며, 상태 전이 규칙은 {@link Member} 도메인 로직에서 관리한다.
 *
 * <ul>
 *   <li>{@link #PENDING} — 가입 신청이 완료되었으나 아직 승인되지 않은 상태
 *   <li>{@link #ACTIVATED} — 가입 승인 후 정상적으로 활성화된 상태
 *   <li>{@link #REJECTED} — 가입 신청이 거절된 상태
 *   <li>{@link #DEACTIVATED} — 한 번 이상 승인되었으나 이후 비활성(삭제) 처리된 상태
 * </ul>
 *
 * @author 박성준
 * @since 1.0.0
 */
public enum MemberStatus {

  /** 가입 신청이 완료되었으나 아직 승인되지 않은 상태 */
  PENDING,

  /** 가입 승인 후 정상적으로 활성화된 상태 */
  ACTIVATED,

  /** 가입 신청이 거절된 상태 */
  REJECTED,

  /** 승인되었으나 이후 비활성(삭제) 처리된 상태 */
  DEACTIVATED; // 승인 했으나 삭제된 경우
}
