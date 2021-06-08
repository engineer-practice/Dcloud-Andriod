package com.example.daoyun09.entity;

public class Dictionary {

private static final long serialVersionUID=1L;

    private Long id;

    /**
     * 英文标识
     */
    private String code;

    /**
     * 中文标识
     */
    private String name;
    private Integer isDelete;

    /**
     * 说明
     */
    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Dictionary{" +
        "id=" + id +
        ", code=" + code +
        ", name=" + name +
        ", isDelete=" + isDelete +
        ", description=" + description +
        "}";
    }
}
