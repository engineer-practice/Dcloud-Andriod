package com.example.daoyun09.entity;

public class SystemManage {

private static final long serialVersionUID=1L;

    private Long id;

    /**
     * 签到经验值
     */
    private Integer attendExp;

    /**
     * 活动经验值
     */
    private Integer activityExp;

    /**
     * 出勤等级
     */
    private Integer attendLevel;

    /**
     * 出勤率
     */
    private Integer attendRatio;

    /**
     * 签到距离
     */
    private Integer attendDistance;

    private Integer isDelete;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAttendExp() {
        return attendExp;
    }

    public void setAttendExp(Integer attendExp) {
        this.attendExp = attendExp;
    }

    public Integer getActivityExp() {
        return activityExp;
    }

    public void setActivityExp(Integer activityExp) {
        this.activityExp = activityExp;
    }

    public Integer getAttendLevel() {
        return attendLevel;
    }

    public void setAttendLevel(Integer attendLevel) {
        this.attendLevel = attendLevel;
    }

    public Integer getAttendRatio() {
        return attendRatio;
    }

    public void setAttendRatio(Integer attendRatio) {
        this.attendRatio = attendRatio;
    }

    public Integer getAttendDistance() {
        return attendDistance;
    }

    public void setAttendDistance(Integer attendDistance) {
        this.attendDistance = attendDistance;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "SystemManage{" +
        "id=" + id +
        ", attendExp=" + attendExp +
        ", activityExp=" + activityExp +
        ", attendLevel=" + attendLevel +
        ", attendRatio=" + attendRatio +
        ", attendDistance=" + attendDistance +
        ", isDelete=" + isDelete +
        "}";
    }
}
