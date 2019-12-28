package com.jwong.education.ui.clock;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.util.FormatUtils;

import java.util.List;

public class ClockRecordDetailAdapter extends BaseQuickAdapter<ClockRecord, BaseViewHolder> {

    public ClockRecordDetailAdapter(List<ClockRecord> data) {
        super(R.layout.list_item_clock_record_detail, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ClockRecord item) {
        helper.setText(R.id.tv_number, (helper.getLayoutPosition() + 1) + "");
        helper.setText(R.id.tv_name, item.getStudentName());
        helper.setText(R.id.tv_discount_price, FormatUtils.priceFormat(item.getCurriculumDiscountPrice()));
        helper.setText(R.id.tv_count, FormatUtils.priceFormat(item.getUnit()));
        helper.setText(R.id.tv_total, FormatUtils.priceFormat(item.getUnit()
                * item.getCurriculumDiscountPrice()));
        helper.setText(R.id.tv_clock_type, helper.itemView.getContext().getResources()
                .getStringArray(R.array.clock_types)[item.getClockType()]);
        int color = R.color.colorPrimary;
        if (item.getClockType() == 1) {
            color = android.R.color.holo_orange_dark;
        } else if (item.getClockType() == 2) {
            color = android.R.color.holo_red_dark;
        }
        helper.setTextColor(R.id.tv_clock_type, helper.itemView.getContext().getResources().getColor(color));
    }
}
