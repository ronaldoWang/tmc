package cn.droidlover.xdroidmvp.systmc.model;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import cn.droidlover.xdroidmvp.net.IModel;

/**
 * Created by ThinkPad on 2017/10/22.
 */

public class VersionModel extends BaseModel<VersionModel.Version> implements IModel {

    @Table("T_VERSION")
    public static class Version {
        @PrimaryKey(AssignType.BY_MYSELF)
        private String id;
        @NotNull
        @Column("version")
        private Integer version;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
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
