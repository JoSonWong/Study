package com.jwong.education.ui.clock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.jwong.education.R;
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.dao.Student;
import com.jwong.education.dto.ClockRecordDTO;
import com.jwong.education.dto.StudentDTO;
import com.jwong.education.ui.student.StudentSelectActivity;
import com.jwong.education.util.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class ClockDetailActivity extends AppCompatActivity implements View.OnClickListener,
        BaseQuickAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ClockViewModel clockViewModel;
    private ClockRecordDetailAdapter adapter;
    private ClockRecordDTO clockRecordDTO;
    private TextView tvCurriculum, tvClockTime;
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
        tvClockTime = findViewById(R.id.tv_clock_time);
        recyclerView = findViewById(R.id.rv_clock_record);
        findViewById(R.id.btn_patch_card).setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        tvCurriculum.setText(clockRecordDTO.getCurriculumName());
        tvClockTime.setText(FormatUtils.convert2DateTime(clockRecordDTO.getClockTime()));
        clockViewModel = ViewModelProviders.of(this).get(ClockViewModel.class);
        clockViewModel.getClockRecordDetailList(clockRecordDTO.getCurriculumId(),
                clockRecordDTO.getClockTime()).observe(this, clockRecords -> {
            adapter = new ClockRecordDetailAdapter(clockRecords);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem moreItem = menu.add(Menu.NONE, Menu.FIRST, Menu.FIRST, null);
        moreItem.setIcon(R.drawable.ic_delete_white_24dp);
        moreItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.delete_record)
                        .setMessage(getString(R.string.delete_curriculum_x_time_x_clock_record, clockRecordDTO.getCurriculumName(),
                                FormatUtils.convert2DateTime(clockRecordDTO.getClockTime())))
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
        }
    }


    private void startSelectStudentActivity(int clockType) {
        this.clockType = clockType;
        Intent intent = new Intent(this, StudentSelectActivity.class);
        intent.putExtra("is_multiple", true);
        intent.putExtra("curriculumId", clockRecordDTO.getCurriculumId());
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
        tvCurriculumPrice.setText(getString(R.string.curriculum_price_x, clockRecord.getCurriculumPrice() + ""));

        EditText etDiscountPrice = viewInput.findViewById(R.id.et_discount_price);
        etDiscountPrice.setText(clockRecord.getCurriculumDiscountPrice() + "");

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
                        Toast.makeText(this, logId > 0 ? R.string.update_success : R.string.update_fail
                                , Toast.LENGTH_SHORT).show();
                        if (logId > 0) {
                            clockViewModel.getClockRecordDetailList(clockRecordDTO.getCurriculumId(),
                                    clockRecordDTO.getClockTime());
                        }
                    } else {
                        Toast.makeText(this, R.string.pls_input_discount_price, Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1200) {
                LongSparseArray<String> sparseLongArray = new LongSparseArray<>();
                if (adapter != null && adapter.getData() != null && !adapter.getData().isEmpty()) {
                    for (ClockRecord record : adapter.getData()) {
                        sparseLongArray.put(record.getStudentId(), record.getStudentName());
                    }
                }
                List<Student> students = new ArrayList<>();
                List<StudentDTO> studentDTOS = (List<StudentDTO>) data.getSerializableExtra("students");
                if (studentDTOS != null && !studentDTOS.isEmpty()) {
                    for (StudentDTO studentDTO : studentDTOS) {
                        if (sparseLongArray.get(studentDTO.getId(), null) == null) {
                            Student student = new Student(studentDTO.getId(), studentDTO.getName(), studentDTO.getAvatar(), studentDTO.getSex(),
                                    studentDTO.getBirthday(), studentDTO.getRecruitTime(), studentDTO.getRecruitGradeCode(),
                                    studentDTO.getRecruitGradeName(), studentDTO.getCurrentGradeCode(), studentDTO.getCurrentGrade(),
                                    studentDTO.getStudentType(), studentDTO.getStudentTypeName(), studentDTO.getGuardian1(),
                                    studentDTO.getGuardian1Phone(), studentDTO.getGuardian2(), studentDTO.getGuardian2Phone());
                            students.add(student);
                        }
                    }
                }
                if (!students.isEmpty()) {
                    clockViewModel.insertClockRecord(clockRecordDTO.getClockTime(), clockRecordDTO.getCurriculumId(),
                            clockRecordDTO.getCurriculumName(), clockRecordDTO.getCurriculumPrice(), students, clockType);
                    clockViewModel.getClockRecordDetailList(clockRecordDTO.getCurriculumId(), clockRecordDTO.getClockTime());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}