package net.chinancd.consgenius.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.chinancd.consgenius.R;

/**
 * Created by hedefu
 * on 2016 0411 at 16:41 .
 * email:hedefu999@gmail.com
 */
public class Accounts extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View act4view = inflater.inflate(R.layout.accounts_frag_layout, null);
        return act4view;
    }
}
