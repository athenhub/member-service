package com.athenhub.memberservice.member.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.athenhub.memberservice.member.MemberFixture;
import com.athenhub.memberservice.member.application.service.MemberCommandService;
import com.athenhub.memberservice.member.domain.Member;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * {@link MemberCommandController}에 대한 Web 계층 단위 테스트.
 *
 * <p>스프링 컨텍스트를 기동하지 않고, {@link MockMvcBuilders#standaloneSetup(Object...)}을 사용하여 컨트롤러만 올린 상태에서 HTTP
 * 요청/응답 매핑과 상태 코드, JSON 구조를 검증한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class MemberCommandControllerTest {

  private MockMvc mockMvc;

  @Mock private MemberCommandService memberCommandService;

  @InjectMocks private MemberCommandController memberCommandController;

  /**
   * 각 테스트 실행 전에 {@link MockMvc}를 초기화한다.
   *
   * <p>{@link MockMvcBuilders#standaloneSetup(Object...)}을 사용하여 {@link MemberCommandController}만
   * 등록된 가벼운 MVC 환경을 구성한다. 이를 통해 스프링 컨텍스트 전체를 기동하지 않고도 HTTP 요청/응답 흐름을 검증할 수 있다.
   *
   * @author 박성준
   * @since 1.0.0
   */
  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(memberCommandController).build();
  }

  /**
   * 회원 가입 요청이 정상 처리될 경우 201(CREATED)와 함께 기대하는 JSON 응답 구조가 반환되는지 검증한다.
   *
   * <p>검증 내용:
   *
   * <ul>
   *   <li>{@link MemberCommandService#signUp}이 호출되어 {@link Member} 인스턴스를 반환하는지
   *   <li>{@link MemberFixture#createMemberMock(UUID)}로 생성한 목 {@link Member}의 값이 응답 JSON에 그대로 반영되는지
   *   <li>HTTP 상태 코드가 201(CREATED)인지
   *   <li>응답 본문의 id, username, name, role, status 필드가 기대값과 일치하는지
   * </ul>
   *
   * @throws Exception MockMvc 수행 중 오류가 발생한 경우
   * @author 박성준
   * @since 1.0.0
   */
  @Test
  void signUp_returnsCreatedWithMemberInfo() throws Exception {
    // given
    UUID memberId = UUID.randomUUID();
    Member member = MemberFixture.createMemberMock(memberId);

    when(memberCommandService.signUp(any(), any())).thenReturn(member);

    String requestJson =
        """
        {
          "name": "홍길동",
          "username": "user01",
          "slackId": "SLACK_123",
          "password": "Passw0rd!",
          "role": "HUB_MANAGER",
          "organizationType": "HUB"
        }
        """;

    // when & then
    mockMvc
        .perform(post("/v1/members").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(memberId.toString()))
        .andExpect(jsonPath("$.username").value("user01"))
        .andExpect(jsonPath("$.name").value("홍길동"))
        .andExpect(jsonPath("$.role").value("HUB_MANAGER"))
        .andExpect(jsonPath("$.status").value("PENDING"));

    verify(memberCommandService).signUp(any(), any());
  }
}
