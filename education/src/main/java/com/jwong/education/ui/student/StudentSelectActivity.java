package com.jwong.education.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jwong.education.R;
import com.jwong.education.dao.Student;
import com.jwong.education.dao.StudentCurriculum;
import com.jwong.education.dto.StudentDTO;
import com.jwong.education.ui.student.StudentAdapter;
import com.jwong.education.ui.student.StudentCurriculumViewModel;
import com.jwong.education.ui.student.StudentViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StudentSelectActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private StudentViewModel studentViewModel;
    private StudentCurriculumViewModel studentCurriculumViewModel;
    private StudentAdapter studentAdapter;
    private boolean isMultiple;
    private long[] checkedList;
    private long curriculumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_student);
        curriculumId = getIntent().getLongExtra("curriculumId", 0);
        isMultiple = getIntent().getBooleanExtra("is_multiple", false);
        checkedList = getIntent().getLongArrayExtra("checked_list");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setTitle(R.string.select_curriculum);
        }
        recyclerView = findViewById(R.id.rv_student);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        if (curriculumId > 0) {
            studentCurriculumViewModel = ViewModelProviders.of(this).get(StudentCurriculumViewModel.class);
            studentCurriculumViewModel.queryCurriculumStudentList(curriculumId).observe(this, studentCurriculumList -> {
                if (studentCurriculumList != null && !studentCurriculumList.isEmpty()) {
                    List<Student> students = new ArrayList<>();
                    for (StudentCurriculum item : studentCurriculumList) {
                        students.add(item.getStudent());
                    }
                    studentAdapter = new StudentAdapter(R.layout.list_item_student, students, true);
                    if (checkedList != null && checkedList.length > 0) {
                        studentAdapter.setCheckedList(checkedList);
                    }
                    studentAdapter.setOnItemClickListener(this);
                    recyclerView.setAdapter(studentAdapter);
                }

            });
        } else {
            studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
            studentViewModel.getStudentList().observe(this, students -> {
                studentAdapter = new StudentAdapter(R.layout.list_item_student, students, true);
                if (checkedList != null && checkedList.length > 0) {
                    studentAdapter.setCheckedList(checkedList);
                }
                studentAdapter.setOnItemClickListener(this);
                recyclerView.setAdapter(studentAdapter);
            });
        }

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (!isMultiple) {
            studentAdapter.clearChecked();
            studentAdapter.notifyDataSetChanged();
        }
        Student student = (Student) adapter.getData().get(position);
        studentAdapter.setCheck(student.getId(), !studentAdapter.isCheck(student.getId()));
        studentAdapter.notifyItemChanged(position);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.top_nav_menu, menu);
        MenuItem moreItem = menu.add(Menu.NONE, Menu.FIRST, Menu.FIRST, null);
        moreItem.setIcon(R.drawable.ic_check_white_24dp);
        moreItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case  Menu.FIRST:
                Intent data = new Intent();
                List<Student> students = studentAdapter.getCheckedList();
                List<StudentDTO> studentDTOS = new ArrayList<>();
                for (Student student : students) {
                    StudentDTO studentDTO = new StudentDTO(student.getId(), student.getName(), student.getAvatar(),
                            student.getSex(), student.getBirthday(), student.getRecruitTime(), student.getRecruitGradeCode(),
                            student.getRecruitGradeName(), student.getCurrentGradeCode(), student.getCurrentGrade(),
                            student.getStudentType(), student.getStudentTypeName(), student.getGuardian1(),
                            student.getGuardian1Phone(), student.getGuardian2(), student.getGuardian2Phone());
                    studentDTOS.add(studentDTO);
                }
                data.putExtra("students", (Serializable) studentDTOS);
                setResult(RESULT_OK, data);
                this.finish();
            case android.R.id.home:// 点击返回图标事件
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}