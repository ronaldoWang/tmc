package cn.droidlover.xdroidmvp.systmc.model;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import cn.droidlover.xdroidmvp.net.IModel;


/**
 * Created by ronaldo on 2017/4/22.
 */

public class UserModel extends BaseModel<UserModel.User> implements IModel {

    @Table("T_USER")
    public static class User {
        @PrimaryKey(AssignType.BY_MYSELF)
        private String id;
        @NotNull
        @Column("login_name")
        private String loginName;
        @NotNull
        @Column("pwd")
        private String pwd;
        @Column("create_date")
        private String createDate;
        @Column("task_org_id")
        private Integer taskOrgId;//班组id
        @Column("task_org_code")
        private String taskOrgCode;//班组code
        @Column("task_org_name")
        private String taskOrgName;//班组名称
        @Column("user_name")
        private String userName;//用户姓名
        @Column("post_name")
        private String postName;//岗位名称
        @Column("user_type")
        private String userType;//用户类型（1、检修，2、班组）
        @Column("person_id")
        private String personId;//用户类型（1、检修，2、班组）

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getTaskOrgCode() {
            return taskOrgCode;
        }

        public void setTaskOrgCode(String taskOrgCode) {
            this.taskOrgCode = taskOrgCode;
        }

        public String getTaskOrgName() {
            return taskOrgName;
        }

        public void setTaskOrgName(String taskOrgName) {
            this.taskOrgName = taskOrgName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPostName() {
            return postName;
        }

        public void setPostName(String postName) {
            this.postName = postName;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public Integer getTaskOrgId() {
            return taskOrgId;
        }

        public void setTaskOrgId(Integer taskOrgId) {
            this.taskOrgId = taskOrgId;
        }

        public String getPersonId() {
            return personId;
        }

        public void setPersonId(String personId) {
            this.personId = personId;
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
