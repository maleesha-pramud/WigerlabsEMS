package com.wigerlabs.wigerlabs_ems.dto;

import java.io.Serializable;

public class DepartmentDTO implements Serializable {
    private int id;
    private String name;

    public DepartmentDTO() {
    }

    public DepartmentDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
