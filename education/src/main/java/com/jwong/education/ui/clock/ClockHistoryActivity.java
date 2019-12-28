package com.jwong.education.ui.clock;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jwong.education.R;
import com.jwong.education.dao.ClockRecord;
import com.jwong.education.dto.ClockRecordDTO;

public class ClockHistoryActivity extends AppCompatActivity implements OnItemClickListener {

    private ClockViewModel clockViewModel;
    private RecyclerView rvClockHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_history);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setTitle(R.string.clock_history);
        }

        rvClockHistory = findViewById(R.id.rv_clock_record);
        rvClockHistory.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        rvClockHistory.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        clockViewModel = ViewModelProviders.of(this).get(ClockViewModel.class);
        clockViewModel.getClockRecordList(10000).observe(this, clockRecords -> {
            if (clockRecords != null) {
                ClockRecordAdapter adapter = new ClockRecordAdapter(clockRecords, false);
                adapter.setOnItemClickListener(this);
                rvClockHistory.setAdapter(adapter);

                View emptyView = LayoutInflater.from(this).inflate(R.layout.list_empty_view, null);
                ((TextView) emptyView.findViewById(R.id.tv_empty)).setText(R.string.no_click_record_tip);
                adapter.setEmptyView(emptyView);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_clock_top_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_month:

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
        ClockRecord clockRecord = (ClockRecord) adapter.getData().get(position);
        Intent intent = new Intent(this, ClockDetailActivity.class);
        ClockRecordDTO recordDTO = new ClockRecordDTO(clockRecord.getId(), clockRecord.getClockTime(), clockRecord.getStudentId(),
                clockRecord.getStudentName(), clockRecord.getCurriculumId(), clockRecord.getCurriculumName(),
                clockRecord.getCurriculumPrice(), clockRecord.getCurriculumDiscountPrice(), clockRecord.getClockType(),
                clockRecord.getUnit());
        intent.putExtra("clockRecord", recordDTO);
        startActivity(intent);
    }
}