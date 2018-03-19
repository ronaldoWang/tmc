package cn.droidlover.xdroidmvp.systmc.model;

/**
 * Created by ronaldo on 2017/4/24.
 */

public class BaseModel<T> {
    protected String message;
    protected boolean success;
    protected T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
