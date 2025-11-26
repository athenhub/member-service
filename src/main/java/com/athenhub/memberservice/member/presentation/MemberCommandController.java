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

@RestController
@RequestMapping("/api/v1/members")
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
