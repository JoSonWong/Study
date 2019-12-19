package com.jwong.education.ui.report;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dao.StudentMonthCost;

import java.util.List;

public class CostAdapter extends BaseQuickAdapter<StudentMonthCost, BaseViewHolder> {


    public CostAdapter(List<StudentMonthCost> data) {
        super(R.layout.list_item_cost, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StudentMonthCost item) {
        helper.setText(R.id.tv_number, (helper.getAdapterPosition() + 1) + "");
        helper.setText(R.id.tv_name, item.getCostName());
        helper.setText(R.id.tv_price, item.getPrice() + "");
        helper.setText(R.id.tv_discount_price, item.getDiscountPrice() + "");
        helper.setText(R.id.tv_date, item.getYear() + "-" + item.getMonth());
    }
}
