package com.athenhub.memberservice.member.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 회원이 자신의 정보를 수정할 때 사용하는 요청 DTO이다.
 *
 * <p>현재는 슬랙 ID만 수정 대상으로 포함하고 있으며, 필수 값에 대해서는 {@link NotBlank} 애너테이션을 통해 공백 문자열 여부를 검증한다.
 *
 * <ul>
 *   <li>{@code slackId} — 회원의 슬랙 ID (필수)
 * </ul>
 *
 * @author 박성준
 * @since 1.0.0
 * @param slackId 수정할 회원의 슬랙 ID
 */
public record MemberUpdateInfoRequest(@NotBlank String slackId) {}
