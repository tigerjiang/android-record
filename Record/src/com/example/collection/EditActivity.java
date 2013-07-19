package com.example.collection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditActivity extends Activity implements OnClickListener {
    private Button mCancelBtn, mSaveBtn;
    private TextView mTableTxt, mColumnTxt;
    private EditText mContentTxt;
    private Context mContext;
    private String mContentStr;
    private boolean isSaved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        mContext = this.getApplicationContext();
        mCancelBtn = (Button) findViewById(R.id.cancel);
        mSaveBtn = (Button) findViewById(R.id.save);
        mTableTxt = (TextView) findViewById(R.id.table_name);
        mColumnTxt = (TextView) findViewById(R.id.column_name);
        mContentTxt = (EditText) findViewById(R.id.content_edit);
        if (getIntent() != null) {
            mContentStr = getIntent().getStringExtra("content");
            mContentTxt.setText(getIntent().getStringExtra("content"));
            mTableTxt.setText(getIntent().getStringExtra("table_name"));
            mColumnTxt.setText(getIntent().getStringExtra("column_name"));
        }
        mCancelBtn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                break;

            case R.id.save:
                if (!isSaved) {
                    Intent data = new Intent();
                    data.putExtra("content", mContentTxt.getText().toString());
                    isSaved = true;
                    setResult(RESULT_OK, data);
                }
                break;
        }
        finish();
    }

}