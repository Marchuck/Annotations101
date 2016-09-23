package com.marchuck;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

final class Utils {

    private Utils() {
        // no instances
    }

    static String getPackageName(Elements elementUtils, Element type) throws NoPackageNameException {
        PackageElement pkg = elementUtils.getPackageOf(type);

        if (pkg.isUnnamed()) {
            throw new NoPackageNameException(type);
        }
        return pkg.getQualifiedName().toString();
    }
}
