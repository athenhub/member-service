package com.athenhub.memberservice.member.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.athenhub.memberservice.member.MemberFixture;
import com.athenhub.memberservice.member.domain.dto.request.MemberRegisterRequest;
import com.athenhub.memberservice.member.domain.exception.MemberErrorCode;
import com.athenhub.memberservice.member.domain.exception.MemberException;
import com.athenhub.memberservice.member.domain.service.MemberExistenceChecker;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    MemberRegisterRequest req = MemberFixture.createRegisterRequest();
    MemberExistenceChecker checker = MemberFixture.mockExistenceChecker(false, false, false);

    Member member = Member.signUp(req, memberId, checker);

    assertNotNull(member);
    assertEquals(memberId, member.getId().toUuid());
    assertEquals(MemberStatus.PENDING, member.getStatus());
  }

  @Test
  @DisplayName("memberId가 중복이면 USED_MEMBER_INFO 예외가 발생한다")
  void signUp_fail_memberIdDuplicated() {
    UUID memberId = UUID.randomUUID();
    MemberRegisterRequest req = MemberFixture.createRegisterRequest();
    MemberExistenceChecker checker = MemberFixture.mockExistenceChecker(true, false, false);

    MemberException ex =
        assertThrows(MemberException.class, () -> Member.signUp(req, memberId, checker));

    assertEquals(MemberErrorCode.USED_MEMBER_INFO.getCode(), ex.getCode());
  }

  @Test
  @DisplayName("username이 중복이면 USED_MEMBER_INFO 예외가 발생한다")
  void signUp_fail_usernameDuplicated() {
    UUID memberId = UUID.randomUUID();
    MemberRegisterRequest req = MemberFixture.createRegisterRequest();
    MemberExistenceChecker checker = MemberFixture.mockExistenceChecker(false, true, false);

    MemberException ex =
        assertThrows(MemberException.class, () -> Member.signUp(req, memberId, checker));

    assertEquals(MemberErrorCode.USED_MEMBER_INFO.getCode(), ex.getCode());
  }

  @Test
  @DisplayName("slackId가 중복이면 USED_MEMBER_INFO 예외가 발생한다")
  void signUp_fail_slackIdDuplicated() {
    UUID memberId = UUID.randomUUID();
    MemberRegisterRequest req = MemberFixture.createRegisterRequest();
    MemberExistenceChecker checker = MemberFixture.mockExistenceChecker(false, false, true);

    MemberException ex =
        assertThrows(MemberException.class, () -> Member.signUp(req, memberId, checker));

    assertEquals(MemberErrorCode.USED_MEMBER_INFO.getCode(), ex.getCode());
  }
}
