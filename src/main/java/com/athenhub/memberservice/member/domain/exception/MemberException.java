package com.athenhub.memberservice.member.domain.exception;

import com.athenhub.commoncore.error.AbstractServiceException;
import com.athenhub.commoncore.error.ErrorCode;

public class MemberException extends AbstractServiceException {

  public MemberException(ErrorCode errorCode, Object... errorArgs) {
    super(errorCode, errorArgs);
  }
}
