package com.jwong.education.ui.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jwong.education.R;
import com.jwong.education.dao.Student;
import com.jwong.education.dto.StudentDTO;
import com.jwong.education.ui.StudentInfoActivity;

public class StudentFragment extends Fragment implements BaseQuickAdapter.OnItemClickListener {

    private StudentViewModel studentViewModel;
    private Spinner spinner;
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
        spinner = root.findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.student_types)));
        spinner.setOnItemSelectedListener(listener);
        spinner.setSelection(1);
        studentViewModel.getStudentList().observe(this, students -> {
            StudentAdapter adapter = new StudentAdapter(R.layout.list_item_student, students, false);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
        });
        return root;
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Student student = studentViewModel.getStudentList().getValue().get(position);
        StudentDTO studentDTO = new StudentDTO(student.getId(), student.getName(), student.getAvatar(),
                student.getSex(), student.getBirthday(), student.getRecruitTime(), student.getRecruitGradeCode(),
                student.getRecruitGradeName(), student.getCurrentGradeCode(), student.getCurrentGrade(),
                student.getStudentType(), student.getStudentTypeName(), student.getGuardian1(),
                student.getGuardian1Phone(), student.getGuardian2(), student.getGuardian2Phone());
        Intent intent = new Intent(getActivity(), StudentInfoActivity.class);
        intent.putExtra("student", studentDTO);
        startActivityForResult(intent, 1100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1100) {
                StudentDTO studentDTO = (StudentDTO) data.getSerializableExtra("student");
                Student student = new Student(studentDTO.getId(), studentDTO.getName(), studentDTO.getAvatar(), studentDTO.getSex(),
                        studentDTO.getBirthday(), studentDTO.getRecruitTime(), studentDTO.getRecruitGradeCode(),
                        studentDTO.getRecruitGradeName(), studentDTO.getCurrentGradeCode(), studentDTO.getCurrentGrade(),
                        studentDTO.getStudentType(), studentDTO.getStudentTypeName(), studentDTO.getGuardian1(),
                        studentDTO.getGuardian1Phone(), studentDTO.getGuardian2(), studentDTO.getGuardian2Phone());
                studentViewModel.updateStudent(student);
            } else if (requestCode == 1200) {
                StudentDTO studentDTO = (StudentDTO) data.getSerializableExtra("student");
                Student student = new Student(studentDTO.getId(), studentDTO.getName(), studentDTO.getAvatar(), studentDTO.getSex(),
                        studentDTO.getBirthday(), studentDTO.getRecruitTime(), studentDTO.getRecruitGradeCode(),
                        studentDTO.getRecruitGradeName(), studentDTO.getCurrentGradeCode(), studentDTO.getCurrentGrade(),
                        studentDTO.getStudentType(), studentDTO.getStudentTypeName(), studentDTO.getGuardian1(),
                        studentDTO.getGuardian1Phone(), studentDTO.getGuardian2(), studentDTO.getGuardian2Phone());
                studentViewModel.addStudent(student);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(getClass().getSimpleName(), "onOptionsItemSelected item:" + item.getItemId());
        Intent intent = new Intent(getActivity(), StudentInfoActivity.class);
        startActivityForResult(intent, 1200);
        return super.onOptionsItemSelected(item);
    }
}