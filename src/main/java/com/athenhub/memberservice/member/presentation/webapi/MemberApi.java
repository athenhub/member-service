package com.athenhub.memberservice.member.presentation.webapi;

import com.athenhub.memberservice.member.application.service.MemberManagerService;
import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.dto.request.MemberMasterUpdateRequest;
import com.athenhub.memberservice.member.domain.dto.request.MemberRegisterRequest;
import com.athenhub.memberservice.member.domain.dto.request.MemberUpdateInfoRequest;
import com.athenhub.memberservice.member.presentation.webapi.dto.MemberMasterUpdateResponse;
import com.athenhub.memberservice.member.presentation.webapi.dto.MemberRegisterResponse;
import com.athenhub.memberservice.member.presentation.webapi.dto.MemberUpdateInfoRegisterResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
public class MemberApi {

  private final MemberManagerService memberManagerService;

  /**
   * 회원 가입 요청을 처리한다.
   *
   * <p>요청 본문으로부터 {@link MemberRegisterRequest}를 검증한 뒤, {@link
   * MemberManagerService#signUp(MemberRegisterRequest, String)}를 호출하여 회원 가입 유스케이스를 수행하고, 결과를 {@link
   * MemberRegisterResponse}로 변환하여 반환한다.
   *
   * @param request 회원 가입 요청 정보 DTO
   * @return 생성된 회원 정보 응답 DTO와 201(CREATED) 상태 코드
   */
  @PostMapping
  public ResponseEntity<MemberRegisterResponse> signUp(
      @Valid @RequestBody MemberRegisterRequest request) {

    Member member = memberManagerService.signUp(request, request.password());
    MemberRegisterResponse response = MemberRegisterResponse.from(member);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{memberId}")
  public ResponseEntity<MemberUpdateInfoRegisterResponse> updateInfo(
      @Valid @RequestBody MemberUpdateInfoRequest request, @PathVariable UUID memberId) {
    Member member = memberManagerService.updateInfo(memberId, request);
    MemberUpdateInfoRegisterResponse response = MemberUpdateInfoRegisterResponse.from(member);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/admin/{memberId}")
  public ResponseEntity<MemberMasterUpdateResponse> masterUpdate(
      @Valid @RequestBody MemberMasterUpdateRequest request, @PathVariable UUID memberId) {
    Member member = memberManagerService.updateByMaster(memberId, request);
    MemberMasterUpdateResponse response = MemberMasterUpdateResponse.from(member);
    return ResponseEntity.ok(response);
  }
}
