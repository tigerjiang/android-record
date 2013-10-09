package com.example.collection;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.collection.data.CollectionProvider;
import com.example.collection.util.IdcardUtil;

public class InputContentActivity extends Activity implements
        android.view.View.OnClickListener {
    private Context mContext;
    private LinearLayout mTitileLayout;
    private static LinearLayout mValueLayout;
    private Button mSaveBtn, mCancelBtn;
    public static String modelFile;
    // 表名
    private String mTableName;
    // 实际意义的列名
    private String[] mColumnTitle;
    // 数据库中对应的列名
    private String[] mColumnName;
    // 列对应的值
    private String[] mColumnValue;
    // 修改的类型 add 和 update
    private String type;
    // 该index用于判定修改的是那一行
    private int mChildIndex;

    private String mId;

//    private Button mDateView, mTimeView;
    private RadioButton mManBtn,mWomenBtn,mNoneBtn;
    
    private EditText tempEditItem;
    private boolean isEditFull;
     //正则表达式表示15位或者18位数字的一串数字
    private Pattern mIdPattern = Pattern.compile("\\d{15}|^\\d{17}[0-9X]$");
    private Pattern mPhonePattern = Pattern.compile("\\d{11}");
    // date and time
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mSecond;

    static final int TIME_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID = 1;
    private String tempStr = "";
    private int mDimenSize ;
    private String mErrorMessage;
    private String mSexStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_layout);
        mContext = InputContentActivity.this;
        mDimenSize = (int) mContext.getResources().getDimension(R.dimen.content_item_height);
        mTitileLayout = (LinearLayout) findViewById(R.id.column);
        mValueLayout = (LinearLayout) findViewById(R.id.value);
        mSaveBtn = (Button) findViewById(R.id.save);
        mSaveBtn.setOnClickListener(this);
        mCancelBtn = (Button) findViewById(R.id.cancel);
        mCancelBtn.setOnClickListener(this);
        initDate();
        initData();
        initView();
    }

    private void initData() {
        if (getIntent() != null) {
            mId = getIntent().getStringExtra("_id");
            type = getIntent().getStringExtra("type");
            mTableName = getIntent().getStringExtra("tabel_name");
            mColumnName = getIntent().getStringArrayExtra("column_name");
            mColumnTitle = getIntent().getStringArrayExtra("column_title");
            mColumnValue = getIntent().getStringArrayExtra("column_value");
        }
    }

    private void initView() {
        for (int i = 1; i < mColumnTitle.length; i++) {
            mChildIndex = i;
            TextView tv = new TextView(mContext);

            tv.setText(mColumnTitle[i]);
            tv.setTextSize(20);
            tv.setTextColor(Color.BLACK);
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            tv.setSingleLine(true);
            tv.setCompoundDrawablesWithIntrinsicBounds(
                    android.R.drawable.divider_horizontal_dark, 0, 0, 0);
            mTitileLayout.addView(tv, LayoutParams.MATCH_PARENT, mDimenSize);

            if (mColumnTitle[mChildIndex].contains("时间")
                    || mColumnTitle[mChildIndex].contains("日期")) {
                LinearLayout dataLayout = new LinearLayout(mContext);
                dataLayout.setOrientation(LinearLayout.HORIZONTAL);
                dataLayout.setWeightSum(2.0f);
            final  Button   mDateView = new Button(mContext);
             final Button  mTimeView = new Button(mContext);
                // mDateView.setTextColor(Color.DKGRAY);
                mDateView.setTextSize(16);
                mTimeView.setTextSize(16);
                // mTimeView.setTextColor(Color.DKGRAY);
                mDateView.setGravity(Gravity.CENTER);
                mTimeView.setGravity(Gravity.CENTER);
                mDateView.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
                        1.0f));
                mTimeView.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
                        1.0f));
               
                
              final   DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                            int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        updateDateDisplay(mDateView);
                    }
                };
                final DatePickerDialog datePickerDialog =  new DatePickerDialog(InputContentActivity.this, dateSetListener, mYear,
                        mMonth, mDay);
                mDateView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        datePickerDialog.show();

                    }
                });

              final  TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;
                        updateTimeDisplay(mTimeView);
                    }
                };
                final  TimePickerDialog tiemPickDialogh =  new TimePickerDialog(InputContentActivity.this, timeSetListener, mHour, mMinute, false); 
                mTimeView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        tiemPickDialogh.show();

                    }
                });

             
                dataLayout.addView(mDateView);
                dataLayout.addView(mTimeView);
                if (type != null) {

                    if (type.equals("add")) {
                        updateDateDisplay(mDateView);
                        updateTimeDisplay(mTimeView);
                    } else {
                        try{
                        String[] dateStr = mColumnValue[i].split(" ");
                        mDateView.setText(dateStr[0]);
                        mTimeView.setText(dateStr[1]);
                        }catch(Exception e){
                            updateDateDisplay(mDateView);
                            updateTimeDisplay(mTimeView);
                        }
                    }
                }
                mValueLayout
                        .addView(dataLayout, LayoutParams.MATCH_PARENT, mDimenSize);
            }else if (mColumnTitle[mChildIndex].contains("性别")) {
                LinearLayout dataLayout = new LinearLayout(mContext);
                dataLayout.setOrientation(LinearLayout.HORIZONTAL);
                dataLayout.setWeightSum(3.0f);
                RadioGroup radioGroup = new RadioGroup(mContext);
                radioGroup.setOrientation(LinearLayout.HORIZONTAL);
                radioGroup.setWeightSum(3.0f);
                
                mManBtn = new RadioButton(mContext);
                mWomenBtn = new RadioButton(mContext);
                mNoneBtn = new RadioButton(mContext);
                
//                mManBtn = new Button(mContext);
//                mWomenBtn = new Button(mContext);
//                mNoneBtn = new Button(mContext);
                // mDateView.setTextColor(Color.DKGRAY);
                mManBtn.setId(100);
                mWomenBtn.setId(101);
                mNoneBtn.setId(102);
                mManBtn.setTextSize(16);
                mWomenBtn.setTextSize(16);
                mNoneBtn.setTextSize(16);
                // mTimeView.setTextColor(Color.DKGRAY);
                mManBtn.setGravity(Gravity.CENTER);
                mWomenBtn.setGravity(Gravity.CENTER);
                mNoneBtn.setGravity(Gravity.CENTER);
                mManBtn.setText("男");
                mWomenBtn.setText("女");
                mNoneBtn.setText("不明");
                mManBtn.setLayoutParams(new RadioGroup.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
                        1.0f));
                mWomenBtn.setLayoutParams(new RadioGroup.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
                        1.0f));
                mNoneBtn.setLayoutParams(new RadioGroup.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
                        1.0f));
//                mNoneBtn.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        mNoneBtn.setBackgroundResource(R.drawable.ic_push_btn_default_sel);
//                        mManBtn.setBackgroundResource(R.drawable.btn_selector);
//                        mWomenBtn.setBackgroundResource(R.drawable.btn_selector);
//                        mSexStr = mNoneBtn.getText().toString();
//                    }
//                });
//                mWomenBtn.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        mWomenBtn.setBackgroundResource(R.drawable.ic_push_btn_default_sel);
//                        mManBtn.setBackgroundResource(R.drawable.btn_selector);
//                        mNoneBtn.setBackgroundResource(R.drawable.btn_selector);
//                        mSexStr = mWomenBtn.getText().toString();
//                    }
//                });
//                mManBtn.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        mManBtn.setBackgroundResource(R.drawable.ic_push_btn_default_sel);
//                        mWomenBtn.setBackgroundResource(R.drawable.btn_selector);
//                        mNoneBtn.setBackgroundResource(R.drawable.btn_selector);
//                        mSexStr = mManBtn.getText().toString();
//                    }
//                });
                radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId==mManBtn.getId()){
                            mSexStr = "男";
                        }else if(checkedId==mWomenBtn.getId()){
                            mSexStr = "女";
                       }else if(checkedId==mNoneBtn.getId()){
                           mSexStr = "不明";
                       }  
                }}
                );
                radioGroup.addView(mManBtn);
                radioGroup.addView(mWomenBtn);
                radioGroup.addView(mNoneBtn);
                if (type != null) {

                    if (type.equals("add")) {
                        radioGroup.check(mManBtn.getId());
                    } else {
                        String sex = mColumnValue[i];
                        if(sex.equals("男")){
                            radioGroup.check(mManBtn.getId());
                        }else if(sex.equals("女")){
                            radioGroup.check(mWomenBtn.getId());
                        }else{
                            radioGroup.check(mNoneBtn.getId());
                        }

                    }
                }
                mValueLayout
                        .addView(radioGroup, LayoutParams.MATCH_PARENT, mDimenSize);
            }
                
                
                
                else if (mColumnTitle[mChildIndex].contains("填表人")
                  ) {
                final EditText mEditItem = new EditText(mContext);
                mEditItem.setSingleLine(true);
                if (type != null) {

                    if (type.equals("add")) {
                        mEditItem.setText(CommonUtil.getValueFromSharePreferences(CommonUtil.USER_NAME_KEY));
                    } else {
                        mEditItem.setText(mColumnValue[i]);
                    }
                }
                mValueLayout.addView(mEditItem, LayoutParams.MATCH_PARENT, mDimenSize);
            }else if (mColumnTitle[mChildIndex].contains("身份证")
                    ) {
                  final EditText mEditItem = new EditText(mContext);
                  mEditItem.setSingleLine(true);
                  mEditItem.setInputType(InputType.TYPE_CLASS_TEXT);
                  
                  mEditItem.addTextChangedListener(new TextWatcher() {
                      @Override
                      public void onTextChanged(CharSequence s, int start,
                              int before, int count) {
                          tempEditItem = mEditItem;
                          Log.d("textchange", "onTextChanged======" + count
                                  + "  " + s);
                      }

                      @Override
                      public void beforeTextChanged(CharSequence s, int start,
                              int count, int after) {
                          Log.d("textchange", "beforeTextChanged======" + count);

                      }

                      // 监听输入框输入多少个字符,如果超出显示范围,弹出全屏编辑框
                      @Override
                      public void afterTextChanged(Editable s) {
                          Log.d("textchange", "afterTextChanged======" + s.toString());
                        if (!TextUtils.isEmpty(s.toString())) {
                            if (IdcardUtil.isIdcard(s.toString())) {
                                mErrorMessage = null;
                            }else{
                                mErrorMessage = "输入身份证号码不正确,请重新输入";
                            }

                        } else {
                            mErrorMessage = null;
                        }
                        Log.w("textchange", "error======" + mErrorMessage);
                    }
                  });
                  if (type != null) {

                      if (type.equals("add")) {
                          mEditItem.setText("");
                      } else {
                          mEditItem.setText(mColumnValue[i]);
                      }
                  }
                  mValueLayout.addView(mEditItem, LayoutParams.MATCH_PARENT, mDimenSize);
              }else if (mColumnTitle[mChildIndex].contains("手机号")
                      ) {
                    final EditText mPhoneItem = new EditText(mContext);
                    mPhoneItem.setSingleLine(true);
                    mPhoneItem.setInputType(InputType.TYPE_CLASS_NUMBER);
                    mPhoneItem.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start,
                                int before, int count) {
                            Log.d("textchange", "onTextChanged======" + count
                                    + "  " + s);
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start,
                                int count, int after) {
                            Log.d("textchange", "beforeTextChanged======" + count);

                        }

                        // 监听输入框输入多少个字符,如果超出显示范围,弹出全屏编辑框
                        @Override
                        public void afterTextChanged(Editable s) {
                            Log.d("textchange", "afterTextChanged======" + s.toString());
                        if (!TextUtils.isEmpty(s.toString())) {
                            Matcher matcher = mPhonePattern.matcher(s.toString());
                            if (!matcher.matches()) {
                                mErrorMessage = "输入手机号码不正确,请重新输入";
                            } else {
                                mErrorMessage = null;
                            }
                        }else{
                            mErrorMessage=null;
                        }
                        Log.w("textchange", "error======" + mErrorMessage);
                        }
                    });
                    
//                    mPhoneItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                      
//                      @Override
//                      public void onFocusChange(View v, boolean hasFocus) {
//                         if(hasFocus){
//                             
//                         }else{
//                             if(!TextUtils.isEmpty(mPhoneItem.getText().toString())){
//                            Matcher matcher =  mPhonePattern.matcher(mPhoneItem.getText().toString());
//                            if(!matcher.matches()){
//                                mErrorMessage = "输入手机号码不正确,请重新输入";
//                            }else{
//                                mErrorMessage=null;
//                            }
//                             }
//                         }
//                      }
//                  });
                    if (type != null) {

                        if (type.equals("add")) {
                            mPhoneItem.setText("");
                        } else {
                            mPhoneItem.setText(mColumnValue[i]);
                        }
                    }
                    mValueLayout.addView(mPhoneItem, LayoutParams.MATCH_PARENT, mDimenSize);
                }
            else {
                final EditText mEditItem = new EditText(mContext);
                mEditItem.setSingleLine(true);
                final String title = tv.getText().toString();
                mEditItem
                        .setOnLongClickListener(new View.OnLongClickListener() {

                            @Override
                            public boolean onLongClick(View v) {

                                showSelectedDialog(title, mEditItem.getText()
                                        .toString());
                                return false;
                            }
                        });

                mEditItem.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                            int before, int count) {
                        tempEditItem = mEditItem;
                        Log.d("textchange", "onTextChanged======" + count
                                + "  " + s);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                            int count, int after) {
                        Log.d("textchange", "beforeTextChanged======" + count);

                    }

                    // 监听输入框输入多少个字符,如果超出显示范围,弹出全屏编辑框
                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                        Log.d("textchange", "afterTextChanged======" + s);
                        Log.d("textchange", "tempStr==" + tempStr + "sss==" + s);
                        Log.d("textchange",
                                "isEqual==" + tempStr.equals(s.toString()));
                        if (!tempStr.equals(s.toString()) && s.length() > 20) {
                            Intent intent = new Intent(mContext,
                                    EditActivity.class);
                            intent.putExtra("content", s.toString());
                            intent.putExtra("table_name", mTableName);
                            intent.putExtra("column_name",
                                    mColumnTitle[mChildIndex]);
                            startActivityForResult(intent, 0);
                            // mEditItem.removeTextChangedListener(this);
                        }
                    }
                });
                if (type != null) {

                    if (type.equals("add")) {
                        mEditItem.setText("");
                    } else {
                        mEditItem.setText(mColumnValue[i]);
                    }
                }
                mValueLayout.addView(mEditItem, LayoutParams.MATCH_PARENT, mDimenSize);
            }
        }
    }

    private void submit() {
        StringBuffer sb = new StringBuffer();
        ContentValues values = new ContentValues();
        values.put(mColumnName[0], mTableName);
        for (int childIndex = 0; childIndex < mValueLayout.getChildCount(); childIndex++) {
            String item = "";

            if (mValueLayout.getChildAt(childIndex) instanceof LinearLayout) {
                if (mColumnTitle[childIndex + 1].contains("日期")
                        || mColumnTitle[childIndex + 1].contains("时间")) {
                    item = ((Button)((LinearLayout)mValueLayout.getChildAt(childIndex)).getChildAt(0)).getText().toString() + " "
                            + ((Button)((LinearLayout)mValueLayout.getChildAt(childIndex)).getChildAt(1)).getText().toString();
                } else if (mColumnTitle[childIndex + 1].contains("性别")) {
                    item =  mSexStr;
                }
            } else {
                item = ((EditText) mValueLayout.getChildAt(childIndex))
                        .getText().toString();
            }
            // 去除表名列
            values.put(mColumnName[childIndex + 1], item);
        }

        String message = null;
        
        if (isEmpty(sb)) {
            message = "信息为空，保存失败";
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            return;
        } else if(!TextUtils.isEmpty(mErrorMessage)){
            message = mErrorMessage;
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            return;
        }else {
            message = "保存成功";
            if (type.equals("add")) {
                // 添加
                getContentResolver().insert(
                        CollectionProvider.DOLLECTION_CONTENT_URI, values);
                Toast.makeText(mContext, "添加成功.", Toast.LENGTH_SHORT).show();
            } else {
                // 修改
                getContentResolver().update(
                        CollectionProvider.DOLLECTION_CONTENT_URI, values,
                        "_id = ?", new String[] { mId });
                Toast.makeText(mContext, "修改成功.", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        
    }

    private boolean isEmpty(StringBuffer sb) {
        String[] checkStr = sb.toString().split(",");
        return checkStr == null || checkStr.length == 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                submit();
                break;
            case R.id.cancel:
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            tempStr = data.getStringExtra("content");
            tempEditItem.setText(data.getStringExtra("content"));
            Log.d("result", tempStr);
            // ((EditText) mValueLayout.getChildAt(mChildIndex -
            // 1)).setText(data
            // .getStringExtra("content"));
        }
        isEditFull = false;
    }

    @SuppressLint("ParserError")
    private void initDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSecond = c.get(Calendar.SECOND);
        Log.d("time",
                new StringBuilder().append(mMonth + 1).append("-").append(mDay)
                        .append("-").append(mYear).append(" ")
                        .append(pad(mHour)).append(":").append(pad(mMinute))
                        .append(":").append(pad(mSecond)).toString());
    }


    private void updateTimeDisplay(Button timeView) {
        timeView.setText(new StringBuilder().append(pad(mHour)).append(":")
                .append(pad(mMinute)).append(":").append(pad(mSecond)));
    }


    private void updateDateDisplay(Button dataView) {
        dataView.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(mYear).append("-").append(mMonth + 1).append("-")
                .append(mDay));
    }
    
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void showSelectedDialog(final String title, final String value) {
        new AlertDialog.Builder(InputContentActivity.this)
                .setItems(new String[] { "查看更多", "返回" },
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                if (which == 0) {
                                    CommonUtil.showDialog(mContext, title,
                                            value);
                                } else {
                                    dialog.dismiss();
                                }
                            }
                        }).create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isEditFull = true;
    }
}
