package com.athenhub.memberservice.member.application.service;

import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.MemberRepository;
import com.athenhub.memberservice.member.domain.dto.request.MemberMasterUpdateRequest;
import com.athenhub.memberservice.member.domain.dto.request.MemberRegisterRequest;
import com.athenhub.memberservice.member.domain.dto.request.MemberUpdateInfoRequest;
import com.athenhub.memberservice.member.domain.exception.MemberErrorCode;
import com.athenhub.memberservice.member.domain.exception.MemberException;
import com.athenhub.memberservice.member.domain.service.IdentityClient;
import com.athenhub.memberservice.member.domain.service.MemberExistenceChecker;
import com.athenhub.memberservice.member.domain.service.PermissionChecker;
import com.athenhub.memberservice.member.domain.vo.MemberId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * 회원 도메인에 대한 명령성 유스케이스를 제공하는 애플리케이션 서비스.
 *
 * <p>회원 가입과 같이 쓰기(상태 변경)가 수반되는 흐름을 오케스트레이션하며, 도메인 계층({@link Member}, {@link
 * MemberExistenceChecker})과 외부 인증 시스템({@link IdentityClient}) 및 영속 계층({@link MemberRepository}) 사이를
 * 중재한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberManagerService {

  private final IdentityClient identityClient;
  private final MemberExistenceChecker memberExistenceChecker;
  private final MemberRepository memberRepository;
  private final PermissionChecker permissionChecker;

  /**
   * 회원 가입(sign-up) 유스케이스를 수행한다.
   *
   * <p>처리 절차는 다음과 같다.
   *
   * <ol>
   *   <li>{@link IdentityClient}를 통해 외부 인증 시스템에 계정을 생성하고 발급된 사용자 식별자(UUID)를 조회한다.
   *   <li>{@link Member#signUp(MemberRegisterRequest, UUID, MemberExistenceChecker)}를 호출하여 도메인
   *       규칙(중복 검증 포함)에 따라 {@link Member} 애그리거트를 생성한다.
   *   <li>생성된 {@link Member}를 {@link MemberRepository}를 통해 저장한다.
   * </ol>
   *
   * <p>이 메서드는 트랜잭션 경계 내에서 실행되며, member DB에 대한 저장 작업이 하나의 트랜잭션으로 처리된다.
   *
   * @param request 회원 가입 요청 정보 DTO
   * @param rawPassword 외부 인증 시스템에 전달할 평문 비밀번호
   * @return 저장된 회원 엔터티
   */
  public Member signUp(MemberRegisterRequest request, String rawPassword) {
    // 1) Keycloak에 계정 생성
    UUID userId = identityClient.createUser(request.username(), rawPassword, request.name());

    // 2) 도메인 엔티티 생성 (중복 검증 포함)
    Member member = Member.signUp(request, userId, memberExistenceChecker);

    // 3) member DB 저장
    memberRepository.save(member);
    return member;
  }

  public Member updateInfo(UUID memberId, MemberUpdateInfoRequest updateRequest) {

    Member member =
        memberRepository
            .findById(MemberId.of(memberId))
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

    member.updateInfo(updateRequest, memberExistenceChecker);

    memberRepository.save(member);

    return member;
  }

  public Member updateByMaster(UUID memberId, MemberMasterUpdateRequest updateRequest) {
    Member member =
        memberRepository
            .findById(MemberId.of(memberId))
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

    member.updateByMaster(updateRequest, permissionChecker, memberExistenceChecker);

    memberRepository.save(member);

    return member;
  }
}
