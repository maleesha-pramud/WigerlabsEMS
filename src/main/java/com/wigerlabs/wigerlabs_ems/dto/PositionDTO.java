package com.wigerlabs.wigerlabs_ems.dto;

import java.io.Serializable;

public class PositionDTO implements Serializable {
    private int id;
    private String name;

    public PositionDTO() {
    }

    public PositionDTO(int id, String name) {
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

