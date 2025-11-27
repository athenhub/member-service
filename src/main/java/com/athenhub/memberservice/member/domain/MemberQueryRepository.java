package com.athenhub.memberservice.member.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberQueryRepository {


  Page<Member> search(String keyword, Pageable pageable);
}
