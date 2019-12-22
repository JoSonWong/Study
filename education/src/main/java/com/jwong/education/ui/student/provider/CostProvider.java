package com.jwong.education.ui.student.provider;

import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dto.entity.CostNode;
import com.jwong.education.util.FormatUtils;

import org.jetbrains.annotations.NotNull;


public class CostProvider extends BaseNodeProvider {

    @Override
    public int getItemViewType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_item_cost;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable BaseNode data) {
        if (data instanceof CostNode) {
            CostNode item = (CostNode) data;
            helper.setText(R.id.tv_number, (helper.getLayoutPosition() + 1) + "");
            helper.setText(R.id.tv_name, item.getCostName());
            helper.setText(R.id.tv_price, FormatUtils.priceFormat(item.getPrice()));
            helper.setText(R.id.tv_discount_price, helper.itemView.getContext()
                    .getString(R.string.rmb_x, FormatUtils.priceFormat(item.getDiscountPrice())));
            helper.setGone(R.id.tv_date, true);
//        if (entity.isExpanded()) {
//            helper.setImageResource(R.id.iv, R.mipmap.arrow_b);
//        } else {
//            helper.setImageResource(R.id.iv, R.mipmap.arrow_r);
//        }
        }
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        CostNode node = (CostNode) data;
        if (node.isExpanded()) {
            if (getAdapter() != null)
                getAdapter().collapse(position);
        } else {
            if (getAdapter() != null)
                getAdapter().expand(position);
        }
    }
}
