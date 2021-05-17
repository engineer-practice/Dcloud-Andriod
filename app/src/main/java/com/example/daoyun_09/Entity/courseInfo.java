package com.example.daoyun_09.Entity;

public class courseInfo{
    private String courseName;
    private String teacherName;
    private String courseNum;
    private String courseTime;


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }



    public String getTeacherName() {
        return teacherName;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public String getCourseTime() {
        return courseTime;
    }

    @Override
    public String toString() {
        return "courseInfo{" +
                "courseName='" + courseName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", courseNum='" + courseNum + '\'' +
                ", courseTime='" + courseTime + '\'' +
                '}';
    }
}
