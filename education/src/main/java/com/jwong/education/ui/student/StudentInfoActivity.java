package com.jwong.education.ui.student;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.jwong.education.R;
import com.jwong.education.dao.Student;
import com.jwong.education.util.FormatUtils;

import java.util.Calendar;
import java.util.Date;

public class StudentInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName, etBirthday, etRecruitDate, etGuardian1, etGuardian1Phone, etGuardian2, etGuardian2Phone;
    private Spinner spRecruitGrade, spCurrentGrade;
    private RadioGroup rgSex, rgType, rgCostType;
    private Long studentId;
    private StudentViewModel studentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        studentId = getIntent().getLongExtra("studentId", 0);
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setTitle(studentId == null || studentId <= 0 ? R.string.add_student : R.string.student_info);
        }
        etName = findViewById(R.id.et_name);
        rgSex = findViewById(R.id.rg_sex);
        etBirthday = findViewById(R.id.et_birthday);
        etBirthday.setOnClickListener(this);
        etRecruitDate = findViewById(R.id.et_recruit_date);
        etRecruitDate.setOnClickListener(this);
        spRecruitGrade = findViewById(R.id.sp_recruit_grade);
        spCurrentGrade = findViewById(R.id.sp_current_grade);
        rgType = findViewById(R.id.rg_type);
        rgCostType = findViewById(R.id.rg_cost_type);
        etGuardian1 = findViewById(R.id.et_guardian1);
        etGuardian1Phone = findViewById(R.id.et_guardian1_phone);
        etGuardian2 = findViewById(R.id.et_guardian2);
        etGuardian2Phone = findViewById(R.id.et_guardian2_phone);

        spRecruitGrade.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1, getResources().getStringArray(R.array.grades)));
        spCurrentGrade.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1, getResources().getStringArray(R.array.grades)));


        if (studentId != null && studentId > 0) {
            studentViewModel.getStudent(studentId).observe(this, student -> {
                etName.setText(student.getName());
                rgSex.check(student.getSex() == 1 ? R.id.rb_female : R.id.rb_male);
                etBirthday.setText(FormatUtils.convert2Date(student.getBirthday()));
                etRecruitDate.setText(FormatUtils.convert2Date(student.getRecruitTime()));
                spRecruitGrade.setSelection(student.getRecruitGradeCode());
                spCurrentGrade.setSelection(student.getCurrentGradeCode());
                rgType.check(student.getStudentType() == 1 ? R.id.rb_studying
                        : (student.getStudentType() == 2 ? R.id.rb_finished : R.id.rb_try));
                rgCostType.check(student.getCostType() == 1 ? R.id.rb_term : R.id.rb_curriculum);
                etGuardian1.setText(student.getGuardian1());
                etGuardian1Phone.setText(student.getGuardian1Phone());
                etGuardian2.setText(student.getGuardian2());
                etGuardian2Phone.setText(student.getGuardian2Phone());
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_nav_menu_ok, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ok:
                if (!TextUtils.isEmpty(etName.getText())) {
                    Student student = new Student();
                    if (studentId != null && studentId > 0) {
                        student.setId(studentId);
                    }
                    student.setName(etName.getText().toString());
                    student.setAvatar("");
                    student.setSex(rgSex.getCheckedRadioButtonId() == R.id.rb_female ? 1 : 0);
                    student.setBirthday(FormatUtils.convert2Date(etBirthday.getText().toString()));
                    student.setRecruitTime(FormatUtils.convert2Date(etRecruitDate.getText().toString()));
                    student.setRecruitGradeCode(spRecruitGrade.getSelectedItemPosition());
                    student.setRecruitGradeName(getResources().getStringArray(R.array.grades)[spRecruitGrade.getSelectedItemPosition()]);
                    student.setCurrentGradeCode(spCurrentGrade.getSelectedItemPosition());
                    student.setCurrentGrade(getResources().getStringArray(R.array.grades)[spCurrentGrade.getSelectedItemPosition()]);
                    student.setStudentType(rgType.getCheckedRadioButtonId() == R.id.rb_studying ? 1
                            : (rgType.getCheckedRadioButtonId() == R.id.rb_try ? 0 : 2));
                    student.setStudentTypeName(((RadioButton) findViewById(rgType.getCheckedRadioButtonId())).getText().toString());
                    student.setCostType(rgCostType.getCheckedRadioButtonId() == R.id.rb_term ? 1 : 0);
                    student.setCostTypeName(((RadioButton) findViewById(
                            rgCostType.getCheckedRadioButtonId())).getText().toString());
                    student.setGuardian1(etGuardian1.getText().toString());
                    student.setGuardian1Phone(etGuardian1Phone.getText().toString());
                    student.setGuardian2(etGuardian2.getText().toString());
                    student.setGuardian2Phone(etGuardian2Phone.getText().toString());
                    if (student.getId() != null && student.getId() > 0) {
                        studentViewModel.update(student);
                        Toast.makeText(getApplicationContext(), R.string.update_success, Toast.LENGTH_SHORT).show();
                    } else {
                        studentViewModel.insert(student);
                        Toast.makeText(getApplicationContext(), R.string.add_student_success, Toast.LENGTH_SHORT).show();
                    }
                    this.finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.pls_input_student_name, Toast.LENGTH_SHORT).show();
                }
            case android.R.id.home:// 点击返回图标事件
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_birthday:
            case R.id.et_recruit_date:
                showDatePicker((EditText) view);
                break;
        }
    }

    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        Date date = FormatUtils.convert2Date(editText.getText().toString());
        if (date != null) {
            calendar.setTime(date);
        }
        DatePickerDialog dpd = new DatePickerDialog(this,
                DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                (DatePicker view, int year, int monthOfYear, int dayOfMonth) ->
                        editText.setText(getString(R.string.x_year_x_month_x_day, year, monthOfYear + 1, dayOfMonth))
                , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }
}