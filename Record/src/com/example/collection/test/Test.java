package com.example.collection.test;

import java.io.IOException;

import com.example.collection.CommonUtil;
import com.example.collection.FileTools;
import com.example.collection.util.IdcardUtil;
import com.example.collection.util.MD5Util;
import com.example.collection.util.Validator;

import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;

public class Test extends AndroidTestCase {
    String rootPath = Environment.getExternalStorageDirectory()
            .getAbsolutePath();

    public void writeTitle() {
        StringBuffer sb = new StringBuffer();
        sb.append("姓名").append(",").append("性别").append(",").append("年龄")
                .append(",").append("婚姻状况").append(",").append("有效证件")
                .append(",").append("证件号").append(",").append("联系电话");
        try {
            FileTools.writeFile(sb.toString(), rootPath + "/"
                    + FileTools.USER_MODEL1_FILE);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void writeTitle2() {
        StringBuffer sb = new StringBuffer();
        sb.append("书名").append(",").append("作者").append(",").append("价格")
                .append(",").append("类型").append(",").append("出版时间");
        try {
            FileTools.writeFile(sb.toString(), rootPath + "/"
                    + FileTools.USER_MODEL2_FILE);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void radTitle() {
        try {
            String title = FileTools.readFile(rootPath + "/"
                    + FileTools.USER_MODEL2_FILE);
            String[] column = title.split(",");
            StringBuffer sb = new StringBuffer();
            for (String s : column) {
                sb.append(s).append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            Log.d("user", "title = " + sb.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void radInfo() {
        try {
            String title = FileTools.readFile(rootPath + "/"
                    + FileTools.USER_MODEL2_FILE);
            String[] column = title.split(",");
            StringBuffer sb = new StringBuffer();
            for (String s : column) {
                sb.append(s).append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            Log.d("user", "title = " + sb.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void writeInfo() {
        String sb = "张三,男,23,未婚,身份证,13313313131133,18300200102" + "\n"
                + "李四,男,25,已婚,身份证,23504312151,13300200100" + "\n"
                + "王二,男,24,未婚,身份证,6501252244,13500200002" + "\n"
                + "路人甲,男,23,未婚,身份证,13527852445,15120020152" + "\n"
                + "路人乙,男,26,已婚,身份证,530351462485,10086" + "\n"
                + "路人丙,男,22,未婚,身份证,2020565,100010" + "\n"
                + "熊大,男,12,未婚,身份证,454056465652,1331324" + "\n"
                + "熊二,男,11,未婚,身份证,13313313131133,1000101";
        try {
            FileTools.writeFile(sb.toString(), rootPath + "/"
                    + FileTools.USER_MODEL2_FILE);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void apendFile() {
        String sb = "张w,男,23,未婚,身份证,13313313131133,1830020010212" + "\n";
        FileTools.appendMethodB(rootPath + "/" + FileTools.USER_MODEL2_FILE,
                sb.toString());
        radInfo();
    }

    public void testMd5() {
        String data = "868033013293826";
        String md5 = CommonUtil.encryptMD5(data);
        Log.d("md5", "md5  =" + md5);
    }

    public void testMd52() {
        String data = "868033013293826";
        String md5 = MD5Util.md5(data);
        Log.d("md5", "md5  =" + md5);
    }
    
    public void deleteFile(){
        FileTools.deleteAllFile();
    }

    public void testIDnumber(){
        String id = "53038119860725431x";
        boolean isLegality = IdcardUtil.isIdcard(id);
        System.err.println(isLegality?id+ " is a legality":id+" is not a legality");
        
    }
}
