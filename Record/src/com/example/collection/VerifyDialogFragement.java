package com.example.collection;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VerifyDialogFragement extends DialogFragment implements
        OnClickListener {

    private View mRootView;
    private Context mContext;
    private Button confirmBtn, cancelBtn;
    private TextView imeiShow;
    private EditText veryCodeEdit;
    private String veryCode;

    public static VerifyDialogFragement newInstance() {
        return new VerifyDialogFragement();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.verify_dialog, null);
        initView(mRootView);
        initData();
        return mRootView;
    }

    private void initView(View rootView) {
        cancelBtn = (Button) rootView.findViewById(R.id.cancel_verify);
        confirmBtn = (Button) rootView.findViewById(R.id.verify);
        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        imeiShow = (TextView) rootView.findViewById(R.id.imei_value);
        veryCodeEdit = (EditText) rootView.findViewById(R.id.code_value);
    }

    private void initData() {
        Bundle bundle = this.getArguments();
        imeiShow.setText(bundle.getString("imei", "0"));
        veryCode = bundle.getString("code");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify:
                if (veryCode.equals(veryCodeEdit.getText().toString())) {
                    CommonUtil.getInstance(mContext)
                            .reStoreValueIntoSharePreferences("isVerify", "1");
                    Toast.makeText(mContext, "验证成功.", Toast.LENGTH_SHORT)
                            .show();
                    this.dismiss();
                } else {
                    CommonUtil.getInstance(mContext)
                            .reStoreValueIntoSharePreferences("isVerify", "0");
                    Toast.makeText(mContext, "验证失败.", Toast.LENGTH_SHORT)
                            .show();
                }
                break;

            default:
                this.dismiss();
                break;
        }

    }

}
