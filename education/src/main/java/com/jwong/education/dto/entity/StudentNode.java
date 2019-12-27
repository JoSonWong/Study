package com.jwong.education.dto.entity;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;

import java.util.List;


public class StudentNode extends BaseExpandNode {

    private List<BaseNode> childNode;
    private Integer type;
    private String typeName;

    public StudentNode(List<BaseNode> childNode, Integer type, String typeName) {
        this.childNode = childNode;
        this.type = type;
        this.typeName = typeName;
        setExpanded(true);
    }

    public Integer getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }


    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return childNode;
    }
}
