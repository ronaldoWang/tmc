package cn.droidlover.xdroidmvp.systmc.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.ScreenUtils;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.router.Router;
import cn.droidlover.xdroidmvp.systmc.R;
import cn.droidlover.xdroidmvp.systmc.adapter.DevelopCustomerFragmentAdapter;
import cn.droidlover.xdroidmvp.systmc.model.DevelopCustomerModel;
import cn.droidlover.xdroidmvp.systmc.model.common.Constent;
import cn.droidlover.xdroidmvp.systmc.present.PDevelopCustomer;
import cn.droidlover.xdroidmvp.systmc.widget.XCSlideView;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * Created by haoxi on 2017/4/25.
 */

public class DevelopCustomerFragment extends XFragment<PDevelopCustomer> {
    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    DevelopCustomerFragmentAdapter adapter;

    XCSlideView mSlideViewLeft;//搜索侧滑框
    int mScreenWidth = 0;//屏幕宽度

    Map<String, Object> conditionMap = new HashMap<>();

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
                            getP().delete(model.getCustomerNo());
                        }

                        @Override
                        public void onNegativeActionClicked(DialogFragment fragment) {
                            super.onNegativeActionClicked(fragment);
                        }
                    };
                    ((SimpleDialog.Builder) builder).message("是否删除?")
                            .positiveAction("确认")
                            .negativeAction("取消");
                    DialogFragment fragment = DialogFragment.newInstance(builder);
                    fragment.show(getFragmentManager(), null);
                }
            });

        }
        return adapter;
    }

    /**
     * 初始化Adapter
     */
    private void initAdapter() {
        setLayoutManager(contentLayout.getRecyclerView());
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
        contentLayout.loadingView(View.inflate(getContext(), R.layout.view_loading, null));
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

    @Override
    public void initData(Bundle savedInstanceState) {
        loadData(1);
    }

    @Override
    public void initView(Bundle bundle) {
        mScreenWidth = ScreenUtils.getScreenWidth();
        View menuViewLeft = LayoutInflater.from(context).inflate(R.layout.layout_slideview, null);
        mSlideViewLeft = XCSlideView.create(this.getActivity(), XCSlideView.Positon.RIGHT);
        mSlideViewLeft.setMenuView(this.getActivity(), menuViewLeft);
        mSlideViewLeft.setMenuWidth(mScreenWidth * 7 / 9);

        //加载Toolbar
        setHasOptionsMenu(true);
        //StatusBarCompat.translucentStatusBar(getActivity());
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
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

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerToggle.syncState();//初始化状态
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                String string = null;
                switch (id) {
                    case R.id.menu_sbzl:
                        string = "我";
                        break;
                }
                return false;
            }
        });

        initAdapter();
    }

    public void loadData(final Integer page) {
        if (Constent.ONLINE) {
            getP().loadData(page, conditionMap);
        } else {
            getP().loadNativeData(page);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public PDevelopCustomer newP() {
        return new PDevelopCustomer();
    }

    public static DevelopCustomerFragment newInstance() {
        return new DevelopCustomerFragment();
    }
}
