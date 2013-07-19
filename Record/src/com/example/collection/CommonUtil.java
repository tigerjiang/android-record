package com.example.collection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collection.util.MD5Util;

public class CommonUtil {
    public static final String USER_NAME_KEY = "username";
    public static final String USER_PASSWORD_KEY = "password";
    private static SharedPreferences mSharedPreferences;
    private static final CommonUtil INSTANCE = new CommonUtil();
    private static Context sContext;
    private static final String INTERNAL_CONFIG_FILE_NAME = "config.cg";
    private static final String EXTERNAL_CONFIG_FILE_NAME = "collection.txt";
    public static final String EXTERNAL_CONFIG_DIR_NAME = "collection";
    public static final String CUSTOM_TABLE_NAME = "自定义表";
    public static final String CUSTOM_SET_LABEL = "自定义表设置";
    public static final String CUSTOM_COLUMN = "custom_column";

    private CommonUtil() {

    }

    public static synchronized CommonUtil getInstance(Context context) {
        if (sContext == null) {
            sContext = context;
            mSharedPreferences = sContext.getSharedPreferences(
                    INTERNAL_CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        }
        return INSTANCE;
    }

    public static String getValueFromSharePreferences(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public static void reStoreValueIntoSharePreferences(String key, String value) {
        Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String[] getTablesInfo() {
        String[] tablesAndColumns = null;
        try {
            if (!TextUtils.isEmpty(getInfoPath())) {
                String tableInfo = FileTools.readTitleFile(getInfoPath());
                tablesAndColumns = tableInfo.split("#");
            }
        } catch (Exception e) {
            System.out.print("文件格式不正确");
        }
        return tablesAndColumns;
    }

    public static boolean checkSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static String getCollectionDIrPath() {
        if (checkSdcard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/" + EXTERNAL_CONFIG_DIR_NAME;
        } else {
            showDialog(sContext, "", "Sdcard不存在!");
            return null;
        }
    }

    public static String getInfoPath() {
        if (checkSdcard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/" + "" + "/" + EXTERNAL_CONFIG_FILE_NAME;
        } else {
            showDialog(sContext, "", "Sdcard不存在!");
            return null;
        }
    }

    public static void showDialog(Context context, String title, String msg) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
                .setPositiveButton("确认", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).create().show();
    }

    public static void showWarnDialog(Context context, String title,
            String msg, OnClickListener listener) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
                .setPositiveButton("确认", listener)
                .setNegativeButton("取消", null).create().show();
    }

    public static void showVerifyDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.verify_dialog, null);
        Button cancelBtn = (Button) rootView.findViewById(R.id.cancel_verify);
        Button confirmBtn = (Button) rootView.findViewById(R.id.verify);
        TextView imeiShow = (TextView) rootView.findViewById(R.id.imei_value);
        final EditText veryCodeEdit = (EditText) rootView
                .findViewById(R.id.code_value);
        imeiShow.setText(getIMEI());
        final AlertDialog verifyDialog = new AlertDialog.Builder(context)
                .setView(rootView).create();
        confirmBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (encryptMD5(getIMEI()).equals(
                        veryCodeEdit.getText().toString())) {
                    CommonUtil.getInstance(sContext)
                            .reStoreValueIntoSharePreferences("isVerify", "1");
                    Toast.makeText(sContext, "验证成功.", Toast.LENGTH_SHORT)
                            .show();
                    verifyDialog.dismiss();
                } else {
                    CommonUtil.getInstance(sContext)
                            .reStoreValueIntoSharePreferences("isVerify", "0");
                    Toast.makeText(sContext, "验证失败.", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
        cancelBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                verifyDialog.dismiss();

            }
        });
        verifyDialog.show();
    }

    public static void showSelectedDialog(Context context,
            OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setItems(new String[] { "查看更多", "返回" }, listener).create()
                .show();
    }

    /**
     * MD5加密
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptMD5(String data) {
        return MD5Util.md5(data);
    }

    public static String getIMEI() {
        TelephonyManager phoneManager = (TelephonyManager) sContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        return phoneManager.getDeviceId();
    }

    /**
     * 显示软键盘
     * 
     * @param v
     */
    public static void showSoftKeyboard(Activity activity) {
        InputMethodManager localInputMethodManager = (InputMethodManager) activity
                .getSystemService("input_method");
        IBinder localIBinder = activity.getWindow().getDecorView()
                .getWindowToken();
        localInputMethodManager.showSoftInputFromInputMethod(localIBinder, 0);
    }

    /**
     * 隐藏软键盘
     * 
     * @param v
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager localInputMethodManager = (InputMethodManager) activity
                .getSystemService("input_method");
        IBinder localIBinder = activity.getWindow().getDecorView()
                .getWindowToken();
        localInputMethodManager.hideSoftInputFromWindow(localIBinder, 0);
    }

}
