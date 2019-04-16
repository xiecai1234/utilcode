package com.fingerbeat.utilcode.utils;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiecaibao on 2018/9/4
 */
public class StringUtil {
    /**
     * 判断字符串是否为URL
     *
     * @param string
     * @return true:是URL、false:不是URL
     */
    public static boolean isHttpUrl(String string) {
        boolean isurl = false;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式

        Pattern pat = Pattern.compile(regex.trim());//比对
        Matcher mat = pat.matcher(string.trim());
        isurl = mat.matches();//判断是否匹配
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }

    /**
     * ”^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\d{8}$”这句话其实很简单：
     * <p>
     * ①130-139这十个前三位已经全部开通，后面8位每一位都是0-9之间的任意数；
     * <p>
     * ②14开头的目前只有145、147、149三位，后面8位每一位都是0-9之间的任意数；
     * <p>
     * ③15开头的除了154以外第三位可以随意取，后面8位每一位都是0-9之间的任意数；
     * <p>
     * ④180-189这十个前三位已经全部开通，后面8位每一位都是0-9之间的任意数；
     * <p>
     * ⑤17开头的目前有170、171、173、175、176、177、178这七位，后面8位每一位都是0-9之间的任意数；
     *
     * @param mobileNums
     * @return
     */
    public static boolean isPhoneNum(String mobileNums) {
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        if (TextUtils.isEmpty(mobileNums)) {
            return false;
        } else {
            return mobileNums.matches(telRegex);
        }
    }

    /**
     * 将InputStream中的数据读取为String
     * 参见http://stackoverflow.com/a/309718/2369039 * * @param is InputStream * @param bufferSize buffer大小 * @param encoding String编码 * @return 读取出来的String * @throws IOException
     */
    public static String is2String(InputStream is, int bufferSize, String encoding) throws IOException {
        Reader reader = new InputStreamReader(is, encoding);
        char[] buffer = new char[bufferSize];
        StringBuilder out = new StringBuilder();
        int rsz;
        while ((rsz = reader.read(buffer, 0, bufferSize)) >= 0) {
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    /**
     * 以UTF-8编码读取InputStream为String * @param is InputStream * @return 读取出来的String * @throws IOException
     */
    public static String is2String(InputStream is) throws IOException {
        return is2String(is, 50, "UTF-8");
    }
}
