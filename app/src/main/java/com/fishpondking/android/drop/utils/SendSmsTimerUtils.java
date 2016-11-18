package com.fishpondking.android.drop.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Author: FishpondKing
 * Date: 2016/11/18:10:54
 * Email: song511653502@gmail.com
 * Description:
 */

public class SendSmsTimerUtils extends CountDownTimer {

    private int inFuture;
    private int downInterval;
    private TextView mTextView;

    public SendSmsTimerUtils(TextView textview, long millisInFuture, long countDownInterval,
                             int inFuture, int downInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textview;
        this.inFuture=inFuture;
        this.downInterval=downInterval;
    }

    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false);
        mTextView.setText(millisUntilFinished / 1000 + "秒后可重新发送");
        mTextView.setBackgroundResource(downInterval);

        SpannableString spannableString = new SpannableString(mTextView.getText().toString());
        ForegroundColorSpan span = new ForegroundColorSpan(Color.GRAY);
        //设置秒数的颜色
        if (millisUntilFinished/1000 > 9) {
            spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        } else {
            spannableString.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        mTextView.setText(spannableString);
    }

    @Override
    public void onFinish() {
        mTextView.setText("重新获取验证码");
        mTextView.setClickable(true);
        mTextView.setBackgroundResource(inFuture);
    }
}
