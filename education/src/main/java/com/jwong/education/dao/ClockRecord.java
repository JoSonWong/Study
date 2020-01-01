package com.jwong.education.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ClockRecord {

    @Id(autoincrement = true)
    private Long id;//记录id

    private Date clockTime;//打卡时间

    private Long studentId;//学生id

    private String studentName;//学生姓名

    private Long curriculumId;//课程id

    private String curriculumName;//课程名称

    private Double curriculumPrice;//课程原价

    private Double curriculumDiscountPrice;//课程优惠价

    private Integer clockType;//0正常，1调课，2请假

    private Float unit = 1.0F;//单位；默认1课时


    @Generated(hash = 264603217)
    public ClockRecord(Long id, Date clockTime, Long studentId, String studentName,
                       Long curriculumId, String curriculumName, Double curriculumPrice,
                       Double curriculumDiscountPrice, Integer clockType, Float unit) {
        this.id = id;
        this.clockTime = clockTime;
        this.studentId = studentId;
        this.studentName = studentName;
        this.curriculumId = curriculumId;
        this.curriculumName = curriculumName;
        this.curriculumPrice = curriculumPrice;
        this.curriculumDiscountPrice = curriculumDiscountPrice;
        this.clockType = clockType;
        this.unit = unit;
    }

    @Generated(hash = 675671732)
    public ClockRecord() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getClockTime() {
        return this.clockTime;
    }

    public void setClockTime(Date clockTime) {
        this.clockTime = clockTime;
    }

    public Long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getCurriculumId() {
        return this.curriculumId;
    }

    public void setCurriculumId(Long curriculumId) {
        this.curriculumId = curriculumId;
    }

    public String getCurriculumName() {
        return this.curriculumName;
    }

    public void setCurriculumName(String curriculumName) {
        this.curriculumName = curriculumName;
    }

    public Double getCurriculumPrice() {
        return this.curriculumPrice;
    }

    public void setCurriculumPrice(Double curriculumPrice) {
        this.curriculumPrice = curriculumPrice;
    }

    public Double getCurriculumDiscountPrice() {
        return this.curriculumDiscountPrice;
    }

    public void setCurriculumDiscountPrice(Double curriculumDiscountPrice) {
        this.curriculumDiscountPrice = curriculumDiscountPrice;
    }

    public Integer getClockType() {
        return this.clockType;
    }

    public void setClockType(Integer clockType) {
        this.clockType = clockType;
    }

    public Float getUnit() {
        return this.unit;
    }

    public void setUnit(Float unit) {
        this.unit = unit;
    }


}
