package cn.droidlover.xdroidmvp.systmc.model.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.droidlover.xdroidmvp.net.IModel;
import cn.droidlover.xdroidmvp.systmc.model.BaseModel;

/**
 * Created by ThinkPad on 2017/11/12.
 */

public class OrderModel extends BaseModel<List<OrderModel.Order>> implements IModel {
    public static class Order implements Serializable {
        private String pid;
        private String masterOrderNo; // 主订单号
        private String subOrderNo; // 子订单号
        private String orderConfirmNo; // 订单确认号
        private String tmcName;// tmc名称
        private String shopCnName;// 门店名称
        private String shopContactDesc;// 门店联系方式
        private String brandName;// 品牌名称
        private String roomTypeDesc;// 房型名称
        private Integer roomNumber;// 房间数
        private String checkInDate;// 入住时间
        private String checkOutDate;// 离店时间
        private Integer totalSalePrice;// 价格
        private String status;// 状态

        private List<GuestModel.Guest> guests = new ArrayList<GuestModel.Guest>();

        public List<GuestModel.Guest> getGuests() {
            return guests;
        }

        public void setGuests(List<GuestModel.Guest> guests) {
            this.guests = guests;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getMasterOrderNo() {
            return masterOrderNo;
        }

        public void setMasterOrderNo(String masterOrderNo) {
            this.masterOrderNo = masterOrderNo;
        }

        public String getSubOrderNo() {
            return subOrderNo;
        }

        public void setSubOrderNo(String subOrderNo) {
            this.subOrderNo = subOrderNo;
        }

        public String getOrderConfirmNo() {
            return orderConfirmNo;
        }

        public void setOrderConfirmNo(String orderConfirmNo) {
            this.orderConfirmNo = orderConfirmNo;
        }

        public String getTmcName() {
            return tmcName;
        }

        public void setTmcName(String tmcName) {
            this.tmcName = tmcName;
        }

        public String getShopCnName() {
            return shopCnName;
        }

        public void setShopCnName(String shopCnName) {
            this.shopCnName = shopCnName;
        }

        public String getShopContactDesc() {
            return shopContactDesc;
        }

        public void setShopContactDesc(String shopContactDesc) {
            this.shopContactDesc = shopContactDesc;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getRoomTypeDesc() {
            return roomTypeDesc;
        }

        public void setRoomTypeDesc(String roomTypeDesc) {
            this.roomTypeDesc = roomTypeDesc;
        }

        public Integer getRoomNumber() {
            return roomNumber;
        }

        public void setRoomNumber(Integer roomNumber) {
            this.roomNumber = roomNumber;
        }

        public String getCheckInDate() {
            return checkInDate;
        }

        public void setCheckInDate(String checkInDate) {
            this.checkInDate = checkInDate;
        }

        public String getCheckOutDate() {
            return checkOutDate;
        }

        public void setCheckOutDate(String checkOutDate) {
            this.checkOutDate = checkOutDate;
        }

        public Integer getTotalSalePrice() {
            return totalSalePrice;
        }

        public void setTotalSalePrice(Integer totalSalePrice) {
            this.totalSalePrice = totalSalePrice;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isAuthError() {
        return false;
    }

    @Override
    public boolean isBizError() {
        return false;
    }

    @Override
    public String getErrorMsg() {
        return null;
    }
}

