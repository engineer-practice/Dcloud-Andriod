package com.example.daoyun09.entity;

public class AttendenceResult{

private static final long serialVersionUID=1L;

    private Integer id;

    private String studentEmail;

    private String attendTime;

    private Integer isDelete;

    private Integer attendId;

    private String code;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getAttendTime() {
        return attendTime;
    }

    public void setAttendTime(String attendTime) {
        this.attendTime = attendTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getAttendId() {
        return attendId;
    }

    public void setAttendId(Integer attendId) {
        this.attendId = attendId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "AttendenceResult{" +
        "id=" + id +
        ", studentEmail=" + studentEmail +
        ", attendTime=" + attendTime +
        ", isDelete=" + isDelete +
        ", attendId=" + attendId +
        ", code=" + code +
        "}";
    }
}
