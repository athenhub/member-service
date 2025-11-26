package com.athenhub.memberservice.member.infrastructure;

import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.MemberRepository;
import com.athenhub.memberservice.member.domain.MemberRole;
import com.athenhub.memberservice.member.domain.exception.MemberErrorCode;
import com.athenhub.memberservice.member.domain.exception.MemberException;
import com.athenhub.memberservice.member.domain.service.PermissionChecker;
import com.athenhub.memberservice.member.domain.vo.MemberId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * {@link PermissionChecker} 의 구현체로, 회원의 권한(특히 마스터 권한)을 확인하는 역할을 담당한다.
 *
 * <p>요청자의 {@link Member} 정보를 조회한 뒤, 해당 회원의 역할이 {@link MemberRole#MASTER_MANAGER} 인지 여부를 통해 마스터 권한
 * 보유 여부를 판단한다. 도메인 계층에서는 {@link PermissionChecker} 인터페이스에만 의존하며, 이 구현체는 인프라스트럭처 계층에서 실제 조회 로직을
 * 제공한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class PermissionCheckerImpl implements PermissionChecker {

  private final MemberRepository memberRepository;

  /**
   * 주어진 요청자가 대상 회원을 관리할 수 있는 마스터 권한을 가지고 있는지 확인한다.
   *
   * <p>먼저 요청자의 ID로 {@link Member} 를 조회하고, 회원이 존재하지 않을 경우 {@link MemberException} 을 발생시킨다. 이후 요청자의
   * 역할이 {@link MemberRole#MASTER_MANAGER} 인지 여부를 반환한다.
   *
   * @param requesterId 권한을 확인할 요청자(회원)의 UUID
   * @param targetMemberId 권한이 행사될 대상 회원의 ID (현재 구현에서는 사용하지 않지만, 향후 대상 회원별 세부 권한 검증 시 활용 가능)
   * @return 요청자의 역할이 {@link MemberRole#MASTER_MANAGER} 이면 {@code true}, 그렇지 않으면 {@code false}
   * @throws MemberException 요청자 회원이 존재하지 않는 경우
   */
  @Override
  public boolean hasMasterPermission(UUID requesterId, MemberId targetMemberId) {
    // 요청자가 존재하는지 조회
    Member requester =
        memberRepository
            .findById(MemberId.of(requesterId))
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

    // 요청자의 role이 MASTER_MANAGER인지 확인
    return requester.getRole() == MemberRole.MASTER_MANAGER;
  }
}
