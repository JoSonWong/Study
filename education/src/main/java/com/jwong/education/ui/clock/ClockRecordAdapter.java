package com.jwong.education.ui.clock;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.util.DateFormatUtil;

import java.util.List;

public class ClockRecordAdapter extends BaseQuickAdapter<ClockRecord, BaseViewHolder> {


    public ClockRecordAdapter(List<ClockRecord> data) {
        super(R.layout.list_item_clock_record, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ClockRecord item) {
        helper.setText(R.id.tv_name, item.getCurriculumName());
        helper.setText(R.id.tv_date_time, DateFormatUtil.convert2DateTime(item.getClockTime()));

    }
}
