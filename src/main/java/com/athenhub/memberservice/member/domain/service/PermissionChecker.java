package com.athenhub.memberservice.member.domain.service;

import com.athenhub.memberservice.member.domain.vo.MemberId;
import java.util.UUID;

/** 회원 관련 권한을 검사하는 도메인 서비스 포트. */
public interface PermissionChecker {

  /**
   * 주어진 요청자가 특정 회원을 관리할 수 있는 마스터 권한을 보유하고 있는지 확인한다.
   *
   * <p>예: MASTER_MANAGER가 다른 회원의 정보 수정, 역할 변경 등을 수행할 수 있는지 검사할 때 사용한다.
   *
   * @param requesterId 권한을 확인할 요청자(회원/관리자) ID
   * @param memberId 관리 대상 회원 ID
   * @return 관리(마스터) 권한이 있으면 true, 없으면 false
   */
  boolean hasMasterPermission(UUID requesterId, MemberId memberId);
}
