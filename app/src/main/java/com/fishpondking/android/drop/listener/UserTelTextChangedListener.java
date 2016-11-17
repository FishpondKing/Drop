package com.fishpondking.android.drop.listener;

import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;

import com.fishpondking.android.drop.utils.RegexUtils;

/**
 * Author: FishpondKing
 * Date: 2016/11/17:10:31
 * Email: song511653502@gmail.com
 * Description:
 */

public class UserTelTextChangedListener implements TextWatcher {

    private TextInputEditText mTextInputEditText;

    private String mError;

    public UserTelTextChangedListener(TextInputEditText textInputEditText,String error){
        mTextInputEditText = textInputEditText;
        mError = error;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (RegexUtils.isMobileExact(s) == false) {
            mTextInputEditText.setError(mError);
        }
    }
}
