package com.fishpondking.android.drop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fishpondking.android.drop.R;

/**
 * Author: FishpondKing
 * Date: 2016/11/15:10:26
 * Email: song511653502@gmail.com
 * Description:
 */

public class SigninFragment extends Fragment {

    private View mView;
    private TextInputEditText mTiEditTextTel;
    private Button mButtonGetValidateCode;
    private TextInputEditText mTiEditTextValidateCode;
    private TextInputEditText mTiEditTextName;
    private TextInputEditText mTiEditTextPassword;
    private TextInputEditText mTiEditTextPasswordCheck;
    private Button mButtonSignin;

    public static SigninFragment newInstance() {
        SigninFragment signinFragment = new SigninFragment();
        return signinFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_signin, container, false);

        mTiEditTextTel = (TextInputEditText) mView.findViewById(R.id.ti_edit_text_signin_user_tel);

        mButtonGetValidateCode = (Button) mView.findViewById(R.id.button_signin_get_validate_code);

        mTiEditTextValidateCode =
                (TextInputEditText) mView.findViewById(R.id.ti_edit_text_signin_validate_code);

        mTiEditTextName =
                (TextInputEditText) mView.findViewById(R.id.ti_edit_text_signin_user_name);

        mTiEditTextName =
                (TextInputEditText) mView.findViewById(R.id.ti_edit_text_signin_user_password);

        mTiEditTextName =
                (TextInputEditText)
                        mView.findViewById(R.id.ti_edit_text_signin_user_password_check);

        mButtonSignin = (Button) mView.findViewById(R.id.button_signin);

        return mView;
    }
}
