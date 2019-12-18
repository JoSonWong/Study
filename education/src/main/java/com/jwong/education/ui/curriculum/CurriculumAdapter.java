package com.jwong.education.ui.curriculum;

import android.util.LongSparseArray;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jwong.education.R;
import com.jwong.education.dao.Curriculum;

import java.util.ArrayList;
import java.util.List;

public class CurriculumAdapter extends BaseQuickAdapter<Curriculum, BaseViewHolder> {

    private boolean isShowCheckBox;
    private LongSparseArray<Boolean> select = new LongSparseArray<>();

    public CurriculumAdapter(int layoutResId, List<Curriculum> data, boolean isShowCheckBox) {
        super(layoutResId, data);
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
        helper.setText(R.id.tv_number, item.getId() + "");
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_price, item.getPrice() + "");
        helper.setGone(R.id.cb_select, isShowCheckBox);
        helper.setChecked(R.id.cb_select, isCheck(item.getId()));
//        helper.setOnCheckedChangeListener(R.id.cb_select, (compoundButton, isChecked) -> {
//            setCheck(item.getId(), isChecked);
//        });
    }
}
