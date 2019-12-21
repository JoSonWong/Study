package com.jwong.education.ui.curriculum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.jwong.education.dao.Curriculum;
import com.jwong.education.dto.CurriculumDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CurriculumSelectActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private CurriculumViewModel settingViewModel;
    private CurriculumAdapter curriculumAdapter;
    private boolean isMultiple;
    private long[] checkedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_curriculum);
        isMultiple = getIntent().getBooleanExtra("is_multiple", false);
        checkedList = getIntent().getLongArrayExtra("checked_list");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setLogo(R.drawable.ic_check_white_24dp);
            actionBar.setTitle(R.string.select_curriculum);
        }
        settingViewModel = ViewModelProviders.of(this).get(CurriculumViewModel.class);
        recyclerView = findViewById(R.id.rv_curriculum);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        settingViewModel.getCurriculumList().observe(this, curriculumList -> {
            curriculumAdapter = new CurriculumAdapter(R.layout.list_item_curriculum, curriculumList, true);
            if (checkedList != null && checkedList.length > 0) {
                curriculumAdapter.setCheckedList(checkedList);
            }
            curriculumAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(curriculumAdapter);
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (!isMultiple) {
            curriculumAdapter.clearChecked();
            curriculumAdapter.notifyDataSetChanged();
        }
        Curriculum curriculum = (Curriculum) adapter.getData().get(position);
        curriculumAdapter.setCheck(curriculum.getId(), !curriculumAdapter.isCheck(curriculum.getId()));
        curriculumAdapter.notifyItemChanged(position);
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
        Log.d(getClass().getSimpleName(), "item:" + item.getItemId());
        switch (item.getItemId()) {
            case Menu.FIRST:
                Intent data = new Intent();
                List<Curriculum> curriculumList = curriculumAdapter.getCheckedList();
                List<CurriculumDTO> studentDTOS = new ArrayList<>();
                for (Curriculum curriculum : curriculumList) {
                    CurriculumDTO curriculumDTO = new CurriculumDTO(curriculum.getId(), curriculum.getName(), curriculum.getPrice());
                    studentDTOS.add(curriculumDTO);
                }
                data.putExtra("curriculumList", (Serializable) studentDTOS);
                setResult(RESULT_OK, data);
                this.finish();
            case android.R.id.home:// 点击返回图标事件
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}