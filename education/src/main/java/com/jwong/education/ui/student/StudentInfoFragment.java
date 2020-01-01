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

    private TextView tvName, tvPhone, tvCurrentGrade, tvStudentId, tvStudentType, tvCostType,
            tvBirthday, tvRecruitDate, tvRecruitGrade, tvRemarks, tvGuardian1, tvGuardian1Phone,
            tvGuardian2, tvGuardian2Phone;
    private ImageView ivSex;
    private StudentViewModel studentViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (getArguments() != null)
//            studentId = getArguments().getLong("studentId");
        View root = inflater.inflate(R.layout.fragment_student_info, container, false);
        tvName = root.findViewById(R.id.tv_name);
        tvPhone = root.findViewById(R.id.tv_phone);
        ivSex = root.findViewById(R.id.iv_sex);
        tvStudentId = root.findViewById(R.id.tv_student_id);
        tvCurrentGrade = root.findViewById(R.id.tv_grade);
        tvStudentType = root.findViewById(R.id.tv_student_type);
        tvCostType = root.findViewById(R.id.tv_cost_type);
        tvBirthday = root.findViewById(R.id.tv_birthday);
        tvRecruitDate = root.findViewById(R.id.tv_recruit);//收招日期
        tvRecruitGrade = root.findViewById(R.id.tv_recruit_grade);
        tvRemarks = root.findViewById(R.id.tv_remarks);
        tvGuardian1 = root.findViewById(R.id.tv_guardian1);
        tvGuardian1Phone = root.findViewById(R.id.tv_guardian1_phone);
        tvGuardian2 = root.findViewById(R.id.tv_guardian2);
        tvGuardian2Phone = root.findViewById(R.id.tv_guardian2_phone);

        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        studentViewModel.getStudent(StudentActivity.studentId).observe(this, student -> {
            tvName.setText(student.getName());
            tvPhone.setText(student.getPhone());
            tvStudentId.setText(getString(R.string.student_code_x,
                    FormatUtils.studentCodeFormat(student.getId())));
            ivSex.setImageResource(student.getSex() == 1 ? R.drawable.ic_female : R.drawable.ic_male);
            tvCurrentGrade.setText(student.getCurrentGrade());
            tvBirthday.setText(FormatUtils.convert2Date(student.getBirthday()));
            tvRecruitDate.setText(FormatUtils.convert2Date(student.getRecruitTime()));
            tvStudentType.setText(student.getStudentTypeName());
            tvCostType.setText(student.getCostTypeName());
            tvBirthday.setText(FormatUtils.convert2Date(student.getBirthday()));
            tvRecruitDate.setText(FormatUtils.convert2Date(student.getRecruitTime()));
            tvRecruitGrade.setText(student.getRecruitGradeName());
            tvRemarks.setText(student.getRemarks());
            tvGuardian1.setText(student.getGuardian1());
            tvGuardian1Phone.setText(student.getGuardian1Phone());
            tvGuardian2.setText(student.getGuardian2());
            tvGuardian2Phone.setText(student.getGuardian2Phone());
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(getContext(), StudentInfoActivity.class);
        intent.putExtra("studentId", StudentActivity.studentId);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_nav_menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}