package com.jwong.education.ui.student;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jwong.education.R;
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.dao.StudentCurriculum;
import com.jwong.education.ui.clock.ClockRecordAdapter;
import com.jwong.education.ui.clock.ClockViewModel;
import com.jwong.education.util.FormatUtils;

public class StudentClockFragment extends Fragment implements View.OnClickListener,
        BaseQuickAdapter.OnItemClickListener {

    private ClockViewModel clockViewModel;
    private RecyclerView rvClockHistory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        setMenuVisibility(false);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        clockViewModel = ViewModelProviders.of(this).get(ClockViewModel.class);
        View root = inflater.inflate(R.layout.fragment_student_clock, container, false);
        rvClockHistory = root.findViewById(R.id.rv_clock_record);
        rvClockHistory.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        rvClockHistory.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        clockViewModel.getStudentClockRecordList(StudentActivity.studentId).observe(this, clockRecords -> {
            if (clockRecords != null) {
                ClockRecordAdapter adapter = new ClockRecordAdapter(clockRecords);
                adapter.setOnItemClickListener(this);
                rvClockHistory.setAdapter(adapter);
            }
        });
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            default:
                break;
        }
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ClockRecord clockRecord = (ClockRecord) adapter.getData().get(position);
        View viewInput = LayoutInflater.from(getContext()).inflate(R.layout.dlg_clock_record_detail, null);
        TextView tvStudentId = viewInput.findViewById(R.id.tv_student_id);
        tvStudentId.setText(getString(R.string.student_code_x,
                FormatUtils.studentCodeFormat(clockRecord.getStudentId())));
        TextView tvStudentName = viewInput.findViewById(R.id.tv_student_name);
        tvStudentName.setText(getString(R.string.student_name_x, clockRecord.getStudentName()));
        TextView tvCurriculumId = viewInput.findViewById(R.id.tv_curriculum_id);
        tvCurriculumId.setText(getString(R.string.curriculum_id_x, clockRecord.getCurriculumId() + ""));
        TextView tvCurriculumName = viewInput.findViewById(R.id.tv_curriculum_name);
        tvCurriculumName.setText(getString(R.string.curriculum_name_x, clockRecord.getCurriculumName()));
        TextView tvCurriculumPrice = viewInput.findViewById(R.id.tv_curriculum_price);
        tvCurriculumPrice.setText(getString(R.string.curriculum_price_x, clockRecord.getCurriculumPrice() + ""));
        EditText etDiscountPrice = viewInput.findViewById(R.id.et_discount_price);
        etDiscountPrice.setText(clockRecord.getCurriculumDiscountPrice() + "");
        RadioGroup radioGroup = viewInput.findViewById(R.id.rg_clock_type);
        radioGroup.check(clockRecord.getClockType() == 1 ? R.id.rb_adjustment
                : (clockRecord.getClockType() == 2 ? R.id.rb_leave : R.id.rb_normal));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(R.string.student_clock_detail).setView(viewInput)
                .setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton(R.string.delete_record, (dialogInterface, i) -> {
                    clockViewModel.delete(clockRecord.getId());
                    clockViewModel.getStudentClockRecordList(StudentActivity.studentId);
                })
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    if (!TextUtils.isEmpty(etDiscountPrice.getText())) {
                        int id = radioGroup.getCheckedRadioButtonId();
                        int clockType = 0;
                        if (id == R.id.rb_adjustment) {
                            clockType = 1;
                        } else if (id == R.id.rb_leave) {
                            clockType = 2;
                        }
                        clockRecord.setClockType(clockType);
                        clockRecord.setCurriculumDiscountPrice(Double.parseDouble(etDiscountPrice.getText().toString()));
                        long logId = clockViewModel.update(clockRecord);
                        Toast.makeText(getContext(), logId > 0 ? R.string.update_success : R.string.update_fail
                                , Toast.LENGTH_SHORT).show();
                        if (logId > 0) {
                            clockViewModel.getStudentClockRecordList(StudentActivity.studentId);
                        }
                    } else {
                        Toast.makeText(getContext(), R.string.pls_input_discount_price, Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.pictrue_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}