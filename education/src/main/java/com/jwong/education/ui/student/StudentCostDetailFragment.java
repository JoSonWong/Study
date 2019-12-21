package com.jwong.education.ui.student;

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
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jwong.education.R;
import com.jwong.education.dao.StudentMonthCost;
import com.jwong.education.ui.clock.ClockViewModel;
import com.jwong.education.ui.report.CostAdapter;
import com.jwong.education.ui.report.ReportViewModel;
import com.jwong.education.util.FormatUtils;
import com.jwong.education.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StudentCostDetailFragment extends Fragment implements BaseQuickAdapter.OnItemClickListener {

    private ReportViewModel reportViewModel;
    private StudentViewModel studentViewModel;
    private RecyclerView rvCostDetail;
    private TextView tvStudentName, tvStudentId, tvGrade, tvYearMonth, tvTotalCost;
    private int year, month;
    private long studentId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_cost_detail, container, false);
        studentId = StudentActivity.studentId;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        root.findViewById(R.id.tv_date).setVisibility(View.GONE);
        tvTotalCost = root.findViewById(R.id.tv_cost);
        tvStudentName = root.findViewById(R.id.tv_name);
        tvStudentId = root.findViewById(R.id.tv_student_id);
        tvGrade = root.findViewById(R.id.tv_grade);
        tvYearMonth = root.findViewById(R.id.tv_month);
        rvCostDetail = root.findViewById(R.id.rv_cost);
        rvCostDetail.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        rvCostDetail.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        studentViewModel.getStudent(studentId).observe(this, student -> {
            tvStudentName.setText(student.getName());
            tvStudentId.setText(getString(R.string.student_code_x,
                    FormatUtils.studentCodeFormat(student.getId())));
            tvGrade.setText(student.getCurrentGrade());
        });
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        reportViewModel.getStudentCost(studentId, year, month).observe(this, costs -> {
            Log.d(getClass().getSimpleName(), "学费回调>>>>>");
            setCostInfo(costs);
        });
        return root;
    }

    private void setCostInfo(List<StudentMonthCost> costs) {
        double totalCost = 0;
        if (costs != null && !costs.isEmpty()) {
            for (StudentMonthCost cost : costs) {
                totalCost = totalCost + cost.getDiscountPrice();
            }
            CostAdapter adapter = new CostAdapter(costs, false);
            adapter.setOnItemClickListener(this);
            rvCostDetail.setAdapter(adapter);
        } else {
            CostAdapter adapter = new CostAdapter(new ArrayList<>(), false);
            adapter.setOnItemClickListener(this);
            rvCostDetail.setAdapter(adapter);
        }
        tvTotalCost.setText(getString(R.string.rmb_x, FormatUtils.priceFormat(totalCost)));
        tvYearMonth.setText(getString(R.string.year_x_month_x, year, month));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.student_cost_detail_top_nav_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ok:
                showInput();
                break;
            case R.id.action_month:
                showMonthPicker();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        StudentMonthCost monthCost = (StudentMonthCost) adapter.getData().get(position);
        View dlgView = LayoutInflater.from(getContext()).inflate(R.layout.dlg_input_cost, null);
        EditText etName = dlgView.findViewById(R.id.et_name);
        etName.setText(monthCost.getCostName());
        etName.setEnabled(monthCost.getCostType() != 0);
        EditText etPrice = dlgView.findViewById(R.id.et_price);
        etPrice.setText(monthCost.getDiscountPrice() + "");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(R.string.update_cost)
                .setView(dlgView)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    if (!TextUtils.isEmpty(etName.getText()) && !TextUtils.isEmpty(etPrice.getText())) {
                        monthCost.setCostName(etName.getText().toString());
                        double price = Double.parseDouble(etPrice.getText().toString());
                        if (monthCost.getCostType() != 0) {
                            monthCost.setPrice(price);
                        }
                        monthCost.setDiscountPrice(price);
                        reportViewModel.update(monthCost);
                        Toast.makeText(getContext(), R.string.update_cost_success, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), TextUtils.isEmpty(etName.getText()) ? R.string.pls_input_cost_name
                                : R.string.pls_input_cost, Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }

    private void showInput() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dlg_input_cost, null);
        EditText etName = view.findViewById(R.id.et_name);
        EditText etPrice = view.findViewById(R.id.et_price);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(R.string.add_cost)
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
                        Toast.makeText(getActivity(), R.string.add_cost_success, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), TextUtils.isEmpty(etName.getText()) ? R.string.pls_input_cost_name
                                : R.string.pls_input_cost, Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }


    private void showMonthPicker() {
        View dlgView = LayoutInflater.from(getContext()).inflate(R.layout.dlg_day_picker, null);
        NumberPicker npYear = dlgView.findViewById(R.id.picker_year);
        NumberPicker npMonth = dlgView.findViewById(R.id.picker_month);
        Calendar cal = Calendar.getInstance();
        npMonth.setMinValue(1);
        npMonth.setMaxValue(12);
        npMonth.setValue(cal.get(Calendar.MONTH) + 1);
        npMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMonth.setWrapSelectorWheel(false);
        int year = cal.get(Calendar.YEAR);
        npYear.setMinValue(2019);
        npYear.setMaxValue(2099);
        npYear.setValue(year);
        npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npYear.setWrapSelectorWheel(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.other_month_cost)
                .setView(dlgView)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    this.year = npYear.getValue();
                    this.month = npMonth.getValue();
                    reportViewModel.getStudentCost(studentId, this.year, this.month);
                });
        builder.create().show();
    }
}