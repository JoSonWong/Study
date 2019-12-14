package com.jwong.education.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jwong.education.R;
import com.jwong.education.dao.Student;
import com.jwong.education.dto.StudentDTO;
import com.jwong.education.util.DateFormatUtil;

public class StudentInfoActivity extends AppCompatActivity {

    private EditText etName, etBirthday, etRecruitDate, etGuardian1, etGuardian1Phone, etGuardian2, etGuardian2Phone;
    private Spinner spSex, spRecruitGrade, spCurrentGrade, spStudentType;
    private StudentDTO student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_input_student);
        student = (StudentDTO) getIntent().getSerializableExtra("student");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setTitle(student==null?R.string.add_student:R.string.student_info);
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
        }
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

}