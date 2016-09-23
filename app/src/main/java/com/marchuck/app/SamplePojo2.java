package com.marchuck.app;

import com.marchuck.RealmBean;
import com.marchuck.WhenClicked;

public class SamplePojo2 {
    public String cat;
    public String dog;

    public SamplePojo2(String cat, String dog) {
        this.cat = cat;
        this.dog = dog;
    }
    @Override
    public String toString() {
        return null;
//        return StringUtil.createString(this);
    }
}
