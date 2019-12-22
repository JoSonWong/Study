package com.jwong.education.ui.student;


import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.jwong.education.dto.entity.CostDetailNode;
import com.jwong.education.dto.entity.CostNode;
import com.jwong.education.ui.student.provider.CostDetailProvider;
import com.jwong.education.ui.student.provider.CostProvider;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

public class CostTreeAdapter extends BaseNodeAdapter {

    public CostTreeAdapter() {
        super();
        addNodeProvider(new CostProvider());
        addNodeProvider(new CostDetailProvider());
    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> data, int position) {
        BaseNode node = data.get(position);
        if (node instanceof CostNode) {
            return 1;
        } else if (node instanceof CostDetailNode) {
            return 2;
        }
        return -1;
    }
}
