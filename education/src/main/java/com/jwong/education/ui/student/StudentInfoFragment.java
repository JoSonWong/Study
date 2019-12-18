package com.jwong.education.ui.student;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.jwong.education.R;
import com.jwong.education.dao.Student;
import com.jwong.education.dto.StudentDTO;
import com.jwong.education.util.DateFormatUtil;

import java.util.Calendar;
import java.util.Date;

public class StudentInfoFragment extends Fragment implements View.OnClickListener {

    private EditText etName, etBirthday, etRecruitDate, etGuardian1, etGuardian1Phone, etGuardian2, etGuardian2Phone;
    private Spinner spSex, spRecruitGrade, spCurrentGrade, spStudentType;
    private StudentViewModel studentViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        setMenuVisibility(false);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        if (getArguments() != null)
//            studentId = getArguments().getLong("studentId");
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        View root = inflater.inflate(R.layout.fragment_student_info, container, false);
        etName = root.findViewById(R.id.et_name);
        spSex = root.findViewById(R.id.sp_sex);
        etBirthday = root.findViewById(R.id.et_birthday);
        etBirthday.setOnClickListener(this);
        etRecruitDate = root.findViewById(R.id.et_recruit_date);
        etRecruitDate.setOnClickListener(this);
        spRecruitGrade = root.findViewById(R.id.sp_recruit_grade);
        spCurrentGrade = root.findViewById(R.id.sp_current_grade);
        spStudentType = root.findViewById(R.id.spinner_student_type);
        etGuardian1 = root.findViewById(R.id.et_guardian1);
        etGuardian1Phone = root.findViewById(R.id.et_guardian1_phone);
        etGuardian2 = root.findViewById(R.id.et_guardian2);
        etGuardian2Phone = root.findViewById(R.id.et_guardian2_phone);

        spSex.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sex_types)));
        spRecruitGrade.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.grades)));
        spCurrentGrade.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.grades)));
        spStudentType.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.student_types)));

        studentViewModel.getStudent(StudentActivity.studentId).observe(this, student -> {
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
        });
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_birthday:
            case R.id.et_recruit_date:
                showDatePicker((EditText) view);
                break;
            default:
                break;
        }
    }

    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        Date date = DateFormatUtil.convert2Date(editText.getText().toString());
        if (date != null) {
            calendar.setTime(date);
        }
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                (DatePicker view, int year, int monthOfYear, int dayOfMonth) ->
                        editText.setText(year + "-" + monthOfYear + "-" + dayOfMonth)
                , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        saveStudent();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void saveStudent() {
        Student student = new Student();
        student.setId(StudentActivity.studentId);
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
        studentViewModel.updateStudent(student);
        Log.d(getClass().getSimpleName(), "保持学生信息");
    }
}