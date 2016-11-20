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
import com.fishpondking.android.drop.listener.GetValidateCodeListener;
import com.fishpondking.android.drop.listener.SigninListener;
import com.fishpondking.android.drop.listener.UserNameChangedListener;
import com.fishpondking.android.drop.listener.UserPasswordChangedListener;
import com.fishpondking.android.drop.listener.UserTelTextChangedListener;

/**
 * Author: FishpondKing
 * Date: 2016/11/15:10:26
 * Email: song511653502@gmail.com
 * Description:
 */

public class SigninFragment extends Fragment {

    private View mView;
    private TextInputEditText mTiEditTextTel;
    private TextInputEditText mTiEditTextName;
    private TextInputEditText mTiEditTextPassword;
    private TextInputEditText mTiEditTextPasswordCheck;
    private TextInputEditText mTiEditTextValidateCode;
    private Button mButtonGetValidateCode;
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
        mTiEditTextTel.addTextChangedListener(new UserTelTextChangedListener(mTiEditTextTel,
                getActivity().getResources().getString(R.string.user_tel_wrong)));

        mTiEditTextName =
                (TextInputEditText) mView.findViewById(R.id.ti_edit_text_signin_user_name);
        mTiEditTextName.addTextChangedListener(new UserNameChangedListener(mTiEditTextName,
                getActivity().getResources().getString(R.string.user_name_wrong)));

        mTiEditTextPassword =
                (TextInputEditText) mView.findViewById(R.id.ti_edit_text_signin_user_password);
        mTiEditTextPassword.addTextChangedListener(
                new UserPasswordChangedListener(mTiEditTextPassword, getActivity().getResources()
                        .getString(R.string.user_password_wrong)));

        mTiEditTextPasswordCheck =
                (TextInputEditText)
                        mView.findViewById(R.id.ti_edit_text_signin_user_password_check);

        mTiEditTextValidateCode =
                (TextInputEditText) mView.findViewById(R.id.ti_edit_text_signin_validate_code);

        mButtonSignin = (Button) mView.findViewById(R.id.button_signin);
        mButtonSignin.setOnClickListener(new SigninListener(getActivity(),
                mButtonSignin, mTiEditTextValidateCode));

        mButtonGetValidateCode = (Button) mView.findViewById(R.id.button_signin_get_validate_code);
        mButtonGetValidateCode.setOnClickListener(new GetValidateCodeListener(getActivity(),
                mButtonGetValidateCode, mTiEditTextTel, mTiEditTextName, mTiEditTextPassword,
                mTiEditTextPasswordCheck, mButtonSignin));

        return mView;
    }

}
