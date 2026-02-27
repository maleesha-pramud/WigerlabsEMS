package com.wigerlabs.wigerlabs_ems.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserDTO implements Serializable {
    private int id;
    private String name;
    private String email;
    private String password;
    private int userRoleId;
    private int positionId;
    private int departmentId;
    private int statusId;
    private LocalDateTime hireDate;
    private java.math.BigDecimal salary;

    public UserDTO() {
    }

    public UserDTO(int id, String name, int userRoleId, int positionId, int departmentId, int statusId, LocalDateTime hireDate, java.math.BigDecimal salary) {
        this.id = id;
        this.name = name;
        this.userRoleId = userRoleId;
        this.positionId = positionId;
        this.departmentId = departmentId;
        this.statusId = statusId;
        this.hireDate = hireDate;
        this.salary = salary;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDateTime getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDateTime hireDate) {
        this.hireDate = hireDate;
    }

    public java.math.BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(java.math.BigDecimal salary) {
        this.salary = salary;
    }
}
