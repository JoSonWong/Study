package com.jwong.education.ui.student;

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
import com.jwong.education.dto.entity.StudentDetailNode;

public class StudentFragment extends Fragment implements OnItemClickListener {

    private StudentViewModel studentViewModel;
    private RecyclerView recyclerView;
    private StudentTreeAdapter adapter = new StudentTreeAdapter();


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
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        studentViewModel.getStudentGroup().observe(this, baseNodes -> {
            adapter.setNewData(baseNodes);
            adapter.setOnItemClickListener(this);
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter.getData().get(position) instanceof StudentDetailNode) {
            StudentDetailNode student = (StudentDetailNode) adapter.getData().get(position);
            Intent intent = new Intent(getActivity(), StudentActivity.class);
            intent.putExtra("studentId", student.getId());
            startActivity(intent);
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_nav_menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Log.d(getClass().getSimpleName(), "onOptionsItemSelected 添加学生 >>> ");
            Intent intent = new Intent(getActivity(), StudentInfoActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}