package com.jwong.education.ui.student;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jwong.education.R;
import com.jwong.education.dto.CostNotificationDTO;
import com.jwong.education.dto.CostNotificationItemDTO;
import com.jwong.education.util.FormatUtils;
import com.jwong.education.util.PrefUtils;
import com.jwong.education.util.ScreenShotUtils;
import com.jwong.education.widget.SealView;

import java.util.Calendar;
import java.util.Date;

public class StudentCostNotificationActivity extends AppCompatActivity implements OnItemClickListener, View.OnClickListener {

    private CostNotificationDTO costNotificationDTO;
    private TextView tvTitle, tvStudentName, tvStudentName2, tvGrade, tvCostName, tvDate;
    private RecyclerView rvCost;
    private View footerView;
    private TextView tvAllTotal;
    private SealView sealView;
    private final static int REQUEST_WRITE = 1300;


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
        tvTitle.setOnClickListener(this);
        tvStudentName = findViewById(R.id.tv_name);
        tvStudentName.setOnClickListener(this);
        tvStudentName2 = findViewById(R.id.tv_name2);
        tvStudentName2.setOnClickListener(this);
        tvGrade = findViewById(R.id.tv_grade);
        tvGrade.setOnClickListener(this);
        tvCostName = findViewById(R.id.tv_cost_name);
        tvCostName.setOnClickListener(this);

        rvCost = findViewById(R.id.rv_cost);
        rvCost.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCost.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        sealView = findViewById(R.id.seal);
        sealView.setOnClickListener(this);
        tvDate = findViewById(R.id.tv_date);
        tvDate.setText(FormatUtils.convert2Date(Calendar.getInstance().getTime()));
        tvDate.setOnClickListener(this);
        setData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_nav_menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createImage();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (Build.VERSION.SDK_INT >= 23) {
                    //判断是否有这个权限
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //第一请求权限被取消显示的判断，一般可以不写
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Log.i(getClass().getSimpleName(), "我们需要这个权限给你提供存储服务");
                            showAlert();
                        } else {
                            //2、申请权限: 参数二：权限的数组；参数三：请求码
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
                        }
                    } else {
                        createImage();
                    }
                } else {
                    createImage();
                }
                break;
            case android.R.id.home:// 点击返回图标事件
                this.finish();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("申请权限")
                .setMessage("请授予权限方可保存通知单到相册")
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) ->
                        Toast.makeText(getApplicationContext(), "无权限保存通知单到相册",
                                Toast.LENGTH_SHORT).show()
                )
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) ->
                        ActivityCompat.requestPermissions(StudentCostNotificationActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE)
                );
        builder.create().show();
    }

    private void createImage() {
        String path = ScreenShotUtils.viewConversionBitmap(findViewById(R.id.rootView), costNotificationDTO.getStudentName());
        if (!TextUtils.isEmpty(path)) {
            Toast.makeText(getApplicationContext(), "通知单已保存到：" + path, Toast.LENGTH_LONG).show();
//            Uri uri = Uri.fromFile(new File(path));
//            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            ContentResolver cr = getContentResolver();
            insertImageT(cr, getString(R.string.company),
                    costNotificationDTO.getStudentName() + tvTitle.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "保存通知单失败了！", Toast.LENGTH_LONG).show();
        }
    }

    private void setData() {
        tvTitle.setText(PrefUtils.getPrefKeyNotificationTitle(getApplicationContext(),
                getString(R.string.company_pay_notification, getString(R.string.company))));
        sealView.setTextFirst(PrefUtils.getSealNameCn(getApplicationContext(), getString(R.string.company)));
        sealView.setTextSecond(PrefUtils.getSealNameEn(getApplicationContext(), getString(R.string.company_en)));

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
            case R.id.tv_title_cost:
                View viewInput = LayoutInflater.from(this).inflate(R.layout.dlg_single_input, null);
                TextView tvName = viewInput.findViewById(R.id.tv_name);
                tvName.setText(R.string.title);
                EditText etName = viewInput.findViewById(R.id.et_name);
                etName.setHint(R.string.pls_input_cost_title);
                etName.setText(tvTitle.getText());
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.warm_tip)
                        .setView(viewInput)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                                    PrefUtils.setPrefKeyNotificationTitle(getApplicationContext(), etName.getText().toString());
                                    tvTitle.setText(PrefUtils.getPrefKeyNotificationTitle(getApplicationContext(),
                                            getString(R.string.company_pay_notification, getString(R.string.company))));
                                }
                        );
                builder.create().show();
                break;

            case R.id.tv_name:
            case R.id.tv_name2:
                viewInput = LayoutInflater.from(this).inflate(R.layout.dlg_single_input, null);
                tvName = viewInput.findViewById(R.id.tv_name);
                tvName.setText(R.string.student_name);
                etName = viewInput.findViewById(R.id.et_name);
                etName.setHint(R.string.pls_input_student_name);
                etName.setText(costNotificationDTO.getStudentName());
                builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.warm_tip)
                        .setView(viewInput)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                            costNotificationDTO.setStudentName(etName.getText().toString());
                            tvStudentName.setText(Html.fromHtml("<u>&nbsp;&nbsp;"
                                    + costNotificationDTO.getStudentName() + "&nbsp;&nbsp;</u>"));
                            tvStudentName2.setText(Html.fromHtml("<u>&nbsp;&nbsp;"
                                    + costNotificationDTO.getStudentName() + "&nbsp;&nbsp;</u>"));
                        });
                builder.create().show();
                break;
            case R.id.tv_grade:
                viewInput = LayoutInflater.from(this).inflate(R.layout.dlg_single_input, null);
                tvName = viewInput.findViewById(R.id.tv_name);
                tvName.setText(R.string.grade);
                etName = viewInput.findViewById(R.id.et_name);
                etName.setHint(R.string.pls_input_grade);
                etName.setText(costNotificationDTO.getGrade());
                builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.warm_tip)
                        .setView(viewInput)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                            costNotificationDTO.setGrade(etName.getText().toString());
                            tvGrade.setText(Html.fromHtml("<u>&nbsp;&nbsp;" + costNotificationDTO.getGrade()
                                    + "&nbsp;&nbsp;</u>"));

                        });
                builder.create().show();
                break;
            case R.id.tv_cost_name:
                viewInput = LayoutInflater.from(this).inflate(R.layout.dlg_single_input, null);
                tvName = viewInput.findViewById(R.id.tv_name);
                tvName.setText(R.string.cost_name);
                etName = viewInput.findViewById(R.id.et_name);
                etName.setHint(R.string.pls_input_cost_project);
                etName.setText(costNotificationDTO.getCostName());
                builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.warm_tip)
                        .setView(viewInput)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                            costNotificationDTO.setCostName(etName.getText().toString());
                            tvCostName.setText(Html.fromHtml("<u>&nbsp;&nbsp;" + costNotificationDTO.getCostName() + "&nbsp;&nbsp;</u>"));
                        });
                builder.create().show();
                break;
            case R.id.tv_date:
                Calendar calendar = Calendar.getInstance();
                if (!TextUtils.isEmpty(tvDate.getText())) {
                    Date date = FormatUtils.convert2Date(tvDate.getText().toString());
                    if (date != null) {
                        calendar.setTime(date);
                    }
                }
                DatePickerDialog dpd = new DatePickerDialog(this,
                        DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        (DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) -> {
                            tvDate.setText(getString(R.string.x_year_x_month_x_day, year, monthOfYear + 1, dayOfMonth));
                        }
                        , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                break;
            case R.id.seal:
                viewInput = LayoutInflater.from(this).inflate(R.layout.dlg_seal_name_setting, null);
                etName = viewInput.findViewById(R.id.et_name);
                etName.setText(PrefUtils.getSealNameCn(getApplicationContext(), getString(R.string.company)));
                EditText etNameEn = viewInput.findViewById(R.id.et_name_en);
                etNameEn.setText(PrefUtils.getSealNameEn(getApplicationContext(), getString(R.string.company_en)));
                builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.warm_tip)
                        .setView(viewInput)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                            PrefUtils.setSealNameCn(getApplicationContext(), etName.getText().toString());
                            PrefUtils.setSealNameEn(getApplicationContext(), etNameEn.getText().toString());
                            sealView.setTextFirst(PrefUtils.getSealNameCn(getApplicationContext(), getString(R.string.company)));
                            sealView.setTextSecond(PrefUtils.getSealNameEn(getApplicationContext(), getString(R.string.company_en)));
                        });
                builder.create().show();
                break;
            default:
                break;
        }
    }


    public String insertImageT(ContentResolver cr, String title, String description) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri url = null;
        String stringUrl = null; /* value to be returned */
        try {
            String CONTENT_AUTHORITY_SLASH = "content://" + "media" + "/";
            Uri uri = Uri.parse(CONTENT_AUTHORITY_SLASH + "external" + "/images/media");
            url = cr.insert(uri, values);
        } catch (Exception e) {
            Log.e("insertImageT", "Failed to insert image", e);
        }
        if (url != null) {
            stringUrl = url.toString();
        }
        return stringUrl;
    }
}