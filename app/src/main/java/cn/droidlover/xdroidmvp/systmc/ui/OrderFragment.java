package cn.droidlover.xdroidmvp.systmc.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import cn.droidlover.xdroidmvp.router.Router;
import cn.droidlover.xdroidmvp.systmc.R;
import cn.droidlover.xdroidmvp.systmc.adapter.OrderFragmentAdapter;
import cn.droidlover.xdroidmvp.systmc.model.common.Constent;
import cn.droidlover.xdroidmvp.systmc.model.order.OrderModel;
import cn.droidlover.xdroidmvp.systmc.net.Api;
import cn.droidlover.xdroidmvp.systmc.widget.LoadingDialog;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * Created by ronaldo on 2017/6/6.
 */

public class OrderFragment extends XFragment {

    @BindView(R.id.contentLayout)
    XRecyclerContentLayout contentLayout;
    @BindView(R.id.order_search_v_name)
    EditText order_search_v_name;
    @BindView(R.id.search_btn)
    ImageView search_btn;

    Map<String, Object> conditionMap = new HashMap<>();
    OrderFragmentAdapter adapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        loadData(1, conditionMap);
    }

    @OnClick({R.id.search_btn})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.search_btn:
                String shopName = order_search_v_name.getText().toString();
                if (!StringUtils.isTrimEmpty(shopName)) {
                    LoadingDialog.showDialogForLoading(context);
                    conditionMap.put("mgtShop.shopCnName", shopName);
                    loadData(1, conditionMap);
                }

                break;
            default:
                break;
        }
    }

    private void loadData(final int page) {
        loadData(page, conditionMap);
    }

    public void loadData(final int page, final Map<String, Object> conditionMap) {
        //LoadingDialog.showDialogForLoading(getActivity());
        Api.getOrderService().query(page, conditionMap)
                .compose(XApi.<OrderModel>getApiTransformer())
                .compose(XApi.<OrderModel>getScheduler())
                .compose(this.<OrderModel>bindToLifecycle())
                .subscribe(new ApiSubscriber<OrderModel>() {
                    @Override
                    protected void onFail(NetError error) {
                        LoadingDialog.cancelDialogForLoading();
                        ToastUtils.showShort(error.getMessage());
                    }

                    @Override
                    public void onNext(OrderModel orderModel) {
                        LoadingDialog.cancelDialogForLoading();
                        if (orderModel.isSuccess()) {
                            showData(page, orderModel.getData());
                        } else {
                            ToastUtils.showShort(orderModel.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        LoadingDialog.cancelDialogForLoading();
                    }
                });
    }

    private void showData(int page, List<OrderModel.Order> data) {
        if (page > 1) {
            getAdapter().addData(data);
        } else {
            getAdapter().setData(data);
        }

        if (null != data && !data.isEmpty() && data.size() == 20) {
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
    public void initView(Bundle savedInstanceState) {
        //BusProvider.getBus().toFlowable(RepairTaskEvent.class)
        //        .subscribe(new Consumer<RepairTaskEvent>() {
        //            @Override
        //            public void accept(RepairTaskEvent repairTaskEvent) throws Exception {
        //                if (repairTaskEvent.getTag().equals(RepairTaskEvent.freshOverhaulList)) {
        //                    //执行刷新
        //                    loadData(1, conditionMap);
        //                }
        //            }
        //        });
        initAdapter();
    }

    /**
     * 获得adapter
     *
     * @return
     */
    public SimpleRecAdapter getAdapter() {
        if (adapter == null) {
            adapter = new OrderFragmentAdapter(context);
            adapter.setRecItemClick(new RecyclerItemCallback<OrderModel.Order, OrderFragmentAdapter.ViewHolder>() {
                @Override
                public void onItemClick(int position, OrderModel.Order model, int tag, OrderFragmentAdapter.ViewHolder holder) {
                    super.onItemClick(position, model, tag, holder);
                    Router.newIntent(context).to(OrderFormViewActivity.class).putString("id", model.getPid()).launch();
                }

                @Override
                public void onItemLongClick(int position, final OrderModel.Order model, int tag, OrderFragmentAdapter.ViewHolder holder) {

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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static OrderFragment newInstance() {
        return new OrderFragment();
    }
}
