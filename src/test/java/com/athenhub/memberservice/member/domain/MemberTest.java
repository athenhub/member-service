package com.athenhub.memberservice.member.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.athenhub.memberservice.member.domain.dto.request.MemberRegisterRequest;
import com.athenhub.memberservice.member.domain.exception.MemberErrorCode;
import com.athenhub.memberservice.member.domain.exception.MemberException;
import com.athenhub.memberservice.member.domain.service.MemberExistenceChecker;
import com.athenhub.memberservice.member.domain.vo.OrganizationId;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * {@link Member#signUp(MemberRegisterRequest, UUID, MemberExistenceChecker)} 테스트.
 *
 * <p>중복 검증과 초기 상태(PENDING) 설정만 검증한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
class MemberTest {

  @Test
  @DisplayName("회원 가입 성공이면 PENDING 상태의 Member가 생성된다")
  void signUp_success() {
    UUID memberId = UUID.randomUUID();
    MemberRegisterRequest req = createRequest();
    MemberExistenceChecker checker = mockChecker(false, false, false);

    Member member = Member.signUp(req, memberId, checker);

    assertNotNull(member);
    assertEquals(memberId, member.getId().toUuid());
    assertEquals(MemberStatus.PENDING, member.getStatus());
  }

  @Test
  @DisplayName("memberId가 중복이면 USED_MEMBER_INFO 예외가 발생한다")
  void signUp_fail_memberIdDuplicated() {
    UUID memberId = UUID.randomUUID();
    MemberRegisterRequest req = createRequest();
    MemberExistenceChecker checker = mockChecker(true, false, false);

    MemberException ex =
        assertThrows(MemberException.class, () -> Member.signUp(req, memberId, checker));

    assertEquals(MemberErrorCode.USED_MEMBER_INFO.getCode(), ex.getCode());
  }

  @Test
  @DisplayName("username이 중복이면 USED_MEMBER_INFO 예외가 발생한다")
  void signUp_fail_usernameDuplicated() {
    UUID memberId = UUID.randomUUID();
    MemberRegisterRequest req = createRequest();
    MemberExistenceChecker checker = mockChecker(false, true, false);

    MemberException ex =
        assertThrows(MemberException.class, () -> Member.signUp(req, memberId, checker));

    assertEquals(MemberErrorCode.USED_MEMBER_INFO.getCode(), ex.getCode());
  }

  @Test
  @DisplayName("slackId가 중복이면 USED_MEMBER_INFO 예외가 발생한다")
  void signUp_fail_slackIdDuplicated() {
    UUID memberId = UUID.randomUUID();
    MemberRegisterRequest req = createRequest();
    MemberExistenceChecker checker = mockChecker(false, false, true);

    MemberException ex =
        assertThrows(MemberException.class, () -> Member.signUp(req, memberId, checker));

    assertEquals(MemberErrorCode.USED_MEMBER_INFO.getCode(), ex.getCode());
  }

  private MemberRegisterRequest createRequest() {
    return new MemberRegisterRequest(
        "홍길동",
        "user01",
        "SLACK_123",
        "Test!1234",
        MemberRole.HUB_MANAGER,
        OrganizationType.HUB,
        OrganizationId.of(UUID.randomUUID()),
        "테스트 허브");
  }

  private MemberExistenceChecker mockChecker(
      boolean hasMember, boolean usernameExists, boolean slackIdExists) {
    MemberExistenceChecker checker = Mockito.mock(MemberExistenceChecker.class);
    Mockito.when(checker.hasMember(Mockito.any())).thenReturn(hasMember);
    Mockito.when(checker.existsByUsername(Mockito.anyString())).thenReturn(usernameExists);
    Mockito.when(checker.existsBySlackId(Mockito.anyString())).thenReturn(slackIdExists);
    return checker;
  }
}
