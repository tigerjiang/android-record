package com.example.collection;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collection.data.CollectionProvider;
import com.example.collection.data.DBHelper;

public class TablesActivity extends Activity {
    private GridView mTableGridView;
    private TableAdapter mAdapter;
    private static List<String[]> mTablist;
    private Context mContext;
    private CommonUtil mUtil;
    private static final String SELECTION = "table_name = ?";
    private int mTableHeight,mTableWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tables_layout);
        mContext = TablesActivity.this;
        mTableHeight =  (int) mContext.getResources().getDimension(R.dimen.table_item_height);
        mTableWidth =  (int) mContext.getResources().getDimension(R.dimen.table_item_width);
        mUtil = CommonUtil.getInstance(mContext);
        mTableGridView = (GridView) findViewById(R.id.tables);
        initHandler();
        initData();
        mAdapter = new TableAdapter();
        mTableGridView.setAdapter(mAdapter);
        mTableGridView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                            int arg2, long arg3) {
                        if (arg2 == 0) {
                            android.content.DialogInterface.OnClickListener listener = new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    CommonUtil.showDialog(mContext, "导出数据",
                                            "导出成功!");
                                    //首先删除所有导出的文件
                                    FileTools.deleteAllFile();
                                    queryAll();

                                }
                            };
                            CommonUtil.showWarnDialog(mContext, "导出数据",
                                    "是否导出所有记录?", listener);
                        } else if (arg2 == 1) {
                            android.content.DialogInterface.OnClickListener listener = new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    // TODO Auto-generated method stub
                                    getContentResolver()
                                            .delete(CollectionProvider.DOLLECTION_CONTENT_URI,
                                                    null, null);
                                    CommonUtil.showDialog(mContext, "清除数据",
                                            "清除所有数据成功!");

                                }
                            };
                            CommonUtil.showWarnDialog(mContext, "清除数据",
                                    "是否清除所有记录?", listener);

                        } else if (arg2 == 2) {
                            // 自定义表
                            if (mTablist.get(arg2).length > 1) {
                                Intent intent = new Intent(mContext,
                                        TablesDetailActivity.class);
                                intent.putExtra("table", mTablist.get(arg2));
                                startActivity(intent);
                            } else {
                                CommonUtil.showDialog(mContext, "自定义表信息",
                                        "自定义表列为空，请先添加列");
                            }
                        } else if (arg2 == 3) {
                            Intent intent = new Intent(mContext,
                                    CustomTableActivity.class);
                            intent.putExtra("custom", mTablist.get(2));
                            startActivityForResult(intent, 3);

                        } else {
                            Intent intent = new Intent(mContext,
                                    TablesDetailActivity.class);
                            intent.putExtra("table", mTablist.get(arg2));
                            startActivity(intent);
                        }
                    }
                });
    }

    private void queryAll() {
        for (int tableIndex = 2; tableIndex < mTablist.size(); tableIndex++) {
            StringBuffer bf = new StringBuffer();
            String[] mTabTitle = mTablist.get(tableIndex);
            String[] project = new String[mTabTitle.length];
            String tableName = mTabTitle[0];
            for (int columnIndex = 0; columnIndex < mTabTitle.length; columnIndex++) {
                if (columnIndex == 0) {
                    project[0] = mUtil
                            .getValueFromSharePreferences("tableName");
                    bf.append(project[0]).append(":");
                    continue;
                }
                project[columnIndex] = mUtil
                        .getValueFromSharePreferences(tableName + "."
                                + mTabTitle[columnIndex]);
                bf.append(project[columnIndex]).append(":");
            }
            Log.d("projection", tableName + "****" + bf.toString());
            Message msg = mHandler.obtainMessage();
            Bundle data = new Bundle();
            data.putString("tableName", tableName);
            data.putStringArray("projection", project);
            data.putStringArray("column", mTabTitle);
            msg.setData(data);
            msg.what = SaveHandler.START_SAVE;
            mHandler.sendMessage(msg);
        }
    }

    private class TableAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTablist.size();
        }

        @Override
        public Object getItem(int position) {
            return mTablist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tableName = new TextView(mContext);
            tableName.setHeight(mTableHeight);
            tableName.setWidth(mTableWidth);
            tableName.setGravity(Gravity.CENTER);
            tableName.setTextColor(Color.BLACK);
            tableName.setText(mTablist.get(position)[0]);
            convertView = tableName;
            convertView.setBackgroundResource(R.drawable.ic_table_bg_focus);
            return convertView;
        }

    }

    private void initData() {
        mTablist = new ArrayList<String[]>();
        mTablist.add(new String[] { "导出所有" });
        mTablist.add(new String[] { "清除所有" });
        String customColumnsStr = mUtil
                .getValueFromSharePreferences(CommonUtil.CUSTOM_COLUMN);
        String[] customColumns = TextUtils.isEmpty(customColumnsStr) ? null
                : customColumnsStr.split("-");

        if (customColumns != null && customColumns.length > 0) {
        } else {
            customColumns = new String[] { CommonUtil.CUSTOM_TABLE_NAME };

        }
        mTablist.add(customColumns);
        mTablist.add(new String[] { CommonUtil.CUSTOM_SET_LABEL });
        String[] tablesAndColumns = CommonUtil.getTablesInfo();
        if(tablesAndColumns==null||tablesAndColumns.length<=0){
            Toast.makeText(mContext, "配置文件不存在,请检查!",Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < tablesAndColumns.length; i++) {
            String[] tableNameAndColumnName = tablesAndColumns[i].split("-");
            restoreColumnName(tableNameAndColumnName);
            mTablist.add(tableNameAndColumnName);
        }

    }

    private void restoreColumnName(String[] tableNameAndColumnName) {
        String tableName = tableNameAndColumnName[0];
        mUtil.reStoreValueIntoSharePreferences("tableName", "table_name");
        for (int i = 1; i < tableNameAndColumnName.length; i++) {
            mUtil.reStoreValueIntoSharePreferences(tableName + "."
                    + tableNameAndColumnName[i], DBHelper.COLUMNS[i]);
        }
    }

    private HandlerThread mThread;
    private Handler mHandler;

    private void initHandler() {
        mThread = new HandlerThread("report");
        mThread.start();
        mHandler = new SaveHandler(mThread.getLooper());
    }

    private class SaveHandler extends Handler {
        // Here I just use do start report though handler. because start report
        // will delay 15s, end report is real-time.so it is necessary to use
        // handler.
        private static final int START_SAVE = 0;

        public SaveHandler(Looper looper) {
            super(looper);

        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_SAVE:
                    String[] project = msg.getData().getStringArray(
                            "projection");
                    String tableName = msg.getData().getString("tableName");
                    String[] column = msg.getData().getStringArray("column");
                    exportDataFromTable(column, project, SELECTION,
                            new String[] { tableName });
                    break;
            }

        }
    }

    private void exportDataFromTable(String[] column, String[] projection,
            String selection, String[] selectionArgs) {
        Cursor cursor = getContentResolver().query(
                CollectionProvider.DOLLECTION_CONTENT_URI, projection,
                selection, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            StringBuffer content = new StringBuffer();
            for (int columnIndex = 1; columnIndex < column.length; columnIndex++) {
                content.append(column[columnIndex]).append("\t");
            }
            content.deleteCharAt(content.lastIndexOf("\t"));
            content.append("\n");
            for (int rowCount = 0; rowCount < cursor.getCount(); rowCount++) {
                cursor.moveToPosition(rowCount);
                for (int i = 1; i < projection.length; i++) {
                    content.append(
                            cursor.getString(cursor
                                    .getColumnIndex(projection[i])))
                            .append("\t");
                }
                content.deleteCharAt(content.lastIndexOf("\t"));
                content.append("\n");
                Log.d("rowResult", content.toString());
            }
            try {
                FileTools.writeFile2(content.toString(),
                        mUtil.getCollectionDIrPath() + "/" + selectionArgs[0]
                                + ".txt");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            initData();
            mAdapter.notifyDataSetChanged();
        }
    }

}
