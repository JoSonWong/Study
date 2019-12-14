package com.jwong.education.ui.setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jwong.education.MainActivity;
import com.jwong.education.R;
import com.jwong.education.dao.Curriculum;

public class SettingFragment extends Fragment implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    private SettingViewModel settingViewModel;
    private RecyclerView recyclerView;
    private CurriculumAdapter curriculumAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingViewModel =
                ViewModelProviders.of(this).get(SettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        root.findViewById(R.id.tv_add_curriculum).setOnClickListener(this);
        recyclerView = root.findViewById(R.id.rv_curriculum);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        settingViewModel.getCurriculumList().observe(this, curriculumList -> {
            for (Curriculum item : curriculumList) {
                Log.d(getClass().getSimpleName(), "课程id：" + item.getId()
                        + " 名称：" + item.getName() + " 原价：" + item.getPrice());
            }
            curriculumAdapter = new CurriculumAdapter(R.layout.list_item_curriculum, curriculumList);
            curriculumAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(curriculumAdapter);
        });
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_curriculum:
                showInput();
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Curriculum curriculum = settingViewModel.getCurriculumList().getValue().get(position);
        View viewInput = LayoutInflater.from(getContext()).inflate(R.layout.dlg_input_curriculum, null);
        EditText etName = viewInput.findViewById(R.id.et_name);
        etName.setText(curriculum.getName());
        EditText etPrice = viewInput.findViewById(R.id.et_price);
        etPrice.setText(curriculum.getPrice() + "");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setTitle(R.string.update_curriculum).setView(viewInput)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    if (!TextUtils.isEmpty(etName.getText()) && !TextUtils.isEmpty(etPrice.getText())) {
                        curriculum.setName(etName.getText().toString());
                        curriculum.setPrice(Double.parseDouble(etPrice.getText().toString()));
                        String result = settingViewModel.updateCurriculum(curriculum);
                        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), TextUtils.isEmpty(etName.getText()) ? R.string.pls_input_curriculum_name
                                : R.string.pls_input_curriculum_price, Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }


    private void showInput() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dlg_input_curriculum, null);
        EditText etName = view.findViewById(R.id.et_name);
        EditText etPrice = view.findViewById(R.id.et_price);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setTitle(R.string.add_curriculum).setView(view)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    if (!TextUtils.isEmpty(etName.getText()) && !TextUtils.isEmpty(etPrice.getText())) {
                        String result = settingViewModel.addCurriculum(etName.getText().toString(),
                                Double.parseDouble(etPrice.getText().toString()));
                        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), TextUtils.isEmpty(etName.getText()) ? R.string.pls_input_curriculum_name
                                : R.string.pls_input_curriculum_price, Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }
}