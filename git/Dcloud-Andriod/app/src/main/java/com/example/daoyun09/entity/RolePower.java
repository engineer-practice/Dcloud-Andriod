package com.example.daoyun09.entity;

public class RolePower {

private static final long serialVersionUID=1L;

    private Long id;

    private Long roleId;

    private Long powerId;

    private Integer isDelete;

    public RolePower(){
        this.isDelete = 0;
        this.powerId = Long.valueOf(0);
        this.roleId = Long.valueOf(0);
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

    public Long getPowerId() {
        return powerId;
    }

    public void setPowerId(Long powerId) {
        this.powerId = powerId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "RolePower{" +
        "id=" + id +
        ", roleId=" + roleId +
        ", powerId=" + powerId +
        ", isDelete=" + isDelete +
        "}";
    }
}
