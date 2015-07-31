package com.dsoft.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @Author: Md. Habibur Rahman
 * Date: 25/07/2015
 * Time: 12:07 PM
 * To change this template use File | Settings | File Templates.
 */
public enum Role {

    SUPER_ADMIN( 1, "superAdmin", "SUPER_ADMIN"),
    ROLE_ADMIN( 2, "Admin", "ROLE_ADMIN"),
    ROLE_EMPLOYEE( 3, "Employee", "ROLE_EMPLOYEE");


    private String label;
    private String value;
    private int code;

    private Role(int code, String value, String label) {
        this.code = code;
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static String getLabelForValue(String value) {
        for (Role type : values()) {
            if (type.getValue().equals(value)) {
                return type.getLabel();
            }
        }
        return value;
    }

    public static List getRoles() {
        List<Role> rules = new ArrayList();
        rules.add(ROLE_ADMIN);
        rules.add(ROLE_EMPLOYEE);
        return rules;
    }
}