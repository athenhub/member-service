package com.athenhub.memberservice.member.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 회원 등록을 위한 요청 정보 DTO.
 *
 * <p>클라이언트가 Member를 신규 등록(회원 가입 요청)할 때 필요한 필드를 전달하는 데이터 구조이다. 필드별로 {@code @NotBlank},
 * {@code @NotNull} 검증 애너테이션이 적용되어 있으며, 이는 등록 요청 시 반드시 포함되어야 하는 필드를 의미한다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>{@code name} — 회원 이름 (필수)
 *   <li>{@code username} — 계정 ID, 4~10자의 알파벳 소문자 + 숫자 (필수)
 *   <li>{@code slackId} — 슬랙 ID (필수)
 *   <li>{@code password} — 평문 비밀번호. 서버는 이 값을 인증 서버(Keycloak)에 전달만 하고, 자체 DB에는 저장하지 않는다. (필수)
 *   <li>{@code role} — 회원 역할(Role), 예: MASTER_MANAGER, HUB_MANAGER, SHIPPING_AGENT, VENDOR_AGENT
 *       (필수)
 *   <li>{@code organizationType} — 소속 타입, HUB / VENDOR / NONE (필수)
 *   <li>{@code organizationId} — 소속 허브/업체 ID (선택, {@code organizationType}에 따라 필수 여부가 달라짐)
 *   <li>{@code organizationName} — 소속 이름(업체명 또는 허브명, 표시용) (선택)
 * </ul>
 *
 * @author 박성준
 * @since 1.0.0
 */
public record MemberRegisterRequest(
    @NotBlank String name,
    @NotBlank @Pattern(regexp = "^[a-z0-9]{4,10}$") String username,
    @NotBlank String slackId,
    @NotBlank
        @Pattern(
            regexp = "^[A-Za-z0-9!@#$%^&*()_+=-]{8,15}$",
            message = "비밀번호는 8~15자이며 영문 대소문자, 숫자, 지정된 특수문자만 사용할 수 있습니다.")
        String password
) {}
