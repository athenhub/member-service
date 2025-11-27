package com.athenhub.memberservice.infrastructure.security;

import com.athenhub.memberservice.member.domain.Member;
import com.athenhub.memberservice.member.domain.MemberRepository;
import com.athenhub.memberservice.member.domain.MemberRole;
import com.athenhub.memberservice.member.domain.vo.MemberId;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
  private final MemberRepository memberRepository;

  @Override
  public Collection<GrantedAuthority> convert(Jwt source) {

    String userId = source.getSubject();
    MemberId memberId = MemberId.of(UUID.fromString(userId));
    Member member = memberRepository.findById(memberId).orElse(null);
    if (member == null) {
      return List.of();
    }

    MemberRole role = Objects.requireNonNullElse(member.getRole(), MemberRole.USER);

    return  List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
  }
}
