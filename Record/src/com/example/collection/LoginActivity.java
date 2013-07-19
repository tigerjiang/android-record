package com.example.collection;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.SumPathEffect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
    private EditText mUserNameEditText;
    private EditText mPsswdEditText;
    private TextView mModifyPwdTextView;
    private Button mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mUserNameEditText = (EditText) findViewById(R.id.username);
        mPsswdEditText = (EditText) findViewById(R.id.password);
        mModifyPwdTextView = (TextView) findViewById(R.id.modify_pwd_txt);
        mSubmitButton = (Button) findViewById(R.id.submit);
        mModifyPwdTextView.setOnClickListener(this);
        mSubmitButton.setOnClickListener(this);
        CommonUtil.getInstance(getApplicationContext());
        // 存储初始化密码
        if (TextUtils.isEmpty(CommonUtil
                .getValueFromSharePreferences(CommonUtil.USER_PASSWORD_KEY))) {
            CommonUtil.reStoreValueIntoSharePreferences(
                    CommonUtil.USER_PASSWORD_KEY, "000000");
        }
        mUserNameEditText.setText(CommonUtil
                .getValueFromSharePreferences(CommonUtil.USER_NAME_KEY));
    }

    private void submit() {
        if (!CommonUtil.getValueFromSharePreferences("isVerify").equals("1")) {
            CommonUtil.showVerifyDialog(LoginActivity.this);
            return;
        }
        if (TextUtils.isEmpty(mUserNameEditText.getText().toString())
                || TextUtils.isEmpty(mPsswdEditText.getText().toString())) {
            Toast.makeText(getApplicationContext(), "用户名或者密码不能为空!",
                    Toast.LENGTH_SHORT).show();
        } else {
            String password = mPsswdEditText.getText().toString();
            if (!password
                    .equals(CommonUtil
                            .getValueFromSharePreferences(CommonUtil.USER_PASSWORD_KEY))) {
                Toast.makeText(getApplicationContext(), "密码不正确,请重新输入!",
                        Toast.LENGTH_SHORT).show();
                return;
            } else {
                CommonUtil.reStoreValueIntoSharePreferences(
                        CommonUtil.USER_NAME_KEY, mUserNameEditText.getText()
                                .toString());
                CommonUtil.reStoreValueIntoSharePreferences(
                        CommonUtil.USER_PASSWORD_KEY, mPsswdEditText.getText()
                                .toString());
                startActivity(new Intent(this.getApplicationContext(),
                        TablesActivity.class));
            }
        }
        // startActivity(new Intent(this.getApplicationContext(),
        // TablesActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try{
            String newPassword = data.getExtras().getString(
                    CommonUtil.USER_PASSWORD_KEY);
            CommonUtil.reStoreValueIntoSharePreferences(
                    CommonUtil.USER_PASSWORD_KEY, newPassword);
            mPsswdEditText.setText("");
            String newUsername = data.getExtras().getString(
                    CommonUtil.USER_NAME_KEY);
            CommonUtil.reStoreValueIntoSharePreferences(
                    CommonUtil.USER_PASSWORD_KEY, newUsername);
        }catch (Exception e) {
            Log.e("result", e.toString());
        }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify_pwd_txt:
                startActivityForResult(new Intent(getApplicationContext(),
                        ModifyPasswordActivity.class), 0);
                break;

            case R.id.submit:
                submit();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
