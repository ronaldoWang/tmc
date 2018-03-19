/**
 *
 */
package cn.droidlover.xdroidmvp.systmc.model.common;

/**
 * PDA/2013-9-26
 *
 * @author wanghx
 * @description 下拉列表键值对
 */
public class TADataItem {
    private Integer id;
    private String code = "";
    private String value = "";
    private String title = "";

    public TADataItem() {
        id = -1;
        value = "";
        title = "";
        code = "";
    }

    public TADataItem(Integer _ID, String _Value) {
        id = _ID;
        value = _Value;
    }

    public TADataItem(String _Code, String _Value) {
        code = _Code;
        value = _Value;
    }

    public TADataItem(Integer _ID, String _Value, String _Title) {
        id = _ID;
        value = _Value;
        title = _Title;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
