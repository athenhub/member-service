package com.athenhub.memberservice.member.domain.service;

import com.athenhub.memberservice.member.domain.Member;
import java.util.UUID;

/**
 * 회원 삭제(탈퇴) 시 적용되는 도메인 정책.
 *
 * <p>삭제 가능한지, 요청자가 삭제 권한이 있는지 등을 검증한다.
 */
public interface MemberDeletionPolicy {

  /**
   * 회원 삭제 요청이 유효한지 검증한다.
   *
   * @param member      삭제 대상 회원
   * @param requesterId 삭제를 요청한 주체(본인, 관리자 등)의 ID
   */
  void validate(Member member, UUID requesterId);
}