package com.example.daoyun09.entity;

public class Role {

private static final long serialVersionUID=1L;

    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    private Integer isStudent;

    /**
     * 描述
     */
    private String description;

    private String powerId;

    /**
     * 状态
     */
    private Integer state;

    private Integer isDelete;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsStudent() {
        return isStudent;
    }

    public void setIsStudent(Integer isStudent) {
        this.isStudent = isStudent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPowerId() {
        return powerId;
    }

    public void setPowerId(String powerId) {
        this.powerId = powerId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "Role{" +
        "id=" + id +
        ", name=" + name +
        ", isStudent=" + isStudent +
        ", description=" + description +
        ", powerId=" + powerId +
        ", state=" + state +
        ", isDelete=" + isDelete +
        "}";
    }
}