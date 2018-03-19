package cn.droidlover.xdroidmvp.systmc.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.widget.Spinner;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import cn.droidlover.xdroidmvp.systmc.R;
import cn.droidlover.xdroidmvp.systmc.model.DevelopCustomerModel;
import cn.droidlover.xdroidmvp.systmc.model.sys.Dict;
import cn.droidlover.xdroidmvp.systmc.present.PDevelopCustomerForm;
import cn.droidlover.xdroidmvp.systmc.utils.DictUtil;
import cn.droidlover.xstatecontroller.XStateController;

public class DevelopCustomerFormEditActivity extends XActivity<PDevelopCustomerForm> {

    @BindView(R.id.edit_customer_customerName)
    EditText et_customerName;

    @BindView(R.id.edit_customer_mobilePhone)
    EditText et_mobilePhone;

    @BindView(R.id.edit_customer_summary)
    EditText et_summary;

    @BindView(R.id.edit_customer_email)
    EditText et_email;

    @BindView(R.id.edit_customer_recentDate)
    EditText et_recentDate;

    @BindView(R.id.edit_customer_recentResult)
    EditText et_recentResult;

    @BindView(R.id.btn_customer_save)
    TextView btn_save;

    @BindView(R.id.contentLayout)
    XStateController controller;

    @BindView(R.id.spinner_customer_sex)
    Spinner spinner_sex;

    @BindView(R.id.spinner_customer_type)
    Spinner spinner_type;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    DevelopCustomerModel.DevelopCustomer data;
    ArrayAdapter<Dict> sexAdapter;// 性别
    ArrayAdapter<Dict> typeAdapter;//客户类别

    Dialog.Builder builder = null;


    @Override
    public void initData(Bundle savedInstanceState) {
        controller.showLoading();
        String id = getIntent().getStringExtra("id");
        getP().queryOne(id);
    }

    @Override
    public void initView(Bundle bundle) {
        setSupportActionBar(toolbar);
        controller.loadingView(View.inflate(context, R.layout.view_loading, null));
        sexAdapter = new ArrayAdapter<Dict>(this,
                R.layout.simple_spinner_item,
                DictUtil.getDictList(context, "sex"));
        spinner_sex.setAdapter(sexAdapter);
        typeAdapter = new ArrayAdapter<Dict>(this,
                R.layout.simple_spinner_item,
                DictUtil.getDictList(context, "customer_type"));
        spinner_type.setAdapter(typeAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_developcustomerform;
    }

    @Override
    public PDevelopCustomerForm newP() {
        return new PDevelopCustomerForm();
    }

    @OnClick({R.id.btn_customer_save, R.id.edit_customer_recentDate})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.btn_customer_save:
                doSave();
                break;
            case R.id.edit_customer_recentDate:
                builder = new DatePickerDialog.Builder(R.style.Material_App_Dialog_TimePicker) {
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        DatePickerDialog dialog = (DatePickerDialog) fragment.getDialog();
                        String date = dialog.getFormattedDate(new SimpleDateFormat("yyyy-MM-dd"));
                        et_recentDate.setText(date);
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                        super.onNegativeActionClicked(fragment);
                    }
                };

                builder.positiveAction("OK")
                        .negativeAction("CANCEL");
                DialogFragment fragment = DialogFragment.newInstance(builder);
                fragment.show(getSupportFragmentManager(), "");
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Router.pop(context);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doSave() {
        data.setCustomerName(et_customerName.getText().toString());
        data.setMobilePhone(et_mobilePhone.getText().toString());
        data.setSex(((Dict) spinner_sex.getSelectedItem()).getValue());
        data.setSummary(et_summary.getText().toString());
        data.setType(((Dict) spinner_type.getSelectedItem()).getValue());
        data.setEmail(et_email.getText().toString());
        data.setRecentDate(et_recentDate.getText().toString());
        data.setRecentResult(et_recentResult.getText().toString());
        data.setSearch("");
        getP().save(data);
    }

    public void showData(DevelopCustomerModel.DevelopCustomer data) {
        this.data = data;
        et_customerName.setText(data.getCustomerName());
        et_mobilePhone.setText(data.getMobilePhone());
        spinner_sex.setSelection(DictUtil.getDictIndex(sexAdapter, data.getSex()));
        et_summary.setText(data.getSummary());
        spinner_type.setSelection(DictUtil.getDictIndex(typeAdapter, data.getType()));
        et_email.setText(data.getEmail());
        et_recentDate.setText(data.getRecentDate());
        et_recentResult.setText(data.getRecentResult());
        controller.showContent();
    }

}
