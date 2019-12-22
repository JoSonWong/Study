package com.jwong.education.ui.student;

import android.util.LongSparseArray;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dao.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends BaseQuickAdapter<Student, BaseViewHolder> {

    private boolean isShowCheckBox;
    private LongSparseArray<Boolean> select = new LongSparseArray<>();

    public StudentAdapter(List<Student> data, boolean isShowCheckBox) {
        super(R.layout.list_item_student, data);
        this.isShowCheckBox = isShowCheckBox;
    }

    public void clearChecked() {
        select.clear();
    }

    public void setCheckedList(long[] idList) {
        clearChecked();
        if (idList != null) {
            for (long id : idList) {
                select.put(id, true);
            }
        }
    }

    public void setCheck(long studentId, boolean isChecked) {
        select.append(studentId, isChecked);
    }

    public boolean isCheck(long studentId) {
        return select.get(studentId, false);
    }

    public List<Student> getCheckedList() {
        List<Student> students = getData();
        List<Student> checkedList = new ArrayList<>();
        for (Student student : students) {
            if (isCheck(student.getId())) {
                checkedList.add(student);
            }
        }
        return checkedList;
    }

    @Override
    protected void convert(BaseViewHolder helper, Student item) {
        helper.setText(R.id.tv_number, item.getId() + "");
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_current_grade, item.getCurrentGrade() + "");
        helper.setImageResource(R.id.iv_select, isCheck(item.getId()) ?
                R.drawable.ic_checked_24dp : R.drawable.ic_uncheck_24dp);
        helper.setGone(R.id.iv_select, !isShowCheckBox);
    }
}
