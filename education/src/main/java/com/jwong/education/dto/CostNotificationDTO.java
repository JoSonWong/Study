package com.jwong.education.dto;

import java.io.Serializable;
import java.util.List;

public class CostNotificationDTO implements Serializable {

    private Long studentId;//学生id

    private String studentName;//学生姓名

    private String grade;//年级

    private String costName;//课时

    private List<CostNotificationItemDTO> costList;

    private boolean hasCurriculumCost;


    public CostNotificationDTO(Long studentId, String studentName, String grade, String costName, List<CostNotificationItemDTO> costList) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.grade = grade;
        this.costName = costName;
        this.costList = costList;
    }

    public CostNotificationDTO() {

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

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCostName() {
        return this.costName;
    }

    public void setCostName(String costName) {
        this.costName = costName;
    }

    public List<CostNotificationItemDTO> getCostList() {
        return this.costList;
    }

    public void setCostList(List<CostNotificationItemDTO> costList) {
        this.costList = costList;
    }

    public boolean isHasCurriculumCost() {
        return hasCurriculumCost;
    }

    public void setHasCurriculumCost(boolean hasCurriculumCost) {
        this.hasCurriculumCost = hasCurriculumCost;
    }

}
