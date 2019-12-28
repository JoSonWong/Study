package com.jwong.education.ui.student;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dao.StudentCurriculum;
import com.jwong.education.dto.CostNotificationItemDTO;
import com.jwong.education.util.FormatUtils;

import java.util.List;

public class CostNotificationAdapter extends BaseQuickAdapter<CostNotificationItemDTO, BaseViewHolder> {


    public CostNotificationAdapter(List<CostNotificationItemDTO> data) {
        super(R.layout.list_item_cost_notification, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, CostNotificationItemDTO item) {
        helper.setText(R.id.tv_number, (helper.getLayoutPosition() + 1) + "");
        helper.setText(R.id.tv_name, item.getCostName());
        helper.setText(R.id.tv_price, item.getCostPrice());
        helper.setText(R.id.tv_count, item.getCurriculumCount() > 0 ? (item.getCurriculumCount() + "") : "");
        helper.setText(R.id.tv_total, FormatUtils.priceFormat(item.getTotal()));
    }
}
