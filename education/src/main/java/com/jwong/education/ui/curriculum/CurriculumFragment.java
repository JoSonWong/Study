package com.jwong.education.ui.curriculum;

import android.app.AlertDialog;
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
import android.widget.Toast;

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
import com.jwong.education.dao.Curriculum;
import com.jwong.education.util.FormatUtils;

public class CurriculumFragment extends Fragment implements OnItemClickListener {

    private CurriculumViewModel settingViewModel;
    private RecyclerView recyclerView;
    private CurriculumAdapter curriculumAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingViewModel =
                ViewModelProviders.of(this).get(CurriculumViewModel.class);
        View root = inflater.inflate(R.layout.fragment_curriculum, container, false);
        recyclerView = root.findViewById(R.id.rv_curriculum);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        settingViewModel.getCurriculumList().observe(this, curriculumList -> {
            curriculumAdapter = new CurriculumAdapter(curriculumList, false);
            curriculumAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(curriculumAdapter);
        });
        return root;
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Curriculum curriculum = (Curriculum) adapter.getData().get(position);
        View viewInput = LayoutInflater.from(getContext()).inflate(R.layout.dlg_input_curriculum, null);
        EditText etName = viewInput.findViewById(R.id.et_name);
        etName.setText(curriculum.getName());
        EditText etPrice = viewInput.findViewById(R.id.et_price);
        etPrice.setText(FormatUtils.priceFormat(curriculum.getPrice()));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(getClass().getSimpleName(), "onOptionsItemSelected item:" + item.getItemId());
        showInput();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_nav_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}