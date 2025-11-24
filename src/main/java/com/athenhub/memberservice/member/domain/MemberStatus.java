package com.athenhub.memberservice.member.domain;

import java.util.Map;
import java.util.Set;

public enum MemberStatus {
  PENDING,
  ACTIVATED,
  REJECTED,
  DEACTIVATED; // 승인 했으나 삭제된 경우
}

