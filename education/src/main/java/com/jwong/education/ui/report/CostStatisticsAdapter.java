package com.jwong.education.ui.report;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dao.StudentMonthCost;
import com.jwong.education.util.FormatUtils;

import java.util.List;

public class CostStatisticsAdapter extends BaseQuickAdapter<StudentMonthCost, BaseViewHolder> {


    public CostStatisticsAdapter(List<StudentMonthCost> data) {
        super(R.layout.list_item_cost_statistics, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StudentMonthCost item) {
        helper.setText(R.id.tv_date, item.getYear() + "-" + item.getMonth());
        helper.setText(R.id.tv_price, FormatUtils.priceFormat(item.getPrice()));
        helper.setText(R.id.tv_discount_price, FormatUtils.priceFormat(item.getDiscountPrice()));
    }
}
