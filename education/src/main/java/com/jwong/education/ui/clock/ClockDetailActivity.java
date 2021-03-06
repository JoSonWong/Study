package com.jwong.education.ui.clock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jwong.education.R;
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.dao.Student;
import com.jwong.education.dto.ClockRecordDTO;
import com.jwong.education.dto.StudentDTO;
import com.jwong.education.ui.student.StudentSelectActivity;
import com.jwong.education.util.FormatUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClockDetailActivity extends AppCompatActivity implements View.OnClickListener,
        OnItemClickListener {

    private RecyclerView recyclerView;
    private ClockViewModel clockViewModel;
    private ClockRecordDetailAdapter adapter;
    private ClockRecordDTO clockRecordDTO;
    private TextView tvCurriculum, tvClockTime, tvPrice, tvTotalCount, tvTotalCost;
    private int clockType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_detail);
        clockRecordDTO = (ClockRecordDTO) getIntent().getSerializableExtra("clockRecord");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setTitle(R.string.clock_detail);
        }
        tvCurriculum = findViewById(R.id.tv_curriculum);
        tvPrice = findViewById(R.id.tv_price);
        tvClockTime = findViewById(R.id.tv_clock_time);
        tvClockTime.setOnClickListener(this);
        tvTotalCount = findViewById(R.id.tv_count);
        tvTotalCost = findViewById(R.id.tv_total);
        recyclerView = findViewById(R.id.rv_clock_record);
        findViewById(R.id.btn_patch_card).setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        tvCurriculum.setText(clockRecordDTO.getCurriculumName());
        tvPrice.setText(getString(R.string.rmb_x,
                FormatUtils.priceFormat(clockRecordDTO.getCurriculumPrice())));
        tvClockTime.setText(FormatUtils.convert2DateTime(clockRecordDTO.getClockTime()));
        clockViewModel = ViewModelProviders.of(this).get(ClockViewModel.class);
        clockViewModel.getClockRecordDetailList(clockRecordDTO.getCurriculumId(),
                clockRecordDTO.getClockTime()).observe(this, clockRecords -> {
            adapter = new ClockRecordDetailAdapter(clockRecords);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);

            float totalCount = 0;
            double totalCost = 0.0;
            for (ClockRecord record : clockRecords) {
                if (record.getClockType() == 0) {
                    totalCount = totalCount + record.getUnit();
                    totalCost = totalCost + record.getCurriculumDiscountPrice() * record.getUnit();
                }
            }
            tvTotalCount.setText(getString(R.string.total_curriculum_count_x, FormatUtils.priceFormat(totalCount)));
            tvTotalCost.setText(getString(R.string.total_curriculum_cost_x, FormatUtils.priceFormat(totalCost)));
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_nav_menu_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.delete_record)
                        .setMessage(R.string.delete_all_this_clock_record)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                            clockViewModel.delete(clockRecordDTO.getCurriculumId(), clockRecordDTO.getClockTime());
                            Toast.makeText(this, R.string.delete_record_success, Toast.LENGTH_SHORT).show();
                            finish();
                        });
                builder.create().show();
                break;
            case android.R.id.home:// 点击返回图标事件
                this.finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_patch_card) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.select_clock_type);
            builder.setSingleChoiceItems(R.array.clock_types, 0, (dialog, which) -> {
                startSelectStudentActivity(which);
                dialog.dismiss();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (view.getId() == R.id.tv_clock_time) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(clockRecordDTO.getClockTime());
            DatePickerDialog dpd = new DatePickerDialog(this,
                    DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                    (DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        clockRecordDTO.setClockTime(calendar.getTime());
                        for (ClockRecord record : adapter.getData()) {
                            record.setClockTime(clockRecordDTO.getClockTime());
                            clockViewModel.update(record);
                        }
                        tvClockTime.setText(FormatUtils.convert2DateTime(clockRecordDTO.getClockTime()));
                        Toast.makeText(getApplicationContext(), R.string.update_success, Toast.LENGTH_SHORT).show();
                    }
                    , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        }
    }


    private void startSelectStudentActivity(int clockType) {
        this.clockType = clockType;
        Intent intent = new Intent(this, StudentSelectActivity.class);
        intent.putExtra("is_multiple", true);
        intent.putExtra("curriculumId", clockRecordDTO.getCurriculumId());
        long[] exceptIdList = new long[adapter.getData().size()];
        for (int i = 0; i < adapter.getData().size(); i++) {
            exceptIdList[i] = adapter.getData().get(i).getStudentId();
        }
        intent.putExtra("except_id_list", exceptIdList);
        startActivityForResult(intent, 1200);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ClockRecord clockRecord = (ClockRecord) adapter.getData().get(position);
        View viewInput = LayoutInflater.from(this).inflate(R.layout.dlg_clock_record_detail, null);
        TextView tvStudentId = viewInput.findViewById(R.id.tv_student_id);
        tvStudentId.setText(getString(R.string.student_code_x, FormatUtils.studentCodeFormat(clockRecord.getStudentId())));
        TextView tvStudentName = viewInput.findViewById(R.id.tv_student_name);
        tvStudentName.setText(getString(R.string.student_name_x, clockRecord.getStudentName()));

        TextView tvCurriculumId = viewInput.findViewById(R.id.tv_curriculum_id);
        tvCurriculumId.setText(getString(R.string.curriculum_id_x, clockRecord.getCurriculumId() + ""));
        TextView tvCurriculumName = viewInput.findViewById(R.id.tv_curriculum_name);
        tvCurriculumName.setText(getString(R.string.curriculum_name_x, clockRecord.getCurriculumName()));

        TextView tvCurriculumPrice = viewInput.findViewById(R.id.tv_curriculum_price);
        tvCurriculumPrice.setText(getString(R.string.curriculum_price_x,
                FormatUtils.priceFormat(clockRecord.getCurriculumPrice())));

        TextView tvCurriculumDiscountPrice = viewInput.findViewById(R.id.tv_discount_price);
        tvCurriculumDiscountPrice.setText(getString(R.string.curriculum_discount_price_x,
                FormatUtils.priceFormat(clockRecord.getCurriculumDiscountPrice())));

        EditText etCount = viewInput.findViewById(R.id.et_curriculum_count);
        etCount.setText(FormatUtils.priceFormat(clockRecord.getUnit()));

        RadioGroup radioGroup = viewInput.findViewById(R.id.rg_clock_type);
        radioGroup.check(clockRecord.getClockType() == 1 ? R.id.rb_adjustment
                : (clockRecord.getClockType() == 2 ? R.id.rb_leave : R.id.rb_normal));

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.student_clock_detail).setView(viewInput)
                .setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton(R.string.delete_record, (dialogInterface, i) -> {
                    clockViewModel.delete(clockRecord.getId());
                    clockViewModel.getClockRecordDetailList(clockRecordDTO.getCurriculumId(),
                            clockRecordDTO.getClockTime());
                })
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    if (!TextUtils.isEmpty(etCount.getText())) {
                        int id = radioGroup.getCheckedRadioButtonId();
                        int clockType = 0;
                        if (id == R.id.rb_adjustment) {
                            clockType = 1;
                        } else if (id == R.id.rb_leave) {
                            clockType = 2;
                        }
                        clockRecord.setClockType(clockType);
                        clockRecord.setUnit(Float.parseFloat(etCount.getText().toString()));
                        long logId = clockViewModel.update(clockRecord);
                        Toast.makeText(this, logId > 0 ? R.string.update_success : R.string.update_fail
                                , Toast.LENGTH_SHORT).show();
                        if (logId > 0) {
                            clockViewModel.getClockRecordDetailList(clockRecordDTO.getCurriculumId(),
                                    clockRecordDTO.getClockTime());
                        }
                    } else {
                        Toast.makeText(this, R.string.pls_input_clock_count, Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 1200) {
                LongSparseArray<String> sparseLongArray = new LongSparseArray<>();
                if (adapter != null && !adapter.getData().isEmpty()) {
                    for (ClockRecord record : adapter.getData()) {
                        sparseLongArray.put(record.getStudentId(), record.getStudentName());
                    }
                }
                List<Student> students = new ArrayList<>();
                Serializable serializable;
                if ((serializable = data.getSerializableExtra("students")) != null) {
                    @SuppressWarnings("unchecked")
                    List<StudentDTO> studentDTOS = (List<StudentDTO>) serializable;
                    if (!studentDTOS.isEmpty()) {
                        for (StudentDTO studentDTO : studentDTOS) {
                            if (sparseLongArray.get(studentDTO.getId(), null) == null) {
                                Student student = new Student(studentDTO.getId(), studentDTO.getName(), studentDTO.getAvatar(),
                                        studentDTO.getPhone(), studentDTO.getSex(),
                                        studentDTO.getBirthday(), studentDTO.getRecruitTime(), studentDTO.getRecruitGradeCode(),
                                        studentDTO.getRecruitGradeName(), studentDTO.getCurrentGradeCode(), studentDTO.getCurrentGrade(),
                                        studentDTO.getStudentType(), studentDTO.getStudentTypeName(), studentDTO.getCostType(), studentDTO.getCostTypeName(),
                                        studentDTO.getRemarks(), studentDTO.getGuardian1(), studentDTO.getGuardian1Phone(), studentDTO.getGuardian2(), studentDTO.getGuardian2Phone());
                                students.add(student);
                            }
                        }
                    }
                }
                if (!students.isEmpty()) {
                    clockViewModel.insertClockRecord(clockRecordDTO.getClockTime(), clockRecordDTO.getCurriculumId(),
                            clockRecordDTO.getCurriculumName(), clockRecordDTO.getCurriculumPrice(), students,
                            clockType, clockRecordDTO.getUnit());
                    clockViewModel.getClockRecordDetailList(clockRecordDTO.getCurriculumId(), clockRecordDTO.getClockTime());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}