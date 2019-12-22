package com.jwong.education.ui.report;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dao.StudentMonthCost;
import com.jwong.education.util.FormatUtils;

import java.util.List;

public class CostAdapter extends BaseQuickAdapter<StudentMonthCost, BaseViewHolder> {

    private boolean isShowDate;

    public CostAdapter(List<StudentMonthCost> data, boolean isShowDate) {
        super(R.layout.list_item_cost, data);
        this.isShowDate = isShowDate;
    }

    @Override
    protected void convert(BaseViewHolder helper, StudentMonthCost item) {
        helper.setText(R.id.tv_number, (helper.getAdapterPosition() + 1) + "");
        helper.setText(R.id.tv_name, item.getCostName());
        helper.setText(R.id.tv_price, FormatUtils.priceFormat(item.getPrice()));
        helper.setText(R.id.tv_discount_price, FormatUtils.priceFormat(item.getDiscountPrice()));
        helper.setText(R.id.tv_date, "");
        helper.setGone(R.id.tv_date, !isShowDate);
    }
}
