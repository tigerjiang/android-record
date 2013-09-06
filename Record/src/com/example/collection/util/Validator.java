package com.example.collection.util;

/**
 * @title 验证18位身份证的合法性
 * 
 *        <pre>
 * 基本验证原理是通过第十八位上的字符座位校验码进行验证的：
 * 1.将前面的身份证号码17位数分别乘以不同的系数： 从第一位到第十七位的系数分别为：7. 9 .10 .5. 8. 4. 2. 1. 6. 3. 7.9. 10. 5. 8. 4. 2.
 * 2.将这17位数字和系数相乘的结果相加。
 * 3.用加出来和除以11，看余数是多少？
 * 4余数只可能有0 、1、2、 3、 4、 5、 6、 7、 8、9、 10这11个数字。 其分别对应的最后一位身份证的号码为1 .0. X. 9. 8. 7. 6. 5. 4. 3. 2.
 * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的x。 如果余数是10，身份证的最后一位号码就是2。 　倒数第二位是用来表示性别的
 *  例如：某男性的身份证号码是34052419800101001X。我们要看看这个身份证是不是合法的身份证。 　首先：我们得出，前17位的乘积和是189
 *  然后：用189除以11得出的结果是17 + 2/11，也就是说余数是2。 　最后：通过对应规则就可以知道余数2对应的数字是x。
 * 所以，这是一个合格的身份证号码。
 * </pre>
 */

public class Validator {
    private  String IDNumber;
    private char lastChar;
    private int number[];
    private static int[] coefficient = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10,
        5, 8, 4, 2 };
    private static char[] lastCharArray = new char[] { '1', '0', 'X', '9', '8', '7', '6',
        '5', '4', '3', '2' };
    private static final Validator instance = new Validator();
    private Validator(){
        
    }
    
    public static Validator getInstance(){
        return instance;
    }
    public Validator(String IDNumber) {
        super();
        this.IDNumber = IDNumber;
        this.lastChar = this.getLastChar();
        this.number = this.getIntArray();
    }

    /** * 将身份证的前17位数拆分为单个数字放入整形数组 */
    public int[] getIntArray() {
        String str = this.IDNumber.substring(0, this.IDNumber.length() - 1);
        char ch[] = str.toCharArray();
        int num[] = new int[ch.length];
        for (int i = 0; i < ch.length; i++) {
            num[i] = Integer.parseInt(Character.toString(ch[i]));
        }
        return num;
    }

    /** * 取出身份证最后一位字符 */
    private char getLastChar() {
        return this.IDNumber.charAt(this.IDNumber.length() - 1);
    }

    /**
     * * 身份证号码17位数分别乘以不同的系数。 从第一位到第十七位的系数分别为：7. 9 .10 .5. 8. 4. 2. 1. 6. 3. 7. *
     * 9.10. 5. 8. 4. 2.
     */
    public int MultipliedByCoefficient(int num, int coefficient) {
        return num * coefficient;
    }

    /** * 得到前17位数字与相应的系数相乘后的和 */
    public int getProductSum(int num[], int coefficient[]) {
        int sum = 0;
        for (int i = 0; i < num.length; i++) {
            sum += MultipliedByCoefficient(num[i], coefficient[i]);
        }
        return sum;
    }

    /** * 将得到的和与11取模 */
    public int moduloByEleven() {
        return getProductSum(number, coefficient) % 11;
    }

    /**
     * 将得到取模后的字符与第18位字符进行对比以验证身份证的合法性
     */
    public boolean isLegality(String id) {
        this.IDNumber = id;
        this.lastChar = this.getLastChar();
        this.number = this.getIntArray();
        int index = moduloByEleven();
        if (lastChar == lastCharArray[index]) {
            return true;
        } else {
            return false;
        }
    }
    
}