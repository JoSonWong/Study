package com.jwong.education.ui.student;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dao.Student;

import java.util.List;

public class StudentAdapter extends BaseQuickAdapter<Student, BaseViewHolder> {

    public StudentAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Student item) {
        helper.setText(R.id.tv_number, item.getId() + "");
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_current_grade, item.getCurrentGrade() + "");
    }
}
