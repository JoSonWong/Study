package com.jwong.education.ui.student;


import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.jwong.education.dto.entity.StudentDetailNode;
import com.jwong.education.dto.entity.StudentNode;
import com.jwong.education.ui.student.provider.StudentDetailProvider;
import com.jwong.education.ui.student.provider.StudentProvider;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

public class StudentTreeAdapter extends BaseNodeAdapter {

    public StudentTreeAdapter() {
        super();
        addNodeProvider(new StudentProvider());
        addNodeProvider(new StudentDetailProvider());
    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> data, int position) {
        BaseNode node = data.get(position);
        if (node instanceof StudentNode) {
            return 1;
        } else if (node instanceof StudentDetailNode) {
            return 2;
        }
        return -1;
    }
}
