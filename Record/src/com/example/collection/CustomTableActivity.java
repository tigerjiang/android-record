package com.example.collection;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.collection.data.DBHelper;

public class CustomTableActivity extends Activity implements OnClickListener {
    private Button mAddBtn, mModifyBtn, mBackBtn, mDeleteBtn, mSaveBtn;
    private Context mContext;
    private CommonUtil util;
    private static final String TAG = CustomTableActivity.class.getSimpleName();
    private String[] mColumns;
    private String mCallActivityClassName;
    private StringBuffer columnStr;
    private EditText tempEdit;
    private int mDimenSize;
    private ResizeListView CustomListView;
    private ArrayList<Content> mData;
    private CustomAdapter mCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom);
        mContext = CustomTableActivity.this;
        mDimenSize = (int) mContext.getResources().getDimension(
                R.dimen.content_item_height);
        CustomListView = (ResizeListView) findViewById(R.id.custom_list);
        mData = new ArrayList<Content>();

        mAddBtn = (Button) findViewById(R.id.add_custom);
        mModifyBtn = (Button) findViewById(R.id.modify_custom);
        mBackBtn = (Button) findViewById(R.id.back_custom);
        // mDeleteBtn = (Button) findViewById(R.id.delete_custom);
        mSaveBtn = (Button) findViewById(R.id.save_custom);
        mSaveBtn.setOnClickListener(this);
        mAddBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        // mDeleteBtn.setOnClickListener(this);
        mModifyBtn.setOnClickListener(this);
        util = CommonUtil.getInstance(mContext);
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_custom:
                addCusomColumn();
                break;
            case R.id.modify_custom:
                setFocus(true);
                break;
            case R.id.save_custom:
                setFocus(false);
                saveColumns();
                Toast.makeText(mContext, "保存成功.", Toast.LENGTH_SHORT).show();
                // hideSoftInput();
                break;
            // case R.id.delete_custom:
            // deleteItem();
            // break;
            case R.id.back_custom:
                setResult(RESULT_OK);
                finish();
                break;
        }

    }

    private void initData() {
        if (getIntent() != null) {
            String column[] = getIntent().getStringArrayExtra("custom");
            for (int i = 0; i < column.length; i++) {
                mData.add(new Content(column[i]));
            }
        }
        mCustomAdapter = new CustomAdapter(mContext, mData);
        CustomListView.setAdapter(mCustomAdapter);
    }

    private void addCusomColumn() {
        mData.add(new Content(""));
        mCustomAdapter.notifyDataSetChanged();
        CustomListView.setSelection(CustomListView.getCount() - 1);

    }

    private void saveColumns() {
        CommonUtil.reStoreValueIntoSharePreferences(CommonUtil.CUSTOM_COLUMN,
                "");
        ArrayList<Content> tempList = new ArrayList<Content>();
        for (int i = 0,len = mData.size(); i < len; i++) {
            Log.d("item","index ="+i+"====="+mData.get(i).getContent());
            if (!TextUtils.isEmpty(mData.get(i).getContent())) {
                tempList.add(mData.get(i));
            }   
        }
        mData.clear();
        mData.addAll(tempList);
        mCustomAdapter.notifyDataSetChanged();
//         mCustomAdapter.notifyDataSetChanged();
        columnStr = new StringBuffer();
        String tablename = mData.get(0).getContent();
        for (int j = 0; j < mData.size(); j++) {
            String tempValue = mData.get(j).getContent();
            CommonUtil.reStoreValueIntoSharePreferences(tablename + "."
                    + tempValue, DBHelper.COLUMNS[j + 1]);
            Log.d(TAG, "tempValue" + tempValue + "column: "
                    + DBHelper.COLUMNS[j]);
            columnStr.append(tempValue).append("-");
        }
        columnStr.deleteCharAt(columnStr.lastIndexOf("-"));
        CommonUtil.reStoreValueIntoSharePreferences(CommonUtil.CUSTOM_COLUMN,
                columnStr.toString());
        setFocus(false);
    }

    private void setFocus(boolean isAvailable) {
        for (int i = 0; i < CustomListView.getChildCount(); i++) {
            ((ViewGroup) CustomListView.getChildAt(i)).getChildAt(0)
                    .setEnabled(isAvailable);
        }

        if (isAvailable) {
            CustomListView.requestFocus();
            ((ViewGroup) CustomListView.getChildAt(0)).getChildAt(0)
                    .requestFocus();
        } else {
            CustomListView.clearFocus();
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       if(keyCode == KeyEvent.KEYCODE_BACK){
           setResult(RESULT_OK);
           finish();
       }
        return super.onKeyDown(keyCode, event);
    }
}
