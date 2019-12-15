package com.jwong.education.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jwong.education.R;
import com.jwong.education.dao.Student;
import com.jwong.education.dao.StudentCurriculum;
import com.jwong.education.dto.CurriculumDTO;
import com.jwong.education.dto.StudentDTO;
import com.jwong.education.ui.student.StudentAdapter;
import com.jwong.education.ui.student.StudentCurriculumAdapter;
import com.jwong.education.ui.student.StudentCurriculumViewModel;
import com.jwong.education.util.DateFormatUtil;

import java.util.ArrayList;
import java.util.List;

public class StudentInfoActivity extends AppCompatActivity implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    private EditText etName, etBirthday, etRecruitDate, etGuardian1, etGuardian1Phone, etGuardian2, etGuardian2Phone;
    private Spinner spSex, spRecruitGrade, spCurrentGrade, spStudentType;
    private StudentDTO student;
    private RecyclerView rvStudentCurriculum;
    private StudentCurriculumViewModel studentCurriculumViewModel;
    private StudentCurriculumAdapter curriculumAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        student = (StudentDTO) getIntent().getSerializableExtra("student");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setTitle(student == null ? R.string.add_student : R.string.student_info);
        }

        etName = findViewById(R.id.et_name);
        spSex = findViewById(R.id.sp_sex);

        etBirthday = findViewById(R.id.et_birthday);
        etRecruitDate = findViewById(R.id.et_recruit_date);

        spRecruitGrade = findViewById(R.id.sp_recruit_grade);
        spCurrentGrade = findViewById(R.id.sp_current_grade);
        spStudentType = findViewById(R.id.spinner_student_type);

        etGuardian1 = findViewById(R.id.et_guardian1);
        etGuardian1Phone = findViewById(R.id.et_guardian1_phone);
        etGuardian2 = findViewById(R.id.et_guardian2);
        etGuardian2Phone = findViewById(R.id.et_guardian2_phone);

        findViewById(R.id.btn_curriculum).setOnClickListener(this);
        rvStudentCurriculum = findViewById(R.id.rv_curriculum);
        rvStudentCurriculum.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        spSex.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sex_types)));
        spRecruitGrade.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.grades)));
        spCurrentGrade.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.grades)));
        spStudentType.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.student_types)));

        if (student != null) {
            etName.setText(student.getName());
            spSex.setSelection(student.getSex());
            etBirthday.setText(DateFormatUtil.convert2Date(student.getBirthday()));
            etRecruitDate.setText(DateFormatUtil.convert2Date(student.getRecruitTime()));
            spRecruitGrade.setSelection(student.getRecruitGradeCode());
            spCurrentGrade.setSelection(student.getCurrentGradeCode());
            spStudentType.setSelection(student.getStudentType());
            etGuardian1.setText(student.getGuardian1());
            etGuardian1Phone.setText(student.getGuardian1Phone());
            etGuardian2.setText(student.getGuardian2());
            etGuardian2Phone.setText(student.getGuardian2Phone());

            findViewById(R.id.tr_curriculum_title).setVisibility(View.VISIBLE);
            rvStudentCurriculum.setVisibility(View.VISIBLE);
            studentCurriculumViewModel = ViewModelProviders.of(this).get(StudentCurriculumViewModel.class);
            studentCurriculumViewModel.getStudentCurriculumList(student.getId()).observe(this, studentCurriculumList -> {
                curriculumAdapter = new StudentCurriculumAdapter(R.layout.list_item_student_curriculum, studentCurriculumList);
                curriculumAdapter.setOnItemClickListener(this);
                rvStudentCurriculum.setAdapter(curriculumAdapter);
            });
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        StudentCurriculum studentCurriculum = (StudentCurriculum) adapter.getData().get(position);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ok:
                Intent data = new Intent();
                if (student == null) {
                    student = new StudentDTO();
                }
                student.setName(etName.getText().toString());
                student.setAvatar("");
                student.setSex(spSex.getSelectedItemPosition());
                student.setBirthday(DateFormatUtil.convert2Date(etBirthday.getText().toString()));
                student.setRecruitTime(DateFormatUtil.convert2Date(etRecruitDate.getText().toString()));
                student.setRecruitGradeCode(spRecruitGrade.getSelectedItemPosition());
                student.setRecruitGradeName(getResources().getStringArray(R.array.grades)[spRecruitGrade.getSelectedItemPosition()]);
                student.setCurrentGradeCode(spCurrentGrade.getSelectedItemPosition());
                student.setCurrentGrade(getResources().getStringArray(R.array.grades)[spCurrentGrade.getSelectedItemPosition()]);
                student.setStudentType(spStudentType.getSelectedItemPosition());
                student.setStudentTypeName(getResources().getStringArray(R.array.student_types)[spStudentType.getSelectedItemPosition()]);
                student.setGuardian1(etGuardian1.getText().toString());
                student.setGuardian1Phone(etGuardian1Phone.getText().toString());
                student.setGuardian2(etGuardian2.getText().toString());
                student.setGuardian2Phone(etGuardian2Phone.getText().toString());
                data.putExtra("student", student);
                setResult(RESULT_OK, data);
                this.finish();
            case android.R.id.home:// 点击返回图标事件
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_curriculum:
                Intent intent = new Intent(this, CurriculumSelectActivity.class);
                intent.putExtra("is_multiple", true);
                if (curriculumAdapter != null && curriculumAdapter.getData() != null) {
                    List<StudentCurriculum> list = curriculumAdapter.getData();
                    if (list != null && !list.isEmpty()) {
                        long[] ids = new long[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            ids[i] = list.get(i).getCurriculumId();
                        }
                        intent.putExtra("checked_list", ids);
                    }
                }
                startActivityForResult(intent, 1100);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1100) {
                List<CurriculumDTO> curriculumDTOS = (List<CurriculumDTO>) data.getSerializableExtra("curriculumList");
                if (curriculumDTOS != null && !curriculumDTOS.isEmpty()) {
                    for (CurriculumDTO curriculumDTO : curriculumDTOS) {
                        StudentCurriculum studentCurriculum = new StudentCurriculum();
                        studentCurriculum.setCurriculumId(curriculumDTO.getId());
                        studentCurriculum.setStudentId(student.getId());
                        studentCurriculum.setDiscountPrice(curriculumDTO.getPrice());
                        studentCurriculumViewModel.addStudent(studentCurriculum);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}