package com.jwong.education.ui.student;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.jwong.education.R;
import com.jwong.education.dao.Student;
import com.jwong.education.dto.StudentDTO;
import com.jwong.education.util.FormatUtils;

import java.io.Serializable;

public class StudentInfoFragment extends Fragment {

    private TextView tvName, tvCurrentGrade, tvStudentId, tvStudentType, tvCostType,
            tvBirthday, tvRecruitDate, tvRecruitGrade, tvGuardian1, tvGuardian1Phone,
            tvGuardian2, tvGuardian2Phone;
    private ImageView ivSex;

    private StudentViewModel studentViewModel;
    private Student student;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setTitle(R.string.select_student);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (getArguments() != null)
//            studentId = getArguments().getLong("studentId");
        View root = inflater.inflate(R.layout.fragment_student_info, container, false);
        tvName = root.findViewById(R.id.tv_name);
        ivSex = root.findViewById(R.id.iv_sex);
        tvStudentId = root.findViewById(R.id.tv_student_id);
        tvCurrentGrade = root.findViewById(R.id.tv_grade);
        tvStudentType = root.findViewById(R.id.tv_student_type);
        tvCostType = root.findViewById(R.id.tv_cost_type);
        tvBirthday = root.findViewById(R.id.tv_birthday);
        tvRecruitDate = root.findViewById(R.id.tv_recruit);//收招日期
        tvRecruitGrade = root.findViewById(R.id.tv_recruit_grade);
        tvGuardian1 = root.findViewById(R.id.tv_guardian1);
        tvGuardian1Phone = root.findViewById(R.id.tv_guardian1_phone);
        tvGuardian2 = root.findViewById(R.id.tv_guardian2);
        tvGuardian2Phone = root.findViewById(R.id.tv_guardian2_phone);

        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        studentViewModel.getStudent(StudentActivity.studentId).observe(this, student -> {
            this.student = student;
            tvName.setText(student.getName());
            tvStudentId.setText(getString(R.string.student_code_x,
                    FormatUtils.studentCodeFormat(student.getId())));
            ivSex.setImageResource(student.getSex() == 1 ? R.drawable.ic_female : R.drawable.ic_male);
            tvCurrentGrade.setText(student.getCurrentGrade());
            tvBirthday.setText(FormatUtils.convert2Date(student.getBirthday()));
            tvRecruitDate.setText(FormatUtils.convert2Date(student.getRecruitTime()));
            tvStudentType.setText(student.getStudentTypeName());
            //TODO 增加收费类型
            tvCostType.setText("按课时");
            tvBirthday.setText(FormatUtils.convert2Date(student.getBirthday()));
            tvRecruitDate.setText(FormatUtils.convert2Date(student.getRecruitTime()));
            tvRecruitGrade.setText(student.getRecruitGradeName());
            tvGuardian1.setText(student.getGuardian1());
            tvGuardian1Phone.setText(student.getGuardian1Phone());
            tvGuardian2.setText(student.getGuardian2());
            tvGuardian2Phone.setText(student.getGuardian2Phone());
        });
        return root;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getContext(), StudentInfoActivity.class);
        StudentDTO studentDTO = new StudentDTO(student.getId(), student.getName(), student.getAvatar(),
                student.getSex(), student.getBirthday(), student.getRecruitTime(), student.getRecruitGradeCode(),
                student.getRecruitGradeName(), student.getCurrentGradeCode(), student.getCurrentGrade(),
                student.getStudentType(), student.getStudentTypeName(), student.getGuardian1(), student.getGuardian1Phone(),
                student.getGuardian2(), student.getGuardian2Phone());
        intent.putExtra("student", studentDTO);
        startActivityForResult(intent, 1200);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem moreItem = menu.add(Menu.NONE, Menu.FIRST, Menu.FIRST, null);
        moreItem.setIcon(R.drawable.ic_mode_edit_white_24dp);
        moreItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 1200) {
                Serializable serializable;
                if ((serializable = data.getSerializableExtra("student")) != null) {
                    StudentDTO studentDTO = (StudentDTO) serializable;
                    Student student = new Student(studentDTO.getId(), studentDTO.getName(), studentDTO.getAvatar(), studentDTO.getSex(),
                            studentDTO.getBirthday(), studentDTO.getRecruitTime(), studentDTO.getRecruitGradeCode(),
                            studentDTO.getRecruitGradeName(), studentDTO.getCurrentGradeCode(), studentDTO.getCurrentGrade(),
                            studentDTO.getStudentType(), studentDTO.getStudentTypeName(), studentDTO.getGuardian1(),
                            studentDTO.getGuardian1Phone(), studentDTO.getGuardian2(), studentDTO.getGuardian2Phone());
                    studentViewModel.update(student);
                    studentViewModel.getStudent(StudentActivity.studentId);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}