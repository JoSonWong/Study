package com.jwong.education.ui.clock;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.util.FormatUtils;

import java.util.List;

public class ClockRecordAdapter extends BaseQuickAdapter<ClockRecord, BaseViewHolder> {

    private boolean isShowClockType;

    public ClockRecordAdapter(List<ClockRecord> data, boolean isShowClockType) {
        super(R.layout.list_item_clock_record, data);
        this.isShowClockType = isShowClockType;
    }


    @Override
    protected void convert(BaseViewHolder helper, ClockRecord item) {
        helper.setText(R.id.tv_name, item.getCurriculumName());
        helper.setText(R.id.tv_clock_type, helper.itemView.getResources()
                .getStringArray(R.array.clock_types)[item.getClockType()]);
        helper.setGone(R.id.tv_clock_type, !this.isShowClockType);
        helper.setText(R.id.tv_date_time, FormatUtils.convert2DateTime(item.getClockTime()));
    }
}
