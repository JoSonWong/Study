package com.jwong.education.ui.clock;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
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
import com.jwong.education.util.FormatUtils;
import com.jwong.education.util.Utils;

import java.util.Calendar;
import java.util.Date;

public class ClockHistoryActivity extends AppCompatActivity implements OnItemClickListener {

    private ClockViewModel clockViewModel;
    private RecyclerView rvClockHistory;
    private int year, month;

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

        Calendar cal = Calendar.getInstance();
        setMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);

        clockViewModel.getMonthClockRecordListGroupByClockTime(Utils.getYearMonthFirstDate(this.year, this.month), Utils.getYearMonthLastDate(this.year, this.month))
                .observe(this, clockRecords -> {
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

    private void setMonth(int year, int month) {
        this.year = year;
        this.month = month;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.clock_detail_month_x,
                    getString(R.string.year_x_month_x, year, month)));
        }
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
                showMonthPicker();
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

    private void showMonthPicker() {
        View dlgView = LayoutInflater.from(this).inflate(R.layout.dlg_day_picker, null);
        NumberPicker npYear = dlgView.findViewById(R.id.picker_year);
        int[] years = getResources().getIntArray(R.array.year_min_max);
        npYear.setMinValue(years[0]);
        npYear.setMaxValue(years[1]);
        npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npYear.setWrapSelectorWheel(true);

        NumberPicker npMonth = dlgView.findViewById(R.id.picker_month);
        int[] months = getResources().getIntArray(R.array.month_min_max);
        npMonth.setMinValue(months[0]);
        npMonth.setMaxValue(months[1]);
        npMonth.setWrapSelectorWheel(true);
        npMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        Calendar cal = Calendar.getInstance();
        Date textMonth = FormatUtils.convert2Month(getString(R.string.year_x_month_x, this.year, this.month));
        if (textMonth != null) {
            cal.setTime(textMonth);
        }
        npYear.setValue(cal.get(Calendar.YEAR));
        npMonth.setValue(cal.get(Calendar.MONTH) + 1);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.other_month_cost)
                .setView(dlgView)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    setMonth(npYear.getValue(), npMonth.getValue());
                    clockViewModel.getMonthClockRecordListGroupByClockTime(Utils.getYearMonthFirstDate(this.year, this.month),
                            Utils.getYearMonthLastDate(this.year, this.month));
                });
        builder.create().show();
    }
}