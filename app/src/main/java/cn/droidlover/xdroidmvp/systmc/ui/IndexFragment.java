package cn.droidlover.xdroidmvp.systmc.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.systmc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends XFragment {


    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static IndexFragment newInstance() {
        return new IndexFragment();
    }
}
