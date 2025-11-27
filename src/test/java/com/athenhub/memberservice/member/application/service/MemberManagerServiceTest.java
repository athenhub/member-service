package com.athenhub.memberservice.member.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.athenhub.memberservice.member.MemberFixture;
import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.MemberRepository;
import com.athenhub.memberservice.member.domain.MemberStatus;
import com.athenhub.memberservice.member.domain.dto.request.MemberRegisterRequest;
import com.athenhub.memberservice.member.domain.service.IdentityClient;
import com.athenhub.memberservice.member.domain.service.MemberExistenceChecker;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * {@link MemberManagerService}에 대한 단위 테스트.
 *
 * <p>회원 가입 유스케이스에 대해 외부 포트({@link IdentityClient}, {@link MemberExistenceChecker}) 및 리포지토리({@link
 * MemberRepository})와의 상호작용을 검증한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class MemberManagerServiceTest {

  @Mock private IdentityClient identityClient;

  @Mock private MemberExistenceChecker memberExistenceChecker;

  @Mock private MemberRepository memberRepository;

  @InjectMocks private MemberManagerService memberManagerService;

  @Test
  void signUp_success_createsUserInKeycloakAndSavesMember() {
    // given
    MemberRegisterRequest request = MemberFixture.createRegisterRequest();
    String rawPassword = request.password();

    UUID generatedUserId = UUID.randomUUID();
    when(identityClient.createUser(request.username(), rawPassword, request.name()))
        .thenReturn(generatedUserId);

    when(memberRepository.save(any(Member.class)))
        .thenAnswer(invocation -> invocation.getArgument(0, Member.class));

    // when
    Member result = memberManagerService.signUp(request);

    // then
    verify(identityClient).createUser(request.username(), rawPassword, request.name());

    verify(memberRepository).save(any(Member.class));

    assertThat(result).isNotNull();
    assertThat(result.getId().toUuid()).isEqualTo(generatedUserId);
    assertThat(result.getUsername()).isEqualTo(request.username());
    assertThat(result.getStatus()).isEqualTo(MemberStatus.PENDING);
  }

  @Test
  void signUp_identityClientFails_doesNotSaveMember() {
    // given
    MemberRegisterRequest request = MemberFixture.createRegisterRequest();
    String rawPassword = request.password();

    when(identityClient.createUser(request.username(), rawPassword, request.name()))
        .thenThrow(new RuntimeException("Keycloak error"));

    // when & then
    assertThatThrownBy(() -> memberManagerService.signUp(request))
        .isInstanceOf(RuntimeException.class);

    verify(memberRepository, never()).save(any(Member.class));
  }
}
