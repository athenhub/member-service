package com.athenhub.memberservice.member.application.service;


import com.athenhub.memberservice.member.presentation.webapi.dto.MemberProfileResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberProfileService {
  MemberProfileResponse getProfile(UUID memberId);

  MemberProfileResponse getVendorShippingAgent(UUID organizationId);

  MemberProfileResponse getHubShippingAgent();

  Page<MemberProfileResponse> searchProfiles(String keyword, Pageable pageable);
}
