package com.fuyong.netprobe.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuyong.netprobe.R;

/**
 * Created by f00151473 on 2014/5/20.
 */
public class NewsPager extends Fragment {
    public NewsPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_view_pager, container, false);
        ((TextView) rootView.findViewById(R.id.textView)).setText("News Pager");
        return rootView;
    }
}
