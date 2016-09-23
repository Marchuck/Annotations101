package com.marchuck.app;

import com.marchuck.RealmBean;
import com.marchuck.WhenClicked;
 
public class SamplePojo {

    public SamplePojo(String fieldA, String fieldB, int fieldC, SamplePojo2 fieldD) {
        this.fieldA = fieldA;
        this.fieldB = fieldB;
        this.fieldC = fieldC;
        this.fieldD = fieldD;
    }
    public String fieldA;
    public String fieldB;
    public int fieldC;
    public SamplePojo2 fieldD;

    @Override
    public String toString() {
        return null;

//        return StringUtil.createString(this);
    }
}
