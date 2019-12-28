package com.jwong.education.ui.student;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jwong.education.R;
import com.jwong.education.dto.CostNotificationDTO;
import com.jwong.education.dto.CostNotificationItemDTO;
import com.jwong.education.util.FormatUtils;

public class StudentCostNotificationActivity extends AppCompatActivity implements OnItemClickListener, View.OnClickListener {

    private CostNotificationDTO costNotificationDTO;
    private TextView tvTitle, tvStudentName, tvStudentName2, tvGrade, tvCostName;
    private RecyclerView rvCost;
    private View footerView;
    private TextView tvAllTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_notification);
        costNotificationDTO = (CostNotificationDTO) getIntent().getSerializableExtra("cost_notification");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setTitle(R.string.cost_notification);
        }
        if (costNotificationDTO == null) {
            Toast.makeText(getApplicationContext(), "无费用信息", Toast.LENGTH_SHORT).show();
            finish();
        }
        footerView = LayoutInflater.from(this).inflate(R.layout.list_cost_notification_footer, null);
        tvAllTotal = footerView.findViewById(R.id.tv_all_total);
        tvTitle = findViewById(R.id.tv_title_cost);
        tvStudentName = findViewById(R.id.tv_name);
        tvStudentName2 = findViewById(R.id.tv_name2);
        tvGrade = findViewById(R.id.tv_grade);
        tvCostName = findViewById(R.id.tv_cost_name);
        rvCost = findViewById(R.id.rv_cost);
        rvCost.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCost.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        tvCostName.setOnClickListener(this);
        setData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_nav_menu_ok, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ok:
                break;
            case android.R.id.home:// 点击返回图标事件
                this.finish();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setData() {
        tvTitle.setText(getString(R.string.company_pay_notification, getString(R.string.company)));
        tvStudentName.setText(Html.fromHtml("<u>&nbsp;&nbsp;" + costNotificationDTO.getStudentName() + "&nbsp;&nbsp;</u>"));
        tvStudentName2.setText(Html.fromHtml("<u>&nbsp;&nbsp;" + costNotificationDTO.getStudentName() + "&nbsp;&nbsp;</u>"));
        tvGrade.setText(Html.fromHtml("<u>&nbsp;&nbsp;" + costNotificationDTO.getGrade() + "&nbsp;&nbsp;</u>"));
        tvCostName.setText(Html.fromHtml("<u>&nbsp;&nbsp;" + costNotificationDTO.getCostName() + "&nbsp;&nbsp;</u>"));

        if (costNotificationDTO.getCostList() != null) {
            CostNotificationAdapter adapter = new CostNotificationAdapter(costNotificationDTO.getCostList());
            adapter.setOnItemClickListener(this);
            rvCost.setAdapter(adapter);
            adapter.addFooterView(footerView);
            computeAllCost();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter.getData().get(position) instanceof CostNotificationItemDTO) {
            CostNotificationItemDTO item = (CostNotificationItemDTO) adapter.getData().get(position);
            Log.d(getClass().getSimpleName(), "项目：" + item.getCostName() + " 单价：" + item.getCostPrice()
                    + " 数量：" + item.getCurriculumCount() + " 合计：" + item.getTotal());
        }
    }

    private void computeAllCost() {
        double total = 0;
        if (costNotificationDTO.getCostList() != null) {
            for (CostNotificationItemDTO item : costNotificationDTO.getCostList()) {
                total = total + item.getTotal();
            }
        }
        tvAllTotal.setText(getString(R.string.rmb_x, FormatUtils.priceFormat(total)));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cost_name:

                break;
        }
    }
}