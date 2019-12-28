package com.jwong.education.dto;

import java.io.Serializable;

public class CostNotificationItemDTO implements Serializable {


    private String costName;//费用名称

    private String costPrice;//费用标准

    private String discountCost;//优惠价

    private float curriculumCount;//课时

    private double total;//合计


    public CostNotificationItemDTO(String costName, String costPrice, String discountCost,
                                   float curriculumCount, double total) {
        this.costName = costName;
        this.costPrice = costPrice;
        this.discountCost = discountCost;
        this.curriculumCount = curriculumCount;
        this.total = total;
    }

    public CostNotificationItemDTO() {

    }

    public String getCostName() {
        return this.costName;
    }

    public void setCostName(String costName) {
        this.costName = costName;
    }

    public String getCostPrice() {
        return this.costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getDiscountCost() {
        return this.discountCost;
    }

    public void setDiscountCost(String discountCost) {
        this.discountCost = discountCost;
    }

    public float getCurriculumCount() {
        return this.curriculumCount;
    }

    public void setCurriculumCount(int curriculumCount) {
        this.curriculumCount = curriculumCount;
    }

    public double getTotal() {
        return this.total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

}
