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
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.dto.ClockRecordDTO;
import com.jwong.education.ui.clock.ClockDetailActivity;
import com.jwong.education.ui.clock.ClockRecordAdapter;
import com.jwong.education.ui.clock.ClockViewModel;
import com.jwong.education.ui.report.CostAdapter;
import com.jwong.education.ui.report.ReportViewModel;

public class StudentCostDetailActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {

    private ReportViewModel reportViewModel;
    private RecyclerView rvCostDetail;
    private int year, month;
    private long studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_detail);
        studentId = getIntent().getLongExtra("studentId", 0);
        year = getIntent().getIntExtra("year", 0);
        month = getIntent().getIntExtra("month", 0);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setTitle(R.string.cost_detail);
        }

        rvCostDetail = findViewById(R.id.rv_cost);
        rvCostDetail.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        rvCostDetail.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        reportViewModel.getStudentCost(studentId, year, month).observe(this, costs -> {
            if (costs != null) {
                CostAdapter adapter = new CostAdapter(costs);
                adapter.setOnItemClickListener(this);
                rvCostDetail.setAdapter(adapter);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ok:

                break;
            case android.R.id.home:// 点击返回图标事件
                this.finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}