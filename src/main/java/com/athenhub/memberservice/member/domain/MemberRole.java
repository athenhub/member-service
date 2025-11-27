package com.athenhub.memberservice.member.domain;

/**
 * 회원의 역할(권한)을 나타내는 열거형.
 *
 * <p>각 역할은 시스템 내에서 수행할 수 있는 작업 범위를 구분하기 위한 개념적 권한 단위이다.
 *
 * <ul>
 *   <li>{@link #MASTER_MANAGER} — 전체 시스템을 관리하는 마스터 관리자
 *   <li>{@link #HUB_MANAGER} — 특정 허브에 소속된 허브 관리자
 *   <li>{@link #SHIPPING_AGENT} — 실제 배송 업무를 담당하는 배송 담당자
 *   <li>{@link #VENDOR_AGENT} — 특정 업체(가맹점)를 담당하는 업체 담당자
 * </ul>
 *
 * @author 박성준
 * @since 1.0.0
 */
public enum MemberRole {

  /** 전체 시스템을 관리하는 마스터 관리자 역할 */
  MASTER_MANAGER,

  /** 특정 허브에 소속되어 허브 및 허브 내 리소스를 관리하는 허브 관리자 역할 */
  HUB_MANAGER,

  /** 배송 수행 및 배송 정보 관리 등을 담당하는 배송 담당자 역할 */
  SHIPPING_AGENT,

  /** 특정 업체(가맹점)의 상품/주문 등을 관리하는 업체 담당자 역할 */
  VENDOR_AGENT,

  USER // 권한을 부여 받지 않은 기본 사용자
}
