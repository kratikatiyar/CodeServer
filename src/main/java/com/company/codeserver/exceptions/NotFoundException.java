package com.company.codeserver.exceptions;

import com.company.codeserver.entities.Project;

public class NotFoundException extends Exception {
  public NotFoundException(Class<?> projectClass, long id) {
  }

}
