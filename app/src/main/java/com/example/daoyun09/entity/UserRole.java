package com.example.daoyun09.entity;

public class UserRole {

private static final long serialVersionUID=1L;

    private Long id;

    private Long roleId;

    private Long userId;

    private Integer isDelete;

    public UserRole(){
        this.isDelete = 0;
        this.roleId = Long.valueOf(0);
        this.userId = Long.valueOf(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "UserRole{" +
        "id=" + id +
        ", roleId=" + roleId +
        ", userId=" + userId +
        ", isDelete=" + isDelete +
        "}";
    }
}
