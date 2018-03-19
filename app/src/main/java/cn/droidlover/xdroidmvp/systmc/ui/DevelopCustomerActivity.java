package cn.droidlover.xdroidmvp.systmc.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import cn.droidlover.xdroidmvp.systmc.R;
import cn.droidlover.xdroidmvp.systmc.adapter.DevelopCustomerFragmentAdapter;
import cn.droidlover.xdroidmvp.systmc.model.DevelopCustomerModel;
import cn.droidlover.xdroidmvp.systmc.model.common.Constent;
import cn.droidlover.xdroidmvp.systmc.present.PDevelopCustomer1;
import cn.droidlover.xdroidmvp.systmc.widget.XCSlideView;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;

public class DevelopCustomerActivity extends XActivity<PDevelopCustomer1> {

    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    EditText et_customerName;
    EditText et_mobilePhone;

    Map<String, Object> conditionMap = new HashMap<>();

    DevelopCustomerFragmentAdapter adapter;
    XCSlideView mSlideViewLeft;//搜索侧滑框

    @Override
    public void initView(Bundle bundle) {
        int mScreenWidth = ScreenUtils.getScreenWidth();
        //加载侧滑界面
        View menuViewLeft = LayoutInflater.from(context).inflate(R.layout.layout_slideview, null);
        mSlideViewLeft = XCSlideView.create(this, XCSlideView.Positon.LEFT);
        mSlideViewLeft.setMenuView(this, menuViewLeft);
        mSlideViewLeft.setMenuWidth(mScreenWidth * 7 / 9);
        et_customerName = ButterKnife.findById(menuViewLeft, R.id.edit_customer_customerName);
        et_mobilePhone = ButterKnife.findById(menuViewLeft, R.id.edit_customer_mobilePhone);


        BootstrapButton btn_search = ButterKnife.findById(menuViewLeft, R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });
        BootstrapButton btn_reset = ButterKnife.findById(menuViewLeft, R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doReset();
            }
        });

        //加载Toolbar
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search:
                        if (!mSlideViewLeft.isShow())
                            mSlideViewLeft.show();
                        else
                            mSlideViewLeft.dismiss();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initAdapter();
        loadData(1);
    }

    /**
     * 获得adapter
     *
     * @return
     */
    public SimpleRecAdapter getAdapter() {
        if (adapter == null) {
            adapter = new DevelopCustomerFragmentAdapter(context);
            adapter.setRecItemClick(new RecyclerItemCallback<DevelopCustomerModel.DevelopCustomer, DevelopCustomerFragmentAdapter.ViewHolder>() {
                @Override
                public void onItemClick(int position, DevelopCustomerModel.DevelopCustomer model, int tag, DevelopCustomerFragmentAdapter.ViewHolder holder) {
                    super.onItemClick(position, model, tag, holder);
                    String id = model.getId();
                    Router.newIntent(context).to(DevelopCustomerFormViewActivity.class).putString("id", id).launch();
                }

                @Override
                public void onItemLongClick(int position, final DevelopCustomerModel.DevelopCustomer model, int tag, DevelopCustomerFragmentAdapter.ViewHolder holder) {
                    super.onItemLongClick(position, model, tag, holder);
                    Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialog) {
                        @Override
                        public void onPositiveActionClicked(DialogFragment fragment) {
                            super.onPositiveActionClicked(fragment);
                            if (Constent.ONLINE) {
                                getP().delete(model.getCustomerNo());
                            } else {
                                getP().deleteNativeData(model.getId());
                            }
                        }

                        @Override
                        public void onNegativeActionClicked(DialogFragment fragment) {
                            super.onNegativeActionClicked(fragment);
                        }
                    };
                    ((SimpleDialog.Builder) builder).message(getResources().getString(R.string.comfirm_delete))
                            .positiveAction(getResources().getString(R.string.ok))
                            .negativeAction(getResources().getString(R.string.cancel));
                    DialogFragment fragment = DialogFragment.newInstance(builder);
                    fragment.show(getSupportFragmentManager(), null);
                }
            });

        }
        return adapter;
    }

    /**
     * 初始化Adapter
     */
    private void initAdapter() {
        contentLayout.getRecyclerView().verticalLayoutManager(context);
        contentLayout.getRecyclerView().setAdapter(getAdapter());
        contentLayout.getRecyclerView()
                .setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
                    @Override
                    public void onRefresh() {
                        loadData(1);
                    }

                    @Override
                    public void onLoadMore(int page) {
                        loadData(page);
                    }
                });
        contentLayout.loadingView(View.inflate(context, R.layout.view_loading, null));
        contentLayout.getRecyclerView().useDefLoadMoreView();
    }

    /**
     * 展示数据
     *
     * @param page 页码
     * @param data 数据
     */
    public void showData(int page, List<DevelopCustomerModel.DevelopCustomer> data) {
        if (page > 1) {
            getAdapter().addData(data);
        } else {
            getAdapter().setData(data);
        }

        if (null != data && !data.isEmpty() && data.size() == 10) {
            contentLayout.getRecyclerView().setPage(page, page + 1);
        } else {
            contentLayout.getRecyclerView().setPage(page, page);
        }

        if (getAdapter().getItemCount() < 1) {
            contentLayout.showEmpty();
            return;
        }
    }

    public void loadData(final Integer page) {
        if (Constent.ONLINE) {
            getP().loadData(page, conditionMap);
        } else {
            getP().loadNativeData(page, conditionMap);
        }
    }

    /**
     * 重置
     */
    private void doReset() {
        mSlideViewLeft.dismiss();
        conditionMap.clear();
        et_customerName.getText().clear();
        et_mobilePhone.getText().clear();
    }

    /**
     * 查询
     */
    private void doSearch() {
        mSlideViewLeft.dismiss();
        if (!StringUtils.isTrimEmpty(et_customerName.getText().toString())) {
            conditionMap.put("searchcondition_customerName_string_like", et_customerName.getText().toString());
        }
        if (!StringUtils.isTrimEmpty(et_mobilePhone.getText().toString())) {
            conditionMap.put("searchcondition_mobilePhone_string_like", et_mobilePhone.getText().toString());
        }
        loadData(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Router.pop(context);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_developcustomer;
    }

    @Override
    public PDevelopCustomer1 newP() {
        return new PDevelopCustomer1();
    }
}
