package com.example.collection;

import java.util.ArrayList;

import com.example.collection.data.CollectionProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class CustomAdapter extends BaseAdapter {
    private ArrayList<Content> mColumnData;
    private Context mContext;
    private final LayoutInflater mInflater;
    private EditText columnTxt;
    private ImageButton deleteBtn;

    public CustomAdapter(Context context, ArrayList<Content> data) {
        this.mContext = context;
        this.mColumnData = data;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mColumnData.size();
    }

    @Override
    public Object getItem(int position) {
        return mColumnData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int index = position;
        final Content data = mColumnData.get(position);
        convertView = mInflater.inflate(R.layout.list_item, null);
        columnTxt = (EditText) convertView.findViewById(R.id.column_txt);
        deleteBtn = (ImageButton) convertView.findViewById(R.id.delete_btn);
        if(position==0){
            deleteBtn.setVisibility(View.GONE);
        }else{
            deleteBtn.setVisibility(View.VISIBLE);
        }
        columnTxt.setText(data.getContent());
        columnTxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                data.setContent(s.toString());
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mColumnData.remove(index);
                        notifyDataSetChanged();
                    }
                };

                CommonUtil.showWarnDialog(mContext, "删除列", "是否删除该列?", listener);

            }
        });
        return convertView;
    }

}
