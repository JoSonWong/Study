package com.jwong.education.ui.student.provider;

import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dto.entity.StudentNode;

import org.jetbrains.annotations.NotNull;


public class StudentProvider extends BaseNodeProvider {

    @Override
    public int getItemViewType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_item_student_group;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable BaseNode data) {
        if (data instanceof StudentNode) {
            StudentNode item = (StudentNode) data;
            helper.setText(android.R.id.title, helper.itemView.getContext().getString(R.string.group_x_people,
                    item.getTypeName(), item.getChildNode() == null ? 0 : item.getChildNode().size()));
        }
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        StudentNode node = (StudentNode) data;
        if (node.isExpanded()) {
            if (getAdapter() != null)
                getAdapter().collapse(position);
        } else {
            if (getAdapter() != null)
                getAdapter().expand(position);
        }
    }
}
