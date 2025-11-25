package com.athenhub.memberservice.member.domain.dto.request;

import com.athenhub.memberservice.member.domain.MemberRole;
import com.athenhub.memberservice.member.domain.MemberStatus;
import com.athenhub.memberservice.member.domain.OrganizationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 마스터 관리자가 회원 정보를 수정할 때 사용하는 요청 DTO이다.
 *
 * <p>회원의 이름, 역할, 소속 유형 및 소속명, 상태를 변경할 수 있으며, 필수 값에 대해서는
 * Bean Validation 애너테이션을 통해 유효성이 검증된다.
 *
 * <ul>
 *   <li>{@code name} — 회원 이름 (필수)</li>
 *   <li>{@code role} — 회원 역할(권한) 정보 (필수)</li>
 *   <li>{@code organizationType} — 회원이 속한 조직 유형 (필수)</li>
 *   <li>{@code organizationName} — 회원이 속한 조직 이름 (선택)</li>
 *   <li>{@code status} — 회원 상태(예: PENDING, ACTIVE, INACTIVE 등) (필수)</li>
 * </ul>
 *
 * @author 박성준
 * @since 1.0.0
 *
 * @param name 수정할 회원 이름
 * @param role 수정할 회원 역할
 * @param organizationType 수정할 조직 유형
 * @param organizationName 수정할 조직 이름
 * @param status 수정할 회원 상태
 */
public record MemberMasterUpdateRequest(
    @NotBlank String name,
    @NotNull MemberRole role,
    @NotNull OrganizationType organizationType,
    String organizationName,
    @NotNull MemberStatus status) {}
