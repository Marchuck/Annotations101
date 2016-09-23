package com.marchuck.app;

import com.marchuck.Buildable;
import com.marchuck.RealmBean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by l.marczak
 *
 * @since 9/8/16.
 */
@RealmBean
@Buildable
public class Sth extends RealmObject {


    @Buildable
    void foooo(){}

    @PrimaryKey
    private String uuid;

    private int age;
    private String name;
    private String surname;
    private String address;

    public Sth() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
