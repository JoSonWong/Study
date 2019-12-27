package com.jwong.education.dto.entity;

import com.chad.library.adapter.base.entity.node.BaseNode;

import java.util.List;

public class StudentDetailNode extends BaseNode {

    private Long id;//学生Id

    private String name;//姓名

    private String avatar;//头像

    private Integer sex;//性别，0未知，1男，2女

    private String currentGrade;//当前年级


    public StudentDetailNode(Long id, String name, String avatar, Integer sex, String currentGrade) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.sex = sex;
        this.currentGrade = currentGrade;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public Integer getSex() {
        return sex;
    }

    public String getCurrentGrade() {
        return currentGrade;
    }


    @org.jetbrains.annotations.Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}
