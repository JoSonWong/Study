package com.jwong.education.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Calendar;
import java.util.Date;

@Entity
public class Student extends BaseDao {

    @Id(autoincrement = true)
    private Long id;//学生Id

    private String name;//姓名

    private String avatar;//头像

    private Integer sex = 0;//性别0男，1女

    private Date birthday;//生日

    private Date recruitTime = Calendar.getInstance().getTime();//招收时间

    private Integer recruitGradeCode = 0;//招收年级代号

    private String recruitGradeName = "一年级";//招收年级

    private Integer currentGradeCode = 0;//当前年级代号

    private String currentGrade = "一年级";//当前年级

    private Integer studentType = 1;//0试听，1在学，2毕业

    private String studentTypeName = "在学";//类型名称

    private Integer costType = 0;//收费类型：0按课程,1按学期

    private String costTypeName = "按课程";//收费类型名称：0按课程,1按学期

    private String guardian1;//监护人1姓名

    private String guardian1Phone;//监护人1电话

    private String guardian2;//监护人2姓名

    private String guardian2Phone;//监护人2电话

    @Generated(hash = 1493955956)
    public Student(Long id, String name, String avatar, Integer sex, Date birthday,
                   Date recruitTime, Integer recruitGradeCode, String recruitGradeName,
                   Integer currentGradeCode, String currentGrade, Integer studentType,
                   String studentTypeName, Integer costType, String costTypeName,
                   String guardian1, String guardian1Phone, String guardian2,
                   String guardian2Phone) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.sex = sex;
        this.birthday = birthday;
        this.recruitTime = recruitTime;
        this.recruitGradeCode = recruitGradeCode;
        this.recruitGradeName = recruitGradeName;
        this.currentGradeCode = currentGradeCode;
        this.currentGrade = currentGrade;
        this.studentType = studentType;
        this.studentTypeName = studentTypeName;
        this.costType = costType;
        this.costTypeName = costTypeName;
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

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public Integer getRecruitGradeCode() {
        return this.recruitGradeCode;
    }

    public void setRecruitGradeCode(Integer recruitGradeCode) {
        this.recruitGradeCode = recruitGradeCode;
    }

    public String getRecruitGradeName() {
        return this.recruitGradeName;
    }

    public void setRecruitGradeName(String recruitGradeName) {
        this.recruitGradeName = recruitGradeName;
    }

    public Integer getCurrentGradeCode() {
        return this.currentGradeCode;
    }

    public void setCurrentGradeCode(Integer currentGradeCode) {
        this.currentGradeCode = currentGradeCode;
    }

    public String getCurrentGrade() {
        return this.currentGrade;
    }

    public void setCurrentGrade(String currentGrade) {
        this.currentGrade = currentGrade;
    }

    public Integer getStudentType() {
        return this.studentType;
    }

    public void setStudentType(Integer studentType) {
        this.studentType = studentType;
    }

    public String getStudentTypeName() {
        return this.studentTypeName;
    }

    public void setStudentTypeName(String studentTypeName) {
        this.studentTypeName = studentTypeName;
    }

    public Integer getCostType() {
        return this.costType;
    }

    public void setCostType(Integer costType) {
        this.costType = costType;
    }

    public String getCostTypeName() {
        return this.costTypeName;
    }

    public void setCostTypeName(String costTypeName) {
        this.costTypeName = costTypeName;
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
