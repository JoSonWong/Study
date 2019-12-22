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
    protected void convert(BaseViewHolder helper, Student item) {
        helper.setText(R.id.tv_name, item.getName());
    }
}
