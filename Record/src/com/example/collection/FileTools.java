package com.example.collection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.util.Log;

public class FileTools {
    public static final String USER_MODEL1_FILE = "model1.txt";
    public static final String USER_MODEL2_FILE = "model2.txt";

    // 读取文件
    public static String FileInputStreamDemo(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory())
            throw new FileNotFoundException();
        FileInputStream fis = new FileInputStream(file);
        byte[] buf = new byte[1024];
        StringBuffer sb = new StringBuffer();
        while ((fis.read(buf)) != -1) {
            sb.append(new String(buf));
            buf = new byte[1024];// 重新生成，避免和上次读取的数据重复
        }
        return sb.toString();
    }

    // 以行的方式读取文件
    public static String readTitleFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory())
            throw new FileNotFoundException();
        // 注意中文编码"GB2312"
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), "GB2312"));
        String temp = null;
        StringBuffer sb = new StringBuffer();
        temp = br.readLine();
        while (temp != null) {
            sb.append(temp + "#");
            temp = br.readLine();
        }
        Log.d("title", sb.toString());
        return sb.toString();
    }

    // 以行的方式读取文件
    public static String readFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory())
            throw new FileNotFoundException();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp = null;
        StringBuffer sb = new StringBuffer();
        temp = br.readLine();
        while (temp != null) {
            sb.append(temp + "&");
            temp = br.readLine();
        }
        return sb.toString();
    }

    public static void writeFile(String src, String dest) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(src.getBytes());
        File file = new File(dest);
        if (!file.exists())
            file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        int c;
        byte buffer[] = new byte[1024];
        while ((c = in.read(buffer)) != -1) {
            for (int i = 0; i < c; i++)
                out.write(buffer[i]);
        }
        in.close();
        out.close();
    }

    public static void writeFile2(String fileContent, String filePathAndName) {
        try {
            File dir = new File(CommonUtil.getCollectionDIrPath());
            if (!dir.exists()) {
                dir.mkdir();
            }
            File f = new File(filePathAndName);
            if (f.exists()) {
                f.delete();
            } else {
                f.createNewFile();

            }
            OutputStreamWriter write = new OutputStreamWriter(
                    new FileOutputStream(f), "GB2312");
            BufferedWriter writer = new BufferedWriter(write);
            // PrintWriter writer = new PrintWriter(new BufferedWriter(new
            // FileWriter(filePathAndName)));
            // PrintWriter writer = new PrintWriter(new
            // FileWriter(filePathAndName));
            writer.write(fileContent);
            writer.close();
        } catch (Exception e) {
            System.out.println("写文件内容操作出错");
            e.printStackTrace();
        }
    }

    public static void appendMethodB(String fileName, String content) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
