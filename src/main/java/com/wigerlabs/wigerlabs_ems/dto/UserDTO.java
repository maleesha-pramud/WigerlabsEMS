package com.wigerlabs.wigerlabs_ems.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private int id;
    private String name;
    private int userRoleId;
    private int positionId;
    private int departmentId;
    private int statusId;

    public UserDTO() {
    }

    public UserDTO(int id, String name, int userRoleId, int positionId, int departmentId, int statusId) {
        this.id = id;
        this.name = name;
        this.userRoleId = userRoleId;
        this.positionId = positionId;
        this.departmentId = departmentId;
        this.statusId = statusId;
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

    public int getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(int userRoleId) {
        this.userRoleId = userRoleId;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}

