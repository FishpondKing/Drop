package com.fishpondking.android.drop.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fishpondking.android.drop.R;
import com.fishpondking.android.drop.listener.UserTelTextChangedListener;

/**
 * Author: FishpondKing
 * Date: 2016/11/15:10:26
 * Email: song511653502@gmail.com
 * Description:
 */

public class LoginFragment extends Fragment {

    private View mView;
    private TextInputEditText mTiEditTextTel;
    private TextInputEditText mTiEditTextPassword;
    private Button mButtonLogin;
    private TextView mTextViewForgetPassword;
    private ImageButton mImageButtonQQLogin;
    private ImageButton mImageButtonWeiboLogin;


    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login, container, false);

        mTiEditTextTel = (TextInputEditText) mView.findViewById(R.id.ti_edit_text_login_user_tel);
        mTiEditTextTel.addTextChangedListener(new UserTelTextChangedListener(mTiEditTextTel,
                getActivity().getResources().getString(R.string.user_tel_wrong)));

        mTiEditTextPassword =
                (TextInputEditText) mView.findViewById(R.id.ti_edit_text_login_user_password);

        mTextViewForgetPassword = (TextView) mView.findViewById(R.id.text_view_forget_password);

        mButtonLogin = (Button) mView.findViewById(R.id.button_login);

        mImageButtonQQLogin = (ImageButton) mView.findViewById(R.id.image_button_qq_login);

        mImageButtonWeiboLogin = (ImageButton) mView.findViewById(R.id.image_button_weibo_login);

        return mView;
    }
}
