package com.example.collection;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContentAdapter extends CursorAdapter {

    private String[] mColumnName;
    private ViewGroup contentView;
    private int selectedIndex = 0;
    private int mWidth,mHeight;

    public ContentAdapter(Context context, Cursor c) {
        super(context, c);
        mHeight =  (int) context.getResources().getDimension(R.dimen.content_item_height);
        mWidth =  (int) context.getResources().getDimension(R.dimen.content_item_width);
        // TODO Auto-generated constructor stub
    }

    public void setData(String[] columnName) {
        mColumnName = columnName;
    }

    public void setIndex(int index) {
        selectedIndex = index;
        notifyDataSetChanged();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        for (int child = 0; child < ((ViewGroup) view).getChildCount(); child++) {
            // if(child == 0){
            // //第一列表名,不需要显示
            // continue ;
            // }
            Log.d("adapter",
                    mColumnName[child]
                            + ":"
                            + cursor.getString(cursor
                                    .getColumnIndex(mColumnName[child + 1])));
            TextView columnValue = (TextView) ((ViewGroup) view)
                    .getChildAt(child);
            columnValue.setText(cursor.getString(cursor
                    .getColumnIndex(mColumnName[child + 1])));
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        contentView = new LinearLayout(context);
        ((LinearLayout) contentView).setOrientation(LinearLayout.HORIZONTAL);
        // 第一列为表名,不需要显示，所以这里的大小为mColumnName
        for (int i = 0; i < mColumnName.length - 1; i++) {
            TextView child = new TextView(context);
            child.setTextSize(15);
            child.setBackgroundColor(Color.TRANSPARENT);
            // child.setFocusableInTouchMode(false);
            child.setSingleLine(true);
            child.setTextColor(Color.BLACK);
            child.setCursorVisible(false);
            child.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            child.setCompoundDrawablesWithIntrinsicBounds(
                    android.R.drawable.divider_horizontal_dark, 0, 0, 0);
            contentView.addView(child, mWidth, mHeight);
            
        }
        return contentView;
    }

    @Override
    public void changeCursor(Cursor cursor) {
        // TODO Auto-generated method stub
        super.changeCursor(cursor);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (contentView != null) {
            if (selectedIndex == position) {
                contentView
                        .setBackgroundResource(R.drawable.media_list_selector);
            } else {
                contentView
                        .setBackgroundResource(R.drawable.media_list_selector);
            }
        }
        return super.getView(position, convertView, parent);
    }

}
