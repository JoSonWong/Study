package com.jwong.education.ui.student.provider;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dto.entity.CostDetailNode;
import com.jwong.education.dto.entity.StudentDetailNode;
import com.jwong.education.util.FormatUtils;

import org.jetbrains.annotations.NotNull;


public class StudentDetailProvider extends BaseNodeProvider {

    @Override
    public int getItemViewType() {
        return 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_item_student;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable BaseNode data) {
        if (data instanceof StudentDetailNode) {
            StudentDetailNode item = (StudentDetailNode) data;
            helper.setText(R.id.tv_number, (helper.getLayoutPosition() + 1) + "");
            helper.setGone(R.id.tv_number, true);
            helper.setText(R.id.tv_name, item.getName());
            helper.setText(R.id.tv_student_code, FormatUtils.studentCodeFormat(item.getId()));
            helper.setText(R.id.tv_current_grade, item.getCurrentGrade() + "");
            helper.setGone(R.id.iv_select, true);
        }
    }
}
