package com.jwong.education.dto.entity;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;

import java.util.List;


public class CostNode extends BaseExpandNode {

    private List<BaseNode> childNode;
    private Long costId;
    private Long studentId;
    private Integer year;
    private Integer month;
    private Integer costType;
    private String costName;
    private Double price;
    private Double discountPrice;

    public CostNode(List<BaseNode> childNode, Long costId, Long studentId, Integer year, Integer month,
                    Integer costType, String costName, Double price, Double discountPrice) {
        this.costId = costId;
        this.studentId = studentId;
        this.year = year;
        this.month = month;
        this.childNode = childNode;
        this.costType = costType;
        this.costName = costName;
        this.price = price;
        this.discountPrice = discountPrice;
        setExpanded(true);
    }

    public Long getCostId() {
        return costId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getCostType() {
        return costType;
    }

    public String getCostName() {
        return costName;
    }

    public double getPrice() {
        return price;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }


    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return childNode;
    }
}
