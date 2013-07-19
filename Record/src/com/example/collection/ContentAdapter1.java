package com.example.collection;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ContentAdapter1 extends BaseAdapter {
    private Context mContext;
    private List<String[]> mInfo;

    public ContentAdapter1(Context context, List<String[]> data) {
        mContext = context;
        mInfo = data;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mInfo.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout linLayout = new LinearLayout(mContext);
        linLayout.setOrientation(LinearLayout.HORIZONTAL);
        String[] data = mInfo.get(position);

        for (int i = 0; i < data.length; i++) {
            EditText child = new EditText(mContext);
            child.setText(data[i]);
            child.setTextSize(15);
            child.setBackgroundColor(Color.TRANSPARENT);
            // child.setFocusableInTouchMode(false);
            child.setSingleLine(true);
            // child.setTextColor(Color.BLACK);
            child.setCursorVisible(false);
            child.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            child.setCompoundDrawablesWithIntrinsicBounds(
                    android.R.drawable.divider_horizontal_dark, 0, 0, 0);
            linLayout.addView(child, 250, 100);
        }
        return linLayout;
    }

    private void initEvents(final EditText view) {
        view.setFocusableInTouchMode(false);
        view.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                view.setFocusableInTouchMode(true);
                view.requestFocus();
                return false;
            }
        });
        view.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    view.setFocusableInTouchMode(false);
                    editUserInfo();
                }
                return false;
            }
        });
    }

    private void editUserInfo() {

    }

}
