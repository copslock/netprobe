/**
 * Copyright (c) 2014 <a href="userfuy@163.com">fu yong</a>. All right is reserved.
 *
 * @author: <a href="userfuy@163.com">fu yong</a>
 * @version: 1.0
 * @className:
 * @description:
 * @date:2014/9/5 18:47
 */
package com.fuyong.netprobe.ui.testsetting;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import org.dom4j.Element;

public class MyTextWatcher implements TextWatcher {
    private EditText mEditText;
    private Element mElement;

    public MyTextWatcher(EditText editText, Element element) {
        mEditText = editText;
        mElement = element;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mElement.setText(s.toString());
    }
}
