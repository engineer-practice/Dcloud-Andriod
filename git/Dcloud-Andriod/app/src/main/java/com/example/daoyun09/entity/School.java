package com.example.daoyun09.entity;

public class School {

private static final long serialVersionUID=1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 学院或学校名称
     */
    private String name;

    /**
     * 表示学院属于哪个学校。如果为0表示是学校
     */
    private String parentId;

    private Integer isDelete;

    private String code;


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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "School{" +
        "id=" + id +
        ", name=" + name +
        ", parentId=" + parentId +
        ", isDelete=" + isDelete +
        ", code=" + code +
        "}";
    }
}