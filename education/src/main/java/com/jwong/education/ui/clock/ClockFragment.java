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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jwong.education.R;
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.dao.Student;
import com.jwong.education.dto.ClockRecordDTO;
import com.jwong.education.dto.CurriculumDTO;
import com.jwong.education.dto.StudentDTO;
import com.jwong.education.ui.curriculum.CurriculumSelectActivity;
import com.jwong.education.ui.student.StudentSelectActivity;

import java.util.ArrayList;
import java.util.List;

public class ClockFragment extends Fragment implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    private ClockViewModel clockViewModel;
    private TextView tvCurriculum, tvStudent;
    private RecyclerView rvStudent, rvClockHistory;
    private CurriculumDTO curriculumDTO;
    private StudentClockAdapter studentAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_clock, container, false);
        root.findViewById(R.id.btn_clock).setOnClickListener(this);
        tvStudent = root.findViewById(R.id.tv_student);
        tvStudent.setOnClickListener(this);
        tvCurriculum = root.findViewById(R.id.tv_curriculum);
        tvCurriculum.setOnClickListener(this);
        root.findViewById(R.id.tv_more).setOnClickListener(this);
        rvStudent = root.findViewById(R.id.rv_student);
        rvClockHistory = root.findViewById(R.id.rv_clock_history);
        rvStudent.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rvClockHistory.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        rvClockHistory.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        clockViewModel = ViewModelProviders.of(this).get(ClockViewModel.class);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        clockViewModel.getClockRecordList(5).observe(this, clockRecords -> {
            if (clockRecords != null) {
                ClockRecordAdapter adapter = new ClockRecordAdapter(clockRecords);
                adapter.setOnItemClickListener(this);
                rvClockHistory.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clock:
                if (curriculumDTO != null && studentAdapter != null && !studentAdapter.getData().isEmpty()) {
                    clockViewModel.insertClockRecord(curriculumDTO, studentAdapter.getData());
                    Toast.makeText(getContext(), R.string.clock_success, Toast.LENGTH_SHORT).show();
                    clearStudent();
                    clearCurriculum();
                } else {
                    if (curriculumDTO == null) {
                        startSelectCurriculumActivity();
                    } else if (studentAdapter == null || studentAdapter.getData().isEmpty()) {
                        startSelectStudentActivity();
                    }
                }
                break;
            case R.id.tv_curriculum:
                startSelectCurriculumActivity();
                break;
            case R.id.tv_student:
                startSelectStudentActivity();
                break;
            case R.id.tv_more:
                Intent intent = new Intent(getContext(), ClockHistoryActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void startSelectCurriculumActivity() {
        Intent intent = new Intent(getActivity(), CurriculumSelectActivity.class);
        intent.putExtra("is_multiple", false);
        startActivityForResult(intent, 1100);
    }

    private void startSelectStudentActivity() {
        Intent intent = new Intent(getContext(), StudentSelectActivity.class);
        intent.putExtra("is_multiple", true);
        intent.putExtra("curriculumId", curriculumDTO.getId());
        if (studentAdapter != null) {
            List<Student> list = studentAdapter.getData();
            if (!list.isEmpty()) {
                long[] ids = new long[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    ids[i] = list.get(i).getId();
                }
                intent.putExtra("checked_list", ids);
            }
        }
        startActivityForResult(intent, 1200);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(getClass().getSimpleName(), "onOptionsItemSelected item:" + item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1100) {
                List<CurriculumDTO> curriculumDTOS = (List<CurriculumDTO>) data.getSerializableExtra("curriculumList");
                if (curriculumDTOS != null && !curriculumDTOS.isEmpty()) {
                    curriculumDTO = curriculumDTOS.get(0);
                    tvCurriculum.setText(curriculumDTO.getName());
                    tvCurriculum.setVisibility(View.VISIBLE);
                    clearStudent();
                    startSelectStudentActivity();
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
                studentAdapter = new StudentClockAdapter(students);
                rvStudent.setAdapter(studentAdapter);
                tvStudent.setVisibility(View.VISIBLE);
                rvStudent.setVisibility(View.VISIBLE);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void clearCurriculum() {
        tvCurriculum.setText("");
        curriculumDTO = null;
        tvCurriculum.setVisibility(View.GONE);
    }

    private void clearStudent() {
        if (studentAdapter != null && !studentAdapter.getData().isEmpty()) {
            studentAdapter.getData().clear();
            studentAdapter.notifyDataSetChanged();
        }
        tvStudent.setVisibility(View.GONE);
        rvStudent.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ClockRecord clockRecord = (ClockRecord) adapter.getData().get(position);
        Intent intent = new Intent(getContext(), ClockDetailActivity.class);
        ClockRecordDTO recordDTO = new ClockRecordDTO(clockRecord.getId(), clockRecord.getClockTime(), clockRecord.getStudentId(),
                clockRecord.getStudentName(), clockRecord.getCurriculumId(), clockRecord.getCurriculumName(),
                clockRecord.getCurriculumPrice(), clockRecord.getCurriculumDiscountPrice(), clockRecord.getClockType());
        intent.putExtra("clockRecord", recordDTO);
        startActivity(intent);
    }
}