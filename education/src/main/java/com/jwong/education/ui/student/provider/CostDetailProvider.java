package com.jwong.education.ui.student.provider;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dto.entity.CostDetailNode;
import com.jwong.education.util.FormatUtils;

import org.jetbrains.annotations.NotNull;


public class CostDetailProvider extends BaseNodeProvider {

    @Override
    public int getItemViewType() {
        return 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_item_cost_detail;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable BaseNode data) {
        if (data instanceof CostDetailNode) {
            CostDetailNode item = (CostDetailNode) data;
            helper.setText(R.id.tv_number, (helper.getAdapterPosition() + 1) + "");
            helper.setVisible(R.id.tv_number, false);
            helper.setText(R.id.tv_name, item.getCurriculumName());
            helper.setText(R.id.tv_price, FormatUtils.priceFormat(item.getPrice()));
            helper.setText(R.id.tv_discount_price, FormatUtils.priceFormat(item.getDiscountPrice()));
            helper.setText(R.id.tv_count, item.getCount() + "");
            helper.setText(R.id.tv_total, FormatUtils.priceFormat((item.getCount() * item.getDiscountPrice())));
        }
    }
}
