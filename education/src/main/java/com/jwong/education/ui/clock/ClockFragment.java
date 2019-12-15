package com.jwong.education.ui.clock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jwong.education.R;
import com.jwong.education.dao.Student;
import com.jwong.education.dto.CurriculumDTO;
import com.jwong.education.dto.StudentDTO;
import com.jwong.education.ui.CurriculumSelectActivity;
import com.jwong.education.ui.StudentSelectActivity;
import com.jwong.education.ui.student.StudentAdapter;

import java.util.ArrayList;
import java.util.List;

public class ClockFragment extends Fragment implements View.OnClickListener {

    private ClockViewModel clockViewModel;
    private View viewClock;
    private TextView tvCurriculum;
    private RecyclerView rvStudent, rvClockHistory;
    private CurriculumDTO curriculumDTO;
    private StudentAdapter studentAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        clockViewModel = ViewModelProviders.of(this).get(ClockViewModel.class);
        View root = inflater.inflate(R.layout.fragment_clock, container, false);
        root.findViewById(R.id.btn_clock).setOnClickListener(this);
        viewClock = root.findViewById(R.id.ll_clock);
        root.findViewById(R.id.tv_add_curriculum).setOnClickListener(this);
        root.findViewById(R.id.tv_add_student).setOnClickListener(this);
        tvCurriculum = root.findViewById(R.id.tv_curriculum);
        rvStudent = root.findViewById(R.id.rv_student);
        rvClockHistory = root.findViewById(R.id.rv_clock_history);

        rvStudent.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        rvClockHistory.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        clockViewModel.getClockRecordList().observe(this, curriculumList -> {

        });
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clock:
                if (viewClock.getVisibility() == View.VISIBLE) {
//                    clockViewModel.addClockRecord();
                } else {
                    viewClock.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_add_curriculum:
                Intent intent = new Intent(getActivity(), CurriculumSelectActivity.class);
                intent.putExtra("is_multiple", false);
                startActivityForResult(intent, 1100);
                break;
            case R.id.tv_add_student:
                intent = new Intent(getActivity(), StudentSelectActivity.class);
                intent.putExtra("is_multiple", true);
                if (studentAdapter != null) {
                    List<Student> list = studentAdapter.getData();
                    if (list != null && !list.isEmpty()) {
                        long[] ids = new long[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            ids[i] = list.get(i).getId();
                        }
                        intent.putExtra("checked_list", ids);
                    }
                }
                startActivityForResult(intent, 1200);
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(getClass().getSimpleName(), "onOptionsItemSelected item:" + item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1100) {
                List<CurriculumDTO> curriculumDTOS = (List<CurriculumDTO>) data.getSerializableExtra("curriculumList");
                if (curriculumDTOS != null && !curriculumDTOS.isEmpty()) {
                    curriculumDTO = curriculumDTOS.get(0);
                    tvCurriculum.setText(curriculumDTO.getName());
                }
            } else if (requestCode == 1200) {
                List<Student> students = new ArrayList<>();
                List<StudentDTO> studentDTOS = (List<StudentDTO>) data.getSerializableExtra("students");
                if (studentDTOS != null && !studentDTOS.isEmpty()) {
                    for (StudentDTO studentDTO : studentDTOS) {
                        Student student = new Student(studentDTO.getId(), studentDTO.getName(), studentDTO.getAvatar(), studentDTO.getSex(),
                                studentDTO.getBirthday(), studentDTO.getRecruitTime(), studentDTO.getRecruitGradeCode(),
                                studentDTO.getRecruitGradeName(), studentDTO.getCurrentGradeCode(), studentDTO.getCurrentGrade(),
                                studentDTO.getStudentType(), studentDTO.getStudentTypeName(), studentDTO.getGuardian1(),
                                studentDTO.getGuardian1Phone(), studentDTO.getGuardian2(), studentDTO.getGuardian2Phone());
                        students.add(student);
                    }
                }
                studentAdapter = new StudentAdapter(R.layout.list_item_student, students, false);
                rvStudent.setAdapter(studentAdapter);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}