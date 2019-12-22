package com.jwong.education.ui.student;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dao.StudentCurriculum;
import com.jwong.education.util.FormatUtils;

import java.util.List;

public class StudentCurriculumAdapter extends BaseQuickAdapter<StudentCurriculum, BaseViewHolder> {


    public StudentCurriculumAdapter(List<StudentCurriculum> data) {
        super(R.layout.list_item_student_curriculum, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, StudentCurriculum item) {
        helper.setText(R.id.tv_number, (helper.getLayoutPosition() + 1) + "");
        helper.setText(R.id.tv_name, item.getCurriculum().getName());
        helper.setText(R.id.tv_price, FormatUtils.priceFormat(item.getCurriculum().getPrice()));
        helper.setText(R.id.tv_discount_price, FormatUtils.priceFormat(item.getDiscountPrice()));
    }
}
