package com.jwong.education.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class StudentCurriculum extends BaseDao {

    @Id(autoincrement = true)
    private Long id;//课程id，自增

    private Long studentId;//学生id

    private Long curriculumId;//课程id

    private Double discountPrice;//折后价

    @Generated(hash = 1554683346)
    public StudentCurriculum(Long id, Long studentId, Long curriculumId,
            Double discountPrice) {
        this.id = id;
        this.studentId = studentId;
        this.curriculumId = curriculumId;
        this.discountPrice = discountPrice;
    }

    @Generated(hash = 1025845966)
    public StudentCurriculum() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCurriculumId() {
        return this.curriculumId;
    }

    public void setCurriculumId(Long curriculumId) {
        this.curriculumId = curriculumId;
    }

    public Double getDiscountPrice() {
        return this.discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }
}
