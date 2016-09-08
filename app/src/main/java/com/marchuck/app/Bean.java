package com.marchuck.app;

import com.marchuck.RealmBean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by l.marczak
 *
 * @since 9/8/16.
 */
@RealmBean
public class Bean extends RealmObject {
    @PrimaryKey
    private String uuid;

    private int age;
    private String name;

    private boolean isAdult;

    public Bean() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }
}
