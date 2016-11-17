package com.fishpondking.android.drop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SignUpCallback;
import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.engine.SingletonUser;
import com.fishpondking.android.drop.listener.GetValidateCodeListener;
import com.fishpondking.android.drop.listener.UserTelTextChangedListener;

/**
 * Author: FishpondKing
 * Date: 2016/11/15:10:26
 * Email: song511653502@gmail.com
 * Description:
 */

public class SigninFragment extends Fragment implements View.OnClickListener {

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
        mTiEditTextTel.addTextChangedListener(new UserTelTextChangedListener(mTiEditTextTel,
                getActivity().getResources().getString(R.string.user_tel_wrong)));

        mButtonGetValidateCode = (Button) mView.findViewById(R.id.button_signin_get_validate_code);
        mButtonGetValidateCode.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_signin_get_validate_code:
                Log.d("SMS","验证码");
                Toast.makeText(getActivity(),"hahaha",Toast.LENGTH_SHORT).show();
                //获取验证码，必须同时具有用户名，密码，手机号
                SingletonUser.getInstance().setUsername("hahaha");
                SingletonUser.getInstance().setMobilePhoneNumber(mTiEditTextTel.getText().toString());
                SingletonUser.getInstance().setPassword("123456");
                SingletonUser.getInstance().signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            // successfully
                            Toast.makeText(getActivity(),"验证码获取成功",Toast.LENGTH_SHORT).show();
                            Log.d("SMS","验证码获取成功");
                        } else {
                            // failed
                            Log.d("SMS","验证码获取失败");
                            Toast.makeText(getActivity(),"验证码获取失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    }
}
