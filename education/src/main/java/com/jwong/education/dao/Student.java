package com.jwong.education.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Student extends BaseDao{

    @Id(autoincrement = true)
    private Long id;//学生Id

    private String name;//姓名

    private Integer sex;//性别，0未知，1男，2女

    private Date birthday;//生日

    private Date recruitTime;//招收时间

    private Date recruitGrade;//招收年级

    private Date currentGrade;//当前年级

    private Integer studentType;//0试听，1在学，2毕业

    private String guardian1;//监护人1姓名

    private String guardian1Phone;//监护人1电话

    private String guardian2;//监护人2姓名

    private String guardian2Phone;//监护人2电话

    @Generated(hash = 1373249178)
    public Student(Long id, String name, Integer sex, Date birthday,
            Date recruitTime, Date recruitGrade, Date currentGrade,
            Integer studentType, String guardian1, String guardian1Phone,
            String guardian2, String guardian2Phone) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.recruitTime = recruitTime;
        this.recruitGrade = recruitGrade;
        this.currentGrade = currentGrade;
        this.studentType = studentType;
        this.guardian1 = guardian1;
        this.guardian1Phone = guardian1Phone;
        this.guardian2 = guardian2;
        this.guardian2Phone = guardian2Phone;
    }

    @Generated(hash = 1556870573)
    public Student() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return this.sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getRecruitTime() {
        return this.recruitTime;
    }

    public void setRecruitTime(Date recruitTime) {
        this.recruitTime = recruitTime;
    }

    public Date getRecruitGrade() {
        return this.recruitGrade;
    }

    public void setRecruitGrade(Date recruitGrade) {
        this.recruitGrade = recruitGrade;
    }

    public Date getCurrentGrade() {
        return this.currentGrade;
    }

    public void setCurrentGrade(Date currentGrade) {
        this.currentGrade = currentGrade;
    }

    public Integer getStudentType() {
        return this.studentType;
    }

    public void setStudentType(Integer studentType) {
        this.studentType = studentType;
    }

    public String getGuardian1() {
        return this.guardian1;
    }

    public void setGuardian1(String guardian1) {
        this.guardian1 = guardian1;
    }

    public String getGuardian1Phone() {
        return this.guardian1Phone;
    }

    public void setGuardian1Phone(String guardian1Phone) {
        this.guardian1Phone = guardian1Phone;
    }

    public String getGuardian2() {
        return this.guardian2;
    }

    public void setGuardian2(String guardian2) {
        this.guardian2 = guardian2;
    }

    public String getGuardian2Phone() {
        return this.guardian2Phone;
    }

    public void setGuardian2Phone(String guardian2Phone) {
        this.guardian2Phone = guardian2Phone;
    }

}
