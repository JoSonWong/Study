package com.jwong.education.dto.entity;

import com.chad.library.adapter.base.entity.node.BaseNode;

import java.util.List;

public class CostDetailNode extends BaseNode {

    private String curriculumName;
    private float count;
    private double price;
    private double discountPrice;

    public CostDetailNode(String curriculumName, float count, double price, double discountPrice) {
        this.curriculumName = curriculumName;
        this.count = count;
        this.price = price;
        this.discountPrice = discountPrice;
    }

    public String getCurriculumName() {
        return curriculumName;
    }

    public float getCount() {
        return count;
    }

    public double getPrice() {
        return price;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}
