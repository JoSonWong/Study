package com.jwong.education.ui.student;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jwong.education.R;
import com.jwong.education.dao.StudentCurriculum;
import com.jwong.education.dto.CurriculumDTO;
import com.jwong.education.ui.curriculum.CurriculumSelectActivity;

import java.util.List;

public class StudentCurriculumFragment extends Fragment implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    private RecyclerView rvStudentCurriculum;
    private StudentCurriculumViewModel studentCurriculumViewModel;
    private StudentCurriculumAdapter curriculumAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        studentCurriculumViewModel = ViewModelProviders.of(this).get(StudentCurriculumViewModel.class);
        View root = inflater.inflate(R.layout.fragment_student_curriculum, container, false);
        rvStudentCurriculum = root.findViewById(R.id.rv_curriculum);
        rvStudentCurriculum.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        rvStudentCurriculum.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        studentCurriculumViewModel.getStudentCurriculumList(StudentActivity.studentId)
                .observe(this, studentCurriculumList -> {
                    curriculumAdapter = new StudentCurriculumAdapter(R.layout.list_item_student_curriculum, studentCurriculumList);
                    curriculumAdapter.setOnItemClickListener(this);
                    rvStudentCurriculum.setAdapter(curriculumAdapter);
                });
        return root;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        StudentCurriculum studentCurriculum = (StudentCurriculum) adapter.getData().get(position);
        View viewInput = LayoutInflater.from(getContext()).inflate(R.layout.dlg_input_curriculum, null);
        EditText etName = viewInput.findViewById(R.id.et_name);
        etName.setText(studentCurriculum.getCurriculum().getName());
        etName.setEnabled(false);
        TextView tvPrice = viewInput.findViewById(R.id.tv_price);
        tvPrice.setText(R.string.discount_price);
        tvPrice.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        EditText etPrice = viewInput.findViewById(R.id.et_price);
        etPrice.setText(studentCurriculum.getDiscountPrice() + "");
        etPrice.setHint(R.string.pls_input_discount_price);
        etPrice.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.update_discount_price).setView(viewInput)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    if (!TextUtils.isEmpty(etPrice.getText())) {
                        studentCurriculum.setDiscountPrice(Double.parseDouble(etPrice.getText().toString()));
                    } else {
                        Toast.makeText(getContext(), R.string.pls_input_discount_price, Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getContext(), CurriculumSelectActivity.class);
        intent.putExtra("is_multiple", true);
        if (curriculumAdapter != null && curriculumAdapter.getData() != null) {
            List<StudentCurriculum> list = curriculumAdapter.getData();
            if (list != null && !list.isEmpty()) {
                long[] ids = new long[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    ids[i] = list.get(i).getCurriculumId();
                }
                intent.putExtra("checked_list", ids);
            }
        }
        startActivityForResult(intent, 1100);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1100) {
                List<CurriculumDTO> curriculumDTOS = (List<CurriculumDTO>) data.getSerializableExtra("curriculumList");
                studentCurriculumViewModel.handleSelectedCurriculum(StudentActivity.studentId, curriculumDTOS);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_nav_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}