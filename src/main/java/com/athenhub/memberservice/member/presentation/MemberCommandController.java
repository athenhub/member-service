package com.athenhub.memberservice.member.presentation;

import com.athenhub.memberservice.member.application.service.MemberCommandService;
import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.dto.request.MemberRegisterRequest;
import com.athenhub.memberservice.member.domain.dto.response.MemberRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 도메인의 명령성 HTTP 요청(등록 등)을 처리하는 컨트롤러.
 *
 * <p>요청 DTO를 애플리케이션 서비스에 전달하고, 도메인 결과를 응답 DTO로 변환하여 반환한다.
 *
 * @author 박성준
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberCommandController {

  private final MemberCommandService memberCommandService;

  @PostMapping
  public ResponseEntity<MemberRegisterResponse> signUp(
      @Valid @RequestBody MemberRegisterRequest request) {
    String rawPassword = request.password(); // 예시

    Member member = memberCommandService.signUp(request, rawPassword);

    return ResponseEntity.status(HttpStatus.CREATED).body(MemberRegisterResponse.from(member));
  }
}
