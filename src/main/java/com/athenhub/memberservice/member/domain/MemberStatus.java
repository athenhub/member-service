package com.athenhub.memberservice.member.domain;

public enum MemberStatus {
  PENDING,
  ACTIVATED,
  REJECTED,
  DEACTIVATED; // 승인 했으나 삭제된 경우
}
