package com.athenhub.memberservice.member.application.service;

import com.athenhub.memberservice.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberQueryService {

  private final MemberRepository memberRepository;

}
