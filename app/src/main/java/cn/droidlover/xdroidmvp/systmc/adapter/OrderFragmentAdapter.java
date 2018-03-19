package cn.droidlover.xdroidmvp.systmc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.KnifeKit;
import cn.droidlover.xdroidmvp.systmc.R;
import cn.droidlover.xdroidmvp.systmc.model.order.OrderModel;

/**
 * Created by haoxi on 2017/4/25.
 */

public class OrderFragmentAdapter extends SimpleRecAdapter<OrderModel.Order, OrderFragmentAdapter.ViewHolder> {
    public static final int TAG_VIEW = 0;


    public OrderFragmentAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_order_fragment;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OrderModel.Order item = data.get(position);
        holder.tv_shopName.setText(item.getShopCnName());
        holder.tv_checkIn.setText(item.getCheckInDate());
        holder.tv_checkOut.setText(item.getCheckOutDate());
        holder.tv_status.setText(item.getStatus());
        holder.tv_masterOrderNo.setText(item.getMasterOrderNo());
        holder.tv_price.setText(new BigDecimal(item.getTotalSalePrice()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_UP) +
                "元");
        holder.itemView.setTag(item.getPid());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRecItemClick() != null) {
                    getRecItemClick().onItemClick(position, item, TAG_VIEW, holder);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getRecItemClick().onItemLongClick(position, item, TAG_VIEW, holder);
                return false;
            }
        });
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_shopName)
        TextView tv_shopName;
        @BindView(R.id.tv_checkIn)
        TextView tv_checkIn;
        @BindView(R.id.tv_checkOut)
        TextView tv_checkOut;
        @BindView(R.id.tv_status)
        TextView tv_status;
        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.tv_masterOrderNo)
        TextView tv_masterOrderNo;

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }
}
