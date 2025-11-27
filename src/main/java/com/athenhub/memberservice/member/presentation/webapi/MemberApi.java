package com.athenhub.memberservice.member.presentation.webapi;

import com.athenhub.memberservice.application.dto.TokenInfo;
import com.athenhub.memberservice.application.service.TokenGenerateService;
import com.athenhub.memberservice.member.application.service.MemberManagerService;
import com.athenhub.memberservice.member.application.service.MemberProfileService;
import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.MemberStatus;
import com.athenhub.memberservice.member.domain.dto.request.MemberChangeStatusRequest;
import com.athenhub.memberservice.member.domain.dto.request.MemberMasterUpdateRequest;
import com.athenhub.memberservice.member.domain.dto.request.MemberRegisterRequest;
import com.athenhub.memberservice.member.domain.dto.request.MemberUpdateInfoRequest;
import com.athenhub.memberservice.member.domain.exception.MemberErrorCode;
import com.athenhub.memberservice.member.domain.exception.MemberException;
import com.athenhub.memberservice.member.domain.service.IdentityClient;
import com.athenhub.memberservice.member.presentation.webapi.dto.MemberApproveResponse;
import com.athenhub.memberservice.member.presentation.webapi.dto.MemberChangeStatusResponse;
import com.athenhub.memberservice.member.presentation.webapi.dto.MemberMasterUpdateResponse;
import com.athenhub.memberservice.member.presentation.webapi.dto.MemberProfileResponse;
import com.athenhub.memberservice.member.presentation.webapi.dto.MemberRegisterResponse;
import com.athenhub.memberservice.member.presentation.webapi.dto.MemberRejectResponse;
import com.athenhub.memberservice.member.presentation.webapi.dto.MemberUpdateInfoRegisterResponse;
import com.athenhub.memberservice.member.presentation.webapi.token.TokenRequest;
import com.athenhub.memberservice.member.presentation.webapi.token.TokenResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequiredArgsConstructor
public class MemberApi {

  private final MemberManagerService memberManagerService;
  private final MemberProfileService memberProfileService;
  private final TokenGenerateService tokenService;
  private final IdentityClient identityClient;
  /**
   * 회원 가입 요청을 처리한다.
   *
   * <p>요청 본문으로부터 {@link MemberRegisterRequest}를 검증한 뒤, {@link
   * MemberRegisterResponse}로 변환하여 반환한다.
   *
   * @param request 회원 가입 요청 정보 DTO
   * @return 생성된 회원 정보 응답 DTO와 201(CREATED) 상태 코드
   */
  @PostMapping
  public ResponseEntity<MemberRegisterResponse> signUp(
      @Valid @RequestBody MemberRegisterRequest request) {

    Member member = memberManagerService.signUp(request);
    MemberRegisterResponse response = MemberRegisterResponse.from(member, identityClient);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/profile")
  @PreAuthorize("isAuthenticated()")
  public MemberProfileResponse findMyProfile(@AuthenticationPrincipal Jwt jwt) {
    if (jwt == null) throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
    UUID id = UUID.fromString(jwt.getSubject());

    return memberProfileService.getProfile(id);
  }

  @GetMapping("/profile/{memberId}")
  @PreAuthorize("isAuthenticated()")
  public MemberProfileResponse findProfile(@PathVariable UUID memberId) {
    return memberProfileService.getProfile(memberId);
  }

  @GetMapping("/profile/{organizationId}/shipping-agent")
  @PreAuthorize("isAuthenticated()")
  public MemberProfileResponse findVendorShippingAgent(@PathVariable UUID organizationId) {
    return memberProfileService.getVendorShippingAgent(organizationId);
  }

  @GetMapping("/profile/shipping-agent")
  @PreAuthorize("isAuthenticated()")
  public MemberProfileResponse findHubShippingAgent() {
    return memberProfileService.getHubShippingAgent();
  }

  @GetMapping("/profiles")
  @PreAuthorize("isAuthenticated()")
  public Page<MemberProfileResponse> findProfiles(@AuthenticationPrincipal Jwt jwt,@RequestParam String keyword, Pageable pageable) {
    if (jwt == null) throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);

    return memberProfileService.searchProfiles(keyword, pageable);
  }


  @PutMapping("/{memberId}")
  public ResponseEntity<MemberUpdateInfoRegisterResponse> updateInfo(
      @Valid @RequestBody MemberUpdateInfoRequest request, @PathVariable UUID memberId) {
    Member member = memberManagerService.updateInfo(memberId, request);
    MemberUpdateInfoRegisterResponse response = MemberUpdateInfoRegisterResponse.from(member);
    return ResponseEntity.ok(response);
  }

  @PreAuthorize("hasRole('MASTER_MANAGER')")
  @PutMapping("/{memberId}/master")
  public ResponseEntity<MemberMasterUpdateResponse> masterUpdate(
      @Valid @RequestBody MemberMasterUpdateRequest request, @PathVariable UUID memberId) {
    Member member = memberManagerService.updateByMaster(memberId, request);
    MemberMasterUpdateResponse response = MemberMasterUpdateResponse.from(member);
    return ResponseEntity.ok(response);
  }

  @PreAuthorize("hasRole('MASTER_MANAGER')")
  @PutMapping("/{memberId}/approve")
  public ResponseEntity<MemberApproveResponse> approveMember(
      @PathVariable UUID memberId,
      @AuthenticationPrincipal Jwt jwt
  ) {
    UUID requestId = UUID.fromString(jwt.getSubject());

    Member member = memberManagerService.aproveMember(memberId, requestId);
    MemberApproveResponse response = MemberApproveResponse.from(member);
    return ResponseEntity.ok(response);
  }

  @PreAuthorize("hasRole('MASTER_MANAGER')")
  @PutMapping("/{memberId}/reject")
  public ResponseEntity<MemberRejectResponse> rejectMember(
      @PathVariable UUID memberId,
      @AuthenticationPrincipal Jwt jwt
  ) {
    UUID requestId = UUID.fromString(jwt.getSubject());
    Member member = memberManagerService.rejectMember(memberId, requestId);
    MemberRejectResponse response = MemberRejectResponse.from(member);
    return ResponseEntity.ok(response);
  }

  @PreAuthorize("hasRole('MASTER_MANAGER')")
  @PutMapping("/{memberId}/status")
  public ResponseEntity<MemberChangeStatusResponse> changeStatus(
      @PathVariable UUID memberId,
      @RequestBody @Valid MemberChangeStatusRequest request
  ) {

    MemberStatus status = MemberStatus.valueOf(request.status());

    Member member = memberManagerService.changeStatus(memberId, request);

    MemberChangeStatusResponse response = MemberChangeStatusResponse.from(member);

    return ResponseEntity.ok(response);
  }


  @PreAuthorize("hasRole('MASTER_MANAGER')")
  @DeleteMapping("/{memberId}")
  public ResponseEntity<Void> deleteMember(
      @PathVariable UUID memberId,
      @AuthenticationPrincipal Jwt jwt
  ) {
    UUID requestId = UUID.fromString(jwt.getSubject());
    memberManagerService.deleteMember(memberId, requestId);
    return ResponseEntity.noContent().build();
  }




  @PostMapping("token")
  public TokenResponse generateToken(@Valid @RequestBody TokenRequest req) {
    TokenInfo tokenInfo = tokenService.generateToken(req.username(), req.password());

    return new TokenResponse(
        tokenInfo.access_token(),
        tokenInfo.expires_in(),
        tokenInfo.refresh_expires_in(),
        tokenInfo.refresh_token(),
        tokenInfo.token_type());
  }
}
