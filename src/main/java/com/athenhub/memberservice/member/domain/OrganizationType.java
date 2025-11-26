package com.athenhub.memberservice.member.domain;

/**
 * 회원이 속한 조직의 유형을 나타내는 열거형.
 *
 * <p>회원이 어떤 종류의 조직(허브, 업체 등)에 소속되어 있는지를 표현하며, 조직별 권한 제어 및 조회 필터링 등에 사용된다.
 *
 * <ul>
 *   <li>{@link #OTHERS} — 허브/업체 외의 기타 조직 유형
 *   <li>{@link #HUB} — 허브 조직에 소속된 경우
 *   <li>{@link #VENDOR} — 업체(가맹점) 조직에 소속된 경우
 * </ul>
 *
 * @author 박성준
 * @since 1.0.0
 */
public enum OrganizationType {

  /** 허브/업체 외의 기타 조직 유형 */
  OTHERS,

  /** 허브 조직에 소속된 경우 */
  HUB,

  /** 업체(가맹점) 조직에 소속된 경우 */
  VENDOR
}
