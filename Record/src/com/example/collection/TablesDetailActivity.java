package com.example.collection;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collection.data.CollectionProvider;

public class TablesDetailActivity extends Activity implements OnClickListener,
        OnItemSelectedListener, OnItemClickListener {
    private LinearLayout mTitileLayout;
    private ListView mContentListView;
    private Context mContext;
    private String[] mTitleList;
    private List<String[]> mContent;
    private ContentAdapter mContentAdapter;
    private Cursor mCursor;
    private String mTableName;
    private static String[] sColumns;
    private CommonUtil mUtil;
    // 添加,修改,删除,清空等操作按钮
    private Button mAddBtn, mModifyBtn, mDeleteBtn, mBackBtn, mExportBtn,
            mClearBtn;
    // 选中行的数据 key为列名 String 为value
    private String[] mCurrentRowValue;
    // 选中的行号，唯一标识
    private String mId = "-1";
    private int mWidth,mHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitileLayout = (LinearLayout) findViewById(R.id.tables_detail_title);
        mContentListView = (ListView) findViewById(R.id.tables_detail_content);
        mContentListView.setOnItemSelectedListener(this);
        mContentListView.setOnItemClickListener(this);
        mContext = TablesDetailActivity.this;
        mHeight =  (int) mContext.getResources().getDimension(R.dimen.content_item_height);
        mWidth =  (int) mContext.getResources().getDimension(R.dimen.content_item_width);
        mUtil = CommonUtil.getInstance(mContext);
        mContent = new ArrayList<String[]>();
        initOperationButton();
        initColumnName();
        initColumnAndValues();
        initColumnNameView();

    }

    private void initOperationButton() {
        mAddBtn = (Button) findViewById(R.id.add_btn);
        mAddBtn.setOnClickListener(this);
        mModifyBtn = (Button) findViewById(R.id.modify_btn);
        mModifyBtn.setOnClickListener(this);
        mDeleteBtn = (Button) findViewById(R.id.delete_btn);
        mDeleteBtn.setOnClickListener(this);
        mBackBtn = (Button) findViewById(R.id.back_btn);
        mBackBtn.setOnClickListener(this);
        mExportBtn = (Button) findViewById(R.id.export_btn);
        mExportBtn.setOnClickListener(this);
        mClearBtn = (Button) findViewById(R.id.clear_btn);
        mClearBtn.setOnClickListener(this);
    }

    private void initColumnName() {
        //
        if (getIntent() != null
                && getIntent().getStringArrayExtra("table") != null) {
            mTitleList = getIntent().getStringArrayExtra("table");
        }
    }

    private void initColumnAndValues() {
        sColumns = new String[mTitleList.length];
        mCurrentRowValue = new String[mTitleList.length];
    }

    // 初始化表头
    private void initColumnNameView() {
        // 下标从1开始，因为第一个存储的是数据库的表名,不用显示
        mTableName = mTitleList[0];
        for (int i = 0; i < mTitleList.length; i++) {
            if (i == 0) {
                sColumns[0] = mUtil.getValueFromSharePreferences("tableName");
                continue;
            }
            TextView tv = new TextView(mContext);
            tv.setLayoutParams(new LayoutParams(mWidth, mHeight));
            tv.setText(mTitleList[i]);
            tv.setTextSize(20);
            tv.setTextColor(Color.BLACK);
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            tv.setSingleLine(true);
            tv.setCompoundDrawablesWithIntrinsicBounds(
                    android.R.drawable.divider_horizontal_dark, 0, 0, 0);
            mTitileLayout.addView(tv);
            sColumns[i] = mUtil.getValueFromSharePreferences(mTableName + "."
                    + mTitleList[i]);
        }
        mCursor = getContentResolver().query(
                CollectionProvider.DOLLECTION_CONTENT_URI, sColumns,
                "table_name = ?", new String[] { mTableName }, null);
        mCursor.setNotificationUri(getContentResolver(),
                CollectionProvider.DOLLECTION_CONTENT_URI);
        mContentAdapter = new ContentAdapter(mContext, mCursor);
        mContentAdapter.setData(sColumns);
        mContentListView.setAdapter(mContentAdapter);
        if (mCursor != null && mCursor.getCount() > 0) {
            mContentListView.setSelection(0);
        }
        // new Utility().setListViewHeightBasedOnChildren(mContentListView);
    }

    @Override
    public void onItemSelected(AdapterView<?> viewGroup, View view,
            int position, long id) {
        view.setSelected(true);
        // mContentAdapter.setIndex(position);
        if (mCursor != null && mCursor.getCount() > 0) {
            if (mCursor.moveToPosition(position)) {
                mId = mCursor.getString(mCursor.getColumnIndex("_id"));
                for (int i = 0; i < sColumns.length; i++) {
                    mCurrentRowValue[i] = mCursor.getString(mCursor
                            .getColumnIndex(sColumns[i]));
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        mId = "-1";

    }

    @Override
    public void onClick(View v) {
        android.content.DialogInterface.OnClickListener listener;
        Intent intent = new Intent(mContext, InputContentActivity.class);
        switch (v.getId()) {
            case R.id.add_btn:
                intent.putExtra("_id", mId);
                intent.putExtra("type", "add");
                intent.putExtra("tabel_name", mTableName);
                // 传送对应表里面的column(如column_1,column_2 ....)
                intent.putExtra("column_name", sColumns);
                // 传送实际的具有实际意义的列名(如名字,年龄,性别...)
                intent.putExtra("column_title", mTitleList);
                startActivity(intent);
                break;

            case R.id.modify_btn:
                if (!mId.equals("-1") && mCurrentRowValue != null
                        && mCurrentRowValue.length > 0) {
                    intent.putExtra("_id", mId);
                    intent.putExtra("type", "update");
                    intent.putExtra("tabel_name", mTableName);
                    // 传送对应表里面的column(如column_1,column_2 ....)
                    intent.putExtra("column_name", sColumns);
                    // 传送实际的具有实际意义的列名(如名字,年龄,性别...)
                    intent.putExtra("column_title", mTitleList);
                    // 修改操作的时候 需要显示修改行的值
                    intent.putExtra("column_value", mCurrentRowValue);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "没有选中修改项.", Toast.LENGTH_SHORT)
                            .show();
                }

                break;
            case R.id.delete_btn:
                if (!mId.equals("-1")) {
                    listener = new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            getContentResolver().delete(
                                    CollectionProvider.DOLLECTION_CONTENT_URI,
                                    "_id = ?", new String[] { mId });
                            Toast.makeText(mContext, "删除数据成功.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    };
                    CommonUtil.showWarnDialog(mContext, "删除数据", "是否删除此条记录!",
                            listener);
                } else {
                    Toast.makeText(mContext, "没有选中刪除项.", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.export_btn:
                listener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        exportDataFromTable(sColumns, "table_name = ?",
                                new String[] { mTableName });
                    }
                };
                CommonUtil
                        .showWarnDialog(mContext, "导出数据", "是否导出数据?", listener);

                break;
            case R.id.clear_btn:
                listener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getContentResolver().delete(
                                CollectionProvider.DOLLECTION_CONTENT_URI,
                                "table_name = ?", new String[] { mTableName });
                        Toast.makeText(mContext, "清除数据成功.", Toast.LENGTH_SHORT)
                                .show();
                    }
                };
                CommonUtil
                        .showWarnDialog(mContext, "清空数据", "清空所有记录!", listener);

                break;
            case R.id.back_btn:
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {

        onItemSelected(parent, view, position, id);
        // if (mCursor != null && mCursor.getCount() > 0) {
        // if (mCursor.moveToPosition(position)) {
        // mId = mCursor.getString(mCursor.getColumnIndex("_id"));
        // for (int i = 0; i < sColumns.length; i++) {
        // mCurrentRowValue[i] = mCursor.getString(mCursor
        // .getColumnIndex(sColumns[i]));
        // }
        // }
        // }

    }

    private void exportDataFromTable(String[] projection, String selection,
            String[] selectionArgs) {
        Cursor cursor = getContentResolver().query(
                CollectionProvider.DOLLECTION_CONTENT_URI, projection,
                selection, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            StringBuffer content = new StringBuffer();
            for (int columnIndex = 1; columnIndex < mTitleList.length; columnIndex++) {
                content.append(mTitleList[columnIndex]).append("|");
            }
            content.append("\n");
            for (int rowCount = 0; rowCount < cursor.getCount(); rowCount++) {
                cursor.moveToPosition(rowCount);
                for (int i = 1; i < projection.length; i++) {
                    content.append(
                            cursor.getString(cursor
                                    .getColumnIndex(projection[i])))
                            .append("\t");
                }
                content.append("\n");
                Log.d("rowResult", content.toString());
            }
            try {
                FileTools.writeFile2(content.toString(),
                        mUtil.getCollectionDIrPath() + "/" + selectionArgs[0]
                                + ".txt");
                Toast.makeText(mContext, "导出数据成功.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCursor != null && mCursor.getCount() > 0) {
            mContentListView.setSelection(0);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                mTitleList = CommonUtil.getValueFromSharePreferences(
                        CommonUtil.CUSTOM_COLUMN).split("-");
                if (mTitleList.length == 1) {
                    finish();
                    return;
                }
                mTitileLayout.removeAllViews();

                initColumnAndValues();
                initColumnNameView();
            }
        }
    }
}
