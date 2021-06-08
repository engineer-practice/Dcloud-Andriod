package com.example.daoyun09.entity;

public class CourseStudent{

private static final long serialVersionUID=1L;
    private Long id;

    private Long courseId;

    private String studentEmail;

    private String teacherEmail;

    private Integer exp;

    private int isDelete;

    public CourseStudent(){
        this.isDelete = 0;
        this.exp = 0;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    @Override
    public String toString() {
        return "CourseStudent{" +
        "id=" + id +
        ", courseId=" + courseId +
        ", studentEmail=" + studentEmail +
        ", teacherEmail=" + teacherEmail +
        "}";
    }
}
