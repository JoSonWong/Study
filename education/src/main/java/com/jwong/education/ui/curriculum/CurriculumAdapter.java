package com.jwong.education.ui.curriculum;

import android.util.LongSparseArray;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dao.Curriculum;
import com.jwong.education.util.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class CurriculumAdapter extends BaseQuickAdapter<Curriculum, BaseViewHolder> {

    private boolean isShowCheckBox;
    private LongSparseArray<Boolean> select = new LongSparseArray<>();

    public CurriculumAdapter(List<Curriculum> data, boolean isShowCheckBox) {
        super(R.layout.list_item_curriculum, data);
        this.isShowCheckBox = isShowCheckBox;
    }

    public void setCheck(long studentId, boolean isChecked) {
        select.append(studentId, isChecked);
    }

    public void clearChecked() {
        select.clear();
    }

    public void setCheckedList(long[] idList) {
        clearChecked();
        if (idList != null) {
            for (long id : idList) {
                select.put(id, true);
            }
        }
    }

    public boolean isCheck(long studentId) {
        return select.get(studentId, false);
    }

    public List<Curriculum> getCheckedList() {
        List<Curriculum> curriculumList = getData();
        List<Curriculum> checkedList = new ArrayList<>();
        for (Curriculum curriculum : curriculumList) {
            if (isCheck(curriculum.getId())) {
                checkedList.add(curriculum);
            }
        }
        return checkedList;
    }

    @Override
    protected void convert(BaseViewHolder helper, Curriculum item) {
        helper.setText(R.id.tv_number, (helper.getLayoutPosition() + 1) + "");
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_price, FormatUtils.priceFormat(item.getPrice()));
        helper.setGone(R.id.iv_select, !isShowCheckBox);
        helper.setImageResource(R.id.iv_select, isCheck(item.getId()) ?
                R.drawable.ic_checked_24dp : R.drawable.ic_uncheck_24dp);
    }
}
