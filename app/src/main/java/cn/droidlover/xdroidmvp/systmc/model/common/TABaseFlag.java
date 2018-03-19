package cn.droidlover.xdroidmvp.systmc.model.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ronaldo on 2017/9/5.
 */

public class TABaseFlag {
    public static TADataItem EMPTY = new TADataItem(0, "");
    private Map itemMap = new HashMap();
    private List<TADataItem> list = new ArrayList<TADataItem>();

    protected void addItem(TADataItem item) {
        itemMap.put(item.getId(), item);
        list.add(item);
    }

    public List<TADataItem> getListItems() {
        return list;
    }

    public TADataItem getDataItemById(String id) {
        TADataItem dataItem = null;
        try {
            dataItem = (TADataItem) itemMap.get(id);
        } catch (Exception e) {
        }
        return dataItem;
    }

    public String getValue(Integer id) {
        TADataItem dataItem = null;
        try {
            Object object = itemMap.get(id);
            if (object == null)
                return "";
            else
                dataItem = (TADataItem) object;
        } catch (Exception e) {
        }
        return dataItem.getValue();
    }
}
