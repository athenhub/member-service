package com.athenhub.memberservice.member.presentation.webapi.dto;

import java.util.List;

public record MemberListResponse(
    List<MemberProfileResponse> contents,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean last
) {



}
