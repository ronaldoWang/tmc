package cn.droidlover.xdroidmvp.systmc.model.order;

import java.io.Serializable;
import java.util.List;

import cn.droidlover.xdroidmvp.net.IModel;
import cn.droidlover.xdroidmvp.systmc.model.BaseModel;

/**
 * Created by ThinkPad on 2017/11/12.
 */

public class GuestModel extends BaseModel<List<GuestModel.Guest>> implements IModel {
    public static class Guest implements Serializable {
        private String nameTitle; // name_title
        private String firstName; // first_name
        private String lastName; // last_name
        private String gender; // 性别
        private String mobilePhone; // mobile_phone
        private String email; // email

        public String getNameTitle() {
            return nameTitle;
        }

        public void setNameTitle(String nameTitle) {
            this.nameTitle = nameTitle;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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

