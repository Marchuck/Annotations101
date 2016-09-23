package com.marchuck;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

class NoPackageNameException extends Exception {

  public NoPackageNameException(Element typeElement) {
    super("The package of " + typeElement.getSimpleName() + " has no name");
  }
}
