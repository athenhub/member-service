package com.athenhub.memberservice.member;

import static org.mockito.Mockito.when;

import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.MemberRole;
import com.athenhub.memberservice.member.domain.MemberStatus;
import com.athenhub.memberservice.member.domain.OrganizationType;
import com.athenhub.memberservice.member.domain.dto.request.MemberRegisterRequest;
import com.athenhub.memberservice.member.domain.service.MemberExistenceChecker;
import com.athenhub.memberservice.member.domain.vo.MemberId;
import java.util.UUID;
import org.mockito.Mockito;

/**
 * Member 도메인 테스트용 픽스처 유틸리티.
 *
 * <p>회원 가입 요청 DTO, 회원 존재 여부 체크 목, 기본 속성이 설정된 Member 목 객체를 생성한다.
 *
 * <h2>제공 기능</h2>
 *
 * <ul>
 *   <li>{@link #createMemberMock(UUID)} – 기본 값이 세팅된 Member 목 생성
 *   <li>{@link #createRegisterRequest()} – 기본 회원 가입 요청 DTO 생성
 *   <li>{@link #mockExistenceChecker(boolean, boolean, boolean)} – 중복 플래그에 따른 목 생성
 * </ul>
 *
 * @author 박성준
 * @since 1.0.0
 */
public final class MemberFixture {

  private MemberFixture() {
    // 유틸리티 클래스
  }

  /**
   * 기본 값이 세팅된 {@link Member} 목 객체를 생성한다.
   *
   * <p>다음과 같은 값을 가진다.
   *
   * <ul>
   *   <li>id: 전달된 memberId
   *   <li>username: {@code "user01"}
   *   <li>name: {@code "홍길동"}
   *   <li>slackId: {@code "SLACK_123"}
   *   <li>role: {@link MemberRole#HUB_MANAGER}
   *   <li>status: {@link MemberStatus#PENDING}
   *   <li>organizationType: {@link OrganizationType#HUB}
   *   <li>organizationId, organizationName: {@code null}
   * </ul>
   *
   * @param memberId 회원 식별자 UUID
   * @return 설정된 {@link Member} 목 객체
   */
  public static Member createMemberMock(UUID memberId) {
    Member member = Mockito.mock(Member.class);
    MemberId memberIdVo = MemberId.of(memberId);

    when(member.getId()).thenReturn(memberIdVo);
    when(member.getUsername()).thenReturn("user01");
    when(member.getName()).thenReturn("홍길동");
    when(member.getSlackId()).thenReturn("SLACK_123");
    when(member.getRole()).thenReturn(MemberRole.HUB_MANAGER);
    when(member.getStatus()).thenReturn(MemberStatus.PENDING);
    when(member.getOrganizationType()).thenReturn(OrganizationType.HUB);
    when(member.getOrganizationId()).thenReturn(null);
    when(member.getOrganizationName()).thenReturn(null);

    return member;
  }

  /**
   * 기본값을 사용한 회원 가입 요청 DTO를 생성한다.
   *
   * @return 테스트용 {@link MemberRegisterRequest}
   */
  public static MemberRegisterRequest createRegisterRequest() {
    return new MemberRegisterRequest(
        "홍길동",
        "user01",
        "SLACK_123",
        "Test!1234",
        MemberRole.HUB_MANAGER,
        OrganizationType.HUB,
        "테스트 허브");
  }

  /**
   * 지정된 플래그에 따라 동작하는 {@link MemberExistenceChecker} 목 객체를 생성한다.
   *
   * @param hasMember {@link MemberExistenceChecker#hasMember} 반환값
   * @param usernameExists {@link MemberExistenceChecker#existsByUsername} 반환값
   * @param slackIdExists {@link MemberExistenceChecker#existsBySlackId} 반환값
   * @return 설정된 목 {@link MemberExistenceChecker}
   */
  public static MemberExistenceChecker mockExistenceChecker(
      boolean hasMember, boolean usernameExists, boolean slackIdExists) {
    MemberExistenceChecker checker = Mockito.mock(MemberExistenceChecker.class);
    Mockito.when(checker.hasMember(Mockito.any())).thenReturn(hasMember);
    Mockito.when(checker.existsByUsername(Mockito.anyString())).thenReturn(usernameExists);
    Mockito.when(checker.existsBySlackId(Mockito.anyString())).thenReturn(slackIdExists);
    return checker;
  }
}
