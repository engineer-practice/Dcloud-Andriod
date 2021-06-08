package com.example.daoyun09.entity;

import java.time.LocalDateTime;

public class CourseManage{

private static final long serialVersionUID=1L;

    private Long id;

    private String name;

    private Integer type;

    private String description;

    private String createdName;

    private LocalDateTime createdTime;

    private Integer isDeleted;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "CourseManage{" +
        "id=" + id +
        ", name=" + name +
        ", type=" + type +
        ", description=" + description +
        ", createdName=" + createdName +
        ", createdTime=" + createdTime +
        ", isDeleted=" + isDeleted +
        "}";
    }
}
