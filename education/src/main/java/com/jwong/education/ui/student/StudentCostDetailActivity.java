package com.jwong.education.ui.student;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jwong.education.R;
import com.jwong.education.dao.StudentMonthCost;
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
                showInput();
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

    private void showInput() {
        View view = LayoutInflater.from(this).inflate(R.layout.dlg_input_cost, null);
        EditText etName = view.findViewById(R.id.et_name);
        EditText etPrice = view.findViewById(R.id.et_price);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.add_cost)
                .setView(view)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    if (!TextUtils.isEmpty(etName.getText()) && !TextUtils.isEmpty(etPrice.getText())) {
                        StudentMonthCost cost = new StudentMonthCost();
                        cost.setStudentId(studentId);
                        cost.setYear(year);
                        cost.setMonth(month);
                        cost.setCostType(1);
                        cost.setCostName(etName.getText().toString());
                        double price = Double.parseDouble(etPrice.getText().toString());
                        cost.setPrice(price);
                        cost.setDiscountPrice(price);
                        reportViewModel.insert(cost);
                        Toast.makeText(this, R.string.add_cost_success, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, TextUtils.isEmpty(etName.getText()) ? R.string.pls_input_cost_name
                                : R.string.pls_input_cost, Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }
}