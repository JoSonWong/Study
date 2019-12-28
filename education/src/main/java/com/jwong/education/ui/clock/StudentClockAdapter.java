package com.jwong.education.ui.clock;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dao.Student;

import java.util.List;

public class StudentClockAdapter extends BaseQuickAdapter<Student, BaseViewHolder> {


    public StudentClockAdapter(List<Student> data) {
        super(R.layout.list_item_student_clock, data);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected void convert(BaseViewHolder helper, Student item) {
        if (item.getId() > 0) {
            helper.setGone(R.id.iv_add, true);
            helper.setGone(R.id.tv_name, false);
            helper.setGone(R.id.iv_remove, false);
            helper.setText(R.id.tv_name, item.getName());
            helper.findView(R.id.iv_remove).setOnClickListener(view -> {
                getData().remove(item);
                notifyDataSetChanged();
            });
        } else {
            helper.setGone(R.id.iv_add, false);
            helper.setGone(R.id.tv_name, true);
            helper.setGone(R.id.iv_remove, true);
            helper.setText(R.id.tv_name, "");
        }
    }
}
