package com.example.collection.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     * 
     * @param arrB
     *            需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception
     *             本方法不处理任何异常，所有异常全部抛出
     */
    private static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * encrypt MD5数字指纹加密函数
     * 
     * @param src
     * @param md5Algorithm
     *            Md5加密算法，可用MD5,SHA-1
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] encrypt(byte[] src, String md5Algorithm)
            throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance(md5Algorithm);
        md5.update(src);
        return md5.digest();
    }

    public static String encrypt(String src) throws NoSuchAlgorithmException,
            Exception {
        byte[] srcBytes = src.getBytes();
        byte[] enBytes = MD5Util.encrypt(srcBytes, "SHA-1");
        return MD5Util.byteArr2HexStr(enBytes);
    }

    public static String md5(String plainText) {
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes("UTF-8"));
            byte b[] = md.digest();

            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // result = buf.toString(); //md5 32bit
        // result = buf.toString().substring(8, 24))); //md5 16bit
        String result = buf.toString().substring(8, 24);
        System.out.println("mdt 16bit: " + buf.toString().substring(8, 24));
        System.out.println("md5 32bit: " + buf.toString());
        return result;// 32位的加密
    }
}
