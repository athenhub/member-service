package com.athenhub.memberservice.member.infrastructure;

import com.athenhub.memberservice.member.application.service.MemberProfileService;
import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.MemberQueryRepository;
import com.athenhub.memberservice.member.domain.MemberRepository;
import com.athenhub.memberservice.member.domain.MemberRole;
import com.athenhub.memberservice.member.domain.MemberStatus;
import com.athenhub.memberservice.member.domain.exception.MemberErrorCode;
import com.athenhub.memberservice.member.domain.exception.MemberException;
import com.athenhub.memberservice.member.domain.service.IdentityClient;
import com.athenhub.memberservice.member.domain.vo.MemberId;
import com.athenhub.memberservice.member.domain.vo.OrganizationId;
import com.athenhub.memberservice.member.presentation.webapi.dto.MemberProfileResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberProfileServiceImpl implements MemberProfileService {

  private final MemberRepository memberRepository;
  private final IdentityClient identityClient;
  private final MemberQueryRepository memberQueryRepository;

  @Override
  public MemberProfileResponse getProfile(UUID memberId) {

    Member member = memberRepository.findById(MemberId.of(memberId))
        .orElseThrow(() -> new MemberException(
            MemberErrorCode.MEMBER_NOT_FOUND));

    UUID userId = member.getId().toUuid();
    return MemberProfileResponse.builder()
        .id(userId)
        .name(member.getName())
        .username(identityClient.getUserName(userId))
        .slackId(member.getSlackId())
        .role(member.getRole().name())
        .status(member.getStatus().name())
        .organizationType(member.getOrganizationType().name())
        .organizationName(member.getOrganizationName())
        .isActivated(!member.isDeleted() && member.getStatus().equals(MemberStatus.ACTIVATED))
        .build();
  }

  @Override
  public MemberProfileResponse getVendorShippingAgent(UUID organizationId) {
    Member member = memberRepository.findAllByRole(MemberRole.SHIPPING_AGENT)
        .stream()
        .filter(m -> m.getOrganizationId().equals(OrganizationId.of(organizationId)))
        .findFirst()
        .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    return MemberProfileResponse.from(member, identityClient.getUserName(member.getId().toUuid()));
  }

  @Override
  public MemberProfileResponse getHubShippingAgent() {
    Member member = memberRepository.findAllByRole(MemberRole.SHIPPING_AGENT)
        .stream()
        .filter(m -> m.getOrganizationId() == null)
        .findFirst()
        .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    return MemberProfileResponse.from(member, identityClient.getUserName(member.getId().toUuid()));
  }

  @Override
  public Page<MemberProfileResponse> searchProfiles(String keyword, Pageable pageable) {
    Page<Member> page = memberQueryRepository.search(keyword, pageable);

    return page.map(this::toProfileResponse);
  }

  private MemberProfileResponse toProfileResponse(Member member) {
    UUID userId = member.getId().toUuid();

    return MemberProfileResponse.builder()
        .id(userId)
        .name(member.getName())
        .username(identityClient.getUserName(userId))
        .slackId(member.getSlackId())
        .role(member.getRole().name())
        .status(member.getStatus().name())
        .organizationType(member.getOrganizationType().name())
        .organizationName(member.getOrganizationName())
        .isActivated(!member.isDeleted() && member.getStatus().equals(MemberStatus.ACTIVATED))
        .build();
  }
}

