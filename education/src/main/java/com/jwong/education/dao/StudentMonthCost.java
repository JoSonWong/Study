package com.jwong.education.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class StudentMonthCost extends BaseDao {

    @Id(autoincrement = true)
    private Long id;//id

    private Long studentId;//学生id

    private Integer year;//年

    private Integer month;//月

    private Integer costType;//费用类型

    private String costName;//费用名称

    private Double price;//价格

    private Double discountPrice;//折后价

    @Generated(hash = 1512104853)
    public StudentMonthCost(Long id, Long studentId, Integer year, Integer month,
            Integer costType, String costName, Double price, Double discountPrice) {
        this.id = id;
        this.studentId = studentId;
        this.year = year;
        this.month = month;
        this.costType = costType;
        this.costName = costName;
        this.price = price;
        this.discountPrice = discountPrice;
    }

    @Generated(hash = 1902130645)
    public StudentMonthCost() {
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

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return this.month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getCostType() {
        return this.costType;
    }

    public void setCostType(Integer costType) {
        this.costType = costType;
    }

    public String getCostName() {
        return this.costName;
    }

    public void setCostName(String costName) {
        this.costName = costName;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscountPrice() {
        return this.discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

}
