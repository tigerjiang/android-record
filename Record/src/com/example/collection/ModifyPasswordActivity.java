package com.example.collection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ModifyPasswordActivity extends Activity implements OnClickListener {
    private Button mBackBtn;
    private Button mSubmitBtn;
    private EditText oldPwdEdit;
    private EditText newPwdEdit;
    private EditText confirmEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_password);
        mBackBtn = (Button) findViewById(R.id.back);
        mSubmitBtn = (Button) findViewById(R.id.submit);
        mBackBtn.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
        oldPwdEdit = (EditText) findViewById(R.id.old_pwd_edit);
        newPwdEdit = (EditText) findViewById(R.id.new_pwd_edit);
        confirmEdit = (EditText) findViewById(R.id.confirm_pwd_edit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                if (isRightInput()) {
                    Intent data = new Intent();
                    data.putExtra(CommonUtil.USER_PASSWORD_KEY, newPwdEdit
                            .getText().toString());
                    setResult(RESULT_OK, data);
                    Toast.makeText(getApplicationContext(), "密码修改成功!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }

    }

    private boolean isRightInput() {
        boolean isRight = false;
        if (!CommonUtil.getValueFromSharePreferences(
                CommonUtil.USER_PASSWORD_KEY).equals(
                oldPwdEdit.getText().toString())) {
            Toast.makeText(getApplicationContext(), "输入原密码不正确，请重新输入",
                    Toast.LENGTH_SHORT).show();
            isRight = false;
        } else if (TextUtils.isEmpty(newPwdEdit.getText().toString())) {
            Toast.makeText(getApplicationContext(), "新密码不能为空!",
                    Toast.LENGTH_SHORT).show();
            isRight = false;
        } else if (!newPwdEdit.getText().toString()
                .equals(confirmEdit.getText().toString())) {
            Toast.makeText(getApplicationContext(), "两次输入密码不一致!",
                    Toast.LENGTH_SHORT).show();
            isRight = false;
        } else {
            isRight = true;
        }
        return isRight;
    }

}
