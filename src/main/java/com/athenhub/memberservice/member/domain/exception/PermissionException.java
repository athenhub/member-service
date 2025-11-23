package com.athenhub.memberservice.member.domain.exception;

import com.athenhub.commoncore.error.AbstractServiceException;
import com.athenhub.commoncore.error.ErrorCode;

public class PermissionException extends AbstractServiceException {

  public PermissionException(ErrorCode errorCode, Object... errorArgs) {
    super(errorCode, errorArgs);
  }
}
