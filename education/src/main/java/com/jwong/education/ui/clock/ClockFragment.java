package com.jwong.education.ui.clock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jwong.education.R;
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.dao.Student;
import com.jwong.education.dto.ClockRecordDTO;
import com.jwong.education.dto.CurriculumDTO;
import com.jwong.education.dto.StudentDTO;
import com.jwong.education.ui.curriculum.CurriculumSelectActivity;
import com.jwong.education.ui.student.StudentActivity;
import com.jwong.education.ui.student.StudentSelectActivity;
import com.jwong.education.util.FormatUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClockFragment extends Fragment implements View.OnClickListener, OnItemClickListener {

    private ClockViewModel clockViewModel;
    private TextView tvTitleCurriculum, tvCurriculum, tvStudent;
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
        tvTitleCurriculum = root.findViewById(R.id.tv_title_curriculum);
        tvCurriculum = root.findViewById(R.id.tv_curriculum);
        tvCurriculum.setOnClickListener(this);
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
        clockViewModel.getClockRecordList(10).observe(this, clockRecords -> {
            if (clockRecords != null) {
                ClockRecordAdapter adapter = new ClockRecordAdapter(clockRecords, false);
                adapter.setOnItemClickListener(this);
                rvClockHistory.setAdapter(adapter);

                View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.list_empty_view, null);
                ((TextView) emptyView.findViewById(R.id.tv_empty)).setText(R.string.no_click_record_tip);
                adapter.setEmptyView(emptyView);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clock:
                if (curriculumDTO != null && studentAdapter != null && !studentAdapter.getData().isEmpty()) {
                    View viewInput = LayoutInflater.from(getContext()).inflate(R.layout.dlg_single_input, null);
                    TextView tvName = viewInput.findViewById(R.id.tv_name);
                    tvName.setText(R.string.pls_input_clock_count);
                    EditText etName = viewInput.findViewById(R.id.et_name);
//                    etName.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    etName.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
                            | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    etName.setText("1.0");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                            .setTitle(R.string.warm_tip)
                            .setView(viewInput)
                            .setNegativeButton(android.R.string.cancel, null)
                            .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                                float count;
                                if (!TextUtils.isEmpty(etName.getText()) && (count = Float.parseFloat(etName.getText().toString())) > 0) {
                                    clockViewModel.insertClockRecord(curriculumDTO, studentAdapter.getData(), count);
                                    Toast.makeText(getContext(), R.string.clock_success, Toast.LENGTH_SHORT).show();
                                    clearStudent();
                                    clearCurriculum();
                                } else {
                                    Toast.makeText(getContext(), R.string.pls_input_clock_count, Toast.LENGTH_SHORT).show();
                                }
                            });
                    builder.create().show();
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
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        MenuItem moreItem = menu.add(Menu.NONE, Menu.FIRST, Menu.FIRST, null);
        moreItem.setIcon(R.drawable.ic_list_white_24dp);
        moreItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(getClass().getSimpleName(), "onOptionsItemSelected item:" + item.getItemId());
        Intent intent = new Intent(getContext(), ClockHistoryActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 1100) {
                Serializable serializable;
                if ((serializable = data.getSerializableExtra("curriculumList")) != null) {
                    @SuppressWarnings("unchecked")
                    List<CurriculumDTO> curriculumDTOS = (List<CurriculumDTO>) serializable;
                    if (!curriculumDTOS.isEmpty()) {
                        curriculumDTO = curriculumDTOS.get(0);
                        tvCurriculum.setText(curriculumDTO.getName());
                        tvCurriculum.setVisibility(View.VISIBLE);
                        tvTitleCurriculum.setVisibility(View.VISIBLE);
                        clearStudent();
                        startSelectStudentActivity();
                    }
                }
            } else if (requestCode == 1200) {
                List<Student> students = new ArrayList<>();
                Serializable serializable;
                if ((serializable = data.getSerializableExtra("students")) != null) {
                    @SuppressWarnings("unchecked")
                    List<StudentDTO> studentDTOS = (List<StudentDTO>) serializable;
                    if (!studentDTOS.isEmpty()) {
                        for (StudentDTO studentDTO : studentDTOS) {
                            Student student = new Student(studentDTO.getId(), studentDTO.getName(), studentDTO.getAvatar(),
                                    studentDTO.getPhone(), studentDTO.getSex(), studentDTO.getBirthday(), studentDTO.getRecruitTime(),
                                    studentDTO.getRecruitGradeCode(), studentDTO.getRecruitGradeName(),
                                    studentDTO.getCurrentGradeCode(), studentDTO.getCurrentGrade(), studentDTO.getStudentType(), studentDTO.getStudentTypeName(),
                                    studentDTO.getCostType(), studentDTO.getCostTypeName(), studentDTO.getRemarks(),
                                    studentDTO.getGuardian1(), studentDTO.getGuardian1Phone(), studentDTO.getGuardian2(), studentDTO.getGuardian2Phone());
                            students.add(student);
                        }
                    }
                }
                Student studentAdd = new Student();
                studentAdd.setId(0L);
                studentAdd.setName("");
                students.add(studentAdd);
                rvStudent.setLayoutManager(new GridLayoutManager(getContext(), 4));
                studentAdapter = new StudentClockAdapter(students);
                studentAdapter.setOnItemClickListener(listenerStudent);
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
        tvTitleCurriculum.setVisibility(View.GONE);
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
                clockRecord.getCurriculumPrice(), clockRecord.getCurriculumDiscountPrice(), clockRecord.getClockType(),
                clockRecord.getUnit());
        intent.putExtra("clockRecord", recordDTO);
        startActivity(intent);
    }

    private OnItemClickListener listenerStudent = (adapter, view, position) -> {
        if (adapter.getData().get(position) instanceof Student) {
            Student student = (Student) adapter.getData().get(position);
            if (student.getId() > 0) {
                Intent intent = new Intent(getActivity(), StudentActivity.class);
                intent.putExtra("studentId", student.getId());
                startActivity(intent);
            } else {//add
                startSelectStudentActivity();
            }
        }
    };
}