package com.jwong.education.ui.setting;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dao.Curriculum;

import java.util.List;

public class CurriculumAdapter extends BaseQuickAdapter<Curriculum, BaseViewHolder> {

    public CurriculumAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Curriculum item) {
        helper.setText(R.id.tv_number, item.getId() + "");
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_price, item.getPrice() + "");
    }
}
