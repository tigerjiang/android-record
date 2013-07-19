package com.example.collection;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.example.collection.data.DBHelper;

public class CustomTableActivity extends Activity implements OnClickListener {
    private LinearLayout mColumnLayout;
    private Button mAddBtn, mModifyBtn, mBackBtn, mDeleteBtn, mSaveBtn;
    private Context mContext;
    private CommonUtil util;
    private static final String TAG = CustomTableActivity.class.getSimpleName();
    private String[] mColumns;
    private String mCallActivityClassName;
    private StringBuffer columnStr;
    private EditText tempEdit;
    private int mDimenSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom);
        mContext = CustomTableActivity.this;
        mDimenSize = (int) mContext.getResources().getDimension(
                R.dimen.content_item_height);
        mColumnLayout = (LinearLayout) findViewById(R.id.column_layout);
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
                CommonUtil.hideSoftKeyboard(this);
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
                if (i == 0) {
                    addColumn(column[i], false);
                } else {
                    addColumn(column[i], true);
                }
            }
            setFocus(false);
        }
    }

    private void addCusomColumn() {
        addColumn("", true);
        // EditText tv = new EditText(mContext);
        // tv.setTextColor(Color.BLACK);
        // tv.setSingleLine(true);
        // tv.setOnFocusChangeListener(new OnFocusChangeListener() {
        //
        // @Override
        // public void onFocusChange(View v, boolean hasFocus) {
        // if (hasFocus) {
        // tempEdit = (EditText) v;
        // } else {
        // if (TextUtils.isEmpty(((EditText) v).getText().toString())) {
        // mColumnLayout.removeView(v);
        // }
        // }
        // }
        // });
        // tv.requestFocus();
        // mColumnLayout.addView(tv, LinearLayout.LayoutParams.MATCH_PARENT,
        // mDimenSize);
        // // mColumnLayout.setOnFocusChangeListener(new OnFocusChangeListener()
        // {
        // //
        // // @Override
        // // public void onFocusChange(View v, boolean hasFocus) {
        // // if (mColumnLayout.getChildCount() < 0) {
        // // mColumnLayout.clearFocus();
        // // }
        // //
        // // }
        // // });

    }

    private void saveColumns() {
        CommonUtil.reStoreValueIntoSharePreferences(CommonUtil.CUSTOM_COLUMN,
                "");
        if (mColumnLayout.getChildCount() == 0) {
            return;
        }

        for (int i = 0; i < mColumnLayout.getChildCount(); i++) {
            try {
                if (((LinearLayout) mColumnLayout.getChildAt(i)).getChildAt(0) != null
                        && TextUtils
                                .isEmpty(((EditText) ((LinearLayout) mColumnLayout
                                        .getChildAt(i)).getChildAt(0))
                                        .getText().toString())) {

                    mColumnLayout.removeViewAt(i);
                }
            } catch (Exception e) {
                continue;
            }
        }
        columnStr = new StringBuffer();
        String tablename = ((EditText) ((LinearLayout) mColumnLayout
                .getChildAt(0)).getChildAt(0)).getText().toString();
        for (int j = 0; j < mColumnLayout.getChildCount(); j++) {
            String tempValue = ((EditText) ((LinearLayout) mColumnLayout
                    .getChildAt(j)).getChildAt(0)).getText().toString();
            CommonUtil.reStoreValueIntoSharePreferences(tablename + "."
                    + tempValue, DBHelper.COLUMNS[j + 1]);
            Log.d(TAG, "tempValue" + tempValue + "column: "
                    + DBHelper.COLUMNS[j]);
            columnStr.append(tempValue).append("-");
        }
        columnStr.deleteCharAt(columnStr.lastIndexOf("-"));
        CommonUtil.reStoreValueIntoSharePreferences(CommonUtil.CUSTOM_COLUMN,
                columnStr.toString());
        if (mColumnLayout.getChildCount() > 0) {

        }
    }

    private void setFocus(boolean isAvailable) {
        if (isAvailable) {
            mColumnLayout.requestFocus();
        } else {
            mColumnLayout.clearFocus();
        }
        for (int j = 0; j < mColumnLayout.getChildCount(); j++) {
            ((LinearLayout) mColumnLayout.getChildAt(j)).getChildAt(0)
                    .setEnabled(isAvailable);
            ((LinearLayout) mColumnLayout.getChildAt(j)).getChildAt(0)
                    .setFocusableInTouchMode(isAvailable);
            if (isAvailable) {
                CommonUtil.showSoftKeyboard(this);
                ((LinearLayout) mColumnLayout.getChildAt(0)).getChildAt(0)
                        .requestFocus();
            } else {
                ((LinearLayout) mColumnLayout.getChildAt(j)).getChildAt(0)
                        .clearFocus();
            }
        }
    }

    private void addColumn(String itemContent, boolean isDelete) {
        final LinearLayout editLayout = new LinearLayout(mContext);
        editLayout.setOrientation(LinearLayout.HORIZONTAL);
        editLayout.setWeightSum(5.0f);

        ImageButton deleteBtn = new ImageButton(mContext);
        deleteBtn.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        // deleteBtn.setGravity(Gravity.CENTER_VERTICAL);
        // deleteBtn.setText("X");
        deleteBtn.setImageResource(R.drawable.remove);
        EditText tvShow = new EditText(mContext);
        tvShow.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT, 4.0f));
        tvShow.setSingleLine(true);
        tvShow.setTextColor(Color.BLACK);
        tvShow.setText(itemContent);
        // tvShow.setFocusableInTouchMode(itemContent.equals("")?true:false);
        // tvShow.setFocusableInTouchMode(false);
        tvShow.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                   try{
                    if (TextUtils.isEmpty(((EditText) v).getText().toString())) {
                        if (mColumnLayout.getChildCount() == 1) {
                            mColumnLayout.removeAllViews();
                            mColumnLayout.clearFocus();
                        } else {
                            mColumnLayout.removeView(editLayout);
                        }
                    }
                   }catch (Exception e) {
                       Log.e(TAG, e.toString());
                   }
                }
            }
        });
        tvShow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//
//                InputMethodManager m = (InputMethodManager) v.getContext()
//                        .getSystemService(Context.INPUT_METHOD_SERVICE);
//                m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }

        });
        deleteBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       try{
                        if (mColumnLayout.getChildCount() == 1) {
                            mColumnLayout.removeAllViews();
                            mColumnLayout.clearFocus();
                        } else {
                            if (editLayout != null)
                                mColumnLayout.removeView(editLayout);
                        }
                       }catch (Exception e) {
                           Log.e(TAG, e.toString());
                    }
                       
                        saveColumns();
                    }
                };
                CommonUtil.showWarnDialog(mContext, "删除列", "是否删除该列?", listener);
            }
        });
        tvShow.requestFocus();
        if (isDelete) {
            deleteBtn.setVisibility(View.VISIBLE);
        } else {
            deleteBtn.setVisibility(View.INVISIBLE);
        }
        editLayout.addView(tvShow);
        editLayout.addView(deleteBtn);
        mColumnLayout.addView(editLayout,
                LinearLayout.LayoutParams.MATCH_PARENT, mDimenSize);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_OK);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void hideSoftInput() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(CustomTableActivity.this
                        .getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void deleteItem() {
        for (int j = 0; j < mColumnLayout.getChildCount(); j++) {
            mColumnLayout.removeView(mColumnLayout.getFocusedChild());
        }
    }
}
