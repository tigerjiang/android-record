package com.example.collection;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;


public class CustomTable extends Activity {
    private ResizeListView CustomListView;
    private ArrayList<Content> mData;
    private CustomAdapter mCustomAdapter;
    private int mDimenSize;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom);
        mContext = this.getApplicationContext();
        mDimenSize = (int) mContext.getResources().getDimension(
                R.dimen.content_item_height);
        CustomListView = (ResizeListView) findViewById(R.id.custom_list);
        mData = new ArrayList<Content>();
        mData.add(new Content("姓名"));
        mData.add(new Content("年龄"));
        mData.add(new Content("性别"));
        mData.add(new Content("电话"));
        mCustomAdapter = new CustomAdapter(getApplicationContext(), mData);
        CustomListView.setAdapter(mCustomAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            // add
            mData.add(new Content(""));
            mCustomAdapter.notifyDataSetChanged();
            CustomListView.setSelection(CustomListView.getCount() - 1);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            // save
            setFocus(false);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            // modify
            setFocus(true);
        }

        return super.onKeyDown(keyCode, event);
    }

    private void setFocus(boolean isAvailable) {
        for (int i = 0; i < CustomListView.getChildCount(); i++) {
            CustomListView.getChildAt(0).setEnabled(isAvailable);
            CustomListView.getChildAt(1).setEnabled(isAvailable);
        }
        if (isAvailable) {
            CustomListView.requestFocus();
        } else {
            CustomListView.clearFocus();
        }
    }
}
