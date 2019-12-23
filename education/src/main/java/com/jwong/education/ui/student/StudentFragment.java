package com.jwong.education.ui.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jwong.education.R;
import com.jwong.education.dao.Student;
import com.jwong.education.dto.StudentDTO;

import java.io.Serializable;

public class StudentFragment extends Fragment implements OnItemClickListener {

    private StudentViewModel studentViewModel;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        View root = inflater.inflate(R.layout.fragment_student, container, false);
        recyclerView = root.findViewById(R.id.rv_student);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        studentViewModel.getStudentList(1).observe(this, students -> {
            StudentAdapter adapter = new StudentAdapter(students, false);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Student student = (Student) adapter.getData().get(position);
        Intent intent = new Intent(getActivity(), StudentActivity.class);
        intent.putExtra("studentId", student.getId());
        startActivityForResult(intent, 1100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 1100) {
                Serializable serializable;
                if ((serializable = data.getSerializableExtra("student")) != null) {
                    StudentDTO studentDTO = (StudentDTO) serializable;
                    Student student = new Student(studentDTO.getId(), studentDTO.getName(), studentDTO.getAvatar(), studentDTO.getSex(),
                            studentDTO.getBirthday(), studentDTO.getRecruitTime(), studentDTO.getRecruitGradeCode(),
                            studentDTO.getRecruitGradeName(), studentDTO.getCurrentGradeCode(), studentDTO.getCurrentGrade(),
                            studentDTO.getStudentType(), studentDTO.getStudentTypeName(), studentDTO.getGuardian1(),
                            studentDTO.getGuardian1Phone(), studentDTO.getGuardian2(), studentDTO.getGuardian2Phone());
                    studentViewModel.update(student);
                }
            } else if (requestCode == 1200) {
                Serializable serializable;
                if ((serializable = data.getSerializableExtra("student")) != null) {
                    StudentDTO studentDTO = (StudentDTO) serializable;
                    Student student = new Student(studentDTO.getId(), studentDTO.getName(), studentDTO.getAvatar(), studentDTO.getSex(),
                            studentDTO.getBirthday(), studentDTO.getRecruitTime(), studentDTO.getRecruitGradeCode(),
                            studentDTO.getRecruitGradeName(), studentDTO.getCurrentGradeCode(), studentDTO.getCurrentGrade(),
                            studentDTO.getStudentType(), studentDTO.getStudentTypeName(), studentDTO.getGuardian1(),
                            studentDTO.getGuardian1Phone(), studentDTO.getGuardian2(), studentDTO.getGuardian2Phone());
                    studentViewModel.insert(student);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.student_top_nav_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Log.d(getClass().getSimpleName(), "onOptionsItemSelected 添加学生 >>> ");
                Intent intent = new Intent(getActivity(), StudentInfoActivity.class);
                startActivityForResult(intent, 1200);
                break;
            case R.id.action_all:
                Log.d(getClass().getSimpleName(), "onOptionsItemSelected 添加学生 >>> ");
                studentViewModel.getStudentList(-1);
                break;
            case R.id.action_studying:
                studentViewModel.getStudentList(1);
                break;
            case R.id.action_try:
                studentViewModel.getStudentList(0);
                break;
            case R.id.action_finish:
                studentViewModel.getStudentList(2);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}