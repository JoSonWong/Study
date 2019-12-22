package com.jwong.education.ui.clock;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dao.ClockRecord;

import java.util.List;

public class ClockRecordDetailAdapter extends BaseQuickAdapter<ClockRecord, BaseViewHolder> {

    public ClockRecordDetailAdapter(List<ClockRecord> data) {
        super(R.layout.list_item_clock_record_detail, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ClockRecord item) {
        helper.setText(R.id.tv_number, (helper.getAdapterPosition() + 1) + "");
        helper.setText(R.id.tv_name, item.getStudentName());
        helper.setText(R.id.tv_discount_price, item.getCurriculumDiscountPrice() + "");
        helper.setText(R.id.tv_clock_type, helper.itemView.getContext().getResources()
                .getStringArray(R.array.clock_types)[item.getClockType()]);
        int color = android.R.color.holo_green_dark;
        if (item.getClockType() == 1) {
            color = android.R.color.holo_orange_dark;
        } else if (item.getClockType() == 2) {
            color = android.R.color.holo_red_dark;
        }
        helper.setTextColor(R.id.tv_clock_type, helper.itemView.getContext().getResources().getColor(color));
    }
}
