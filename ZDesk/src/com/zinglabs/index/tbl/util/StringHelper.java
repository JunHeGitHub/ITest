package com.zinglabs.index.tbl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;


public class StringHelper {
    public final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    public final static String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    public final static String DEFAULT_TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

   
    public static boolean isValid(String str) {
        if (str != null && !str.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    
    public static String getValidStr(String str) {
        if (str != null && !str.equals("")) {
            return str;
        } else {
            return "&nbsp;";
        }
    }

   
    public static Object getStringValue(String str) {
        if (str != null && !str.equals("")) {
            return str;
        } else {
            return null;
        }
    }

   
    public static boolean isValid(String[] str) {
        if (str != null && str.length > 0) {
            return true;
        } else {
            return false;
        }
    }

    
    public static boolean isValid(String[] str, int length) {
        if (isValid(str)) {
            if (str.length == length) {
                return true;
            }
        }

        return false;
    }

   
    public static String toFormat(String formatstr) {
        if (formatstr == null) {
            return "";
        }
        return formatstr.toLowerCase();
    }

  
    public static String toFormatMethodName(String formatstr) {
        if (formatstr == null) {
            return "";
        }
        String _Str = formatstr.substring(0, 1).toUpperCase()
                + formatstr.substring(1);
        int _Pos = 0;
        while ((_Pos = _Str.indexOf("_")) > 0) {
            if (_Str.length() > _Pos + 1) {
                _Str = _Str.substring(0, _Pos)
                        + _Str.substring(_Pos + 1, _Pos + 2).toUpperCase()
                        + _Str.substring(_Pos + 2);
            } else {
                _Str = _Str.substring(0, _Pos);
            }
        }
        return _Str;
    }

   
    public static String toFormatName(String formatstr) {
        if (formatstr == null) {
            return "";
        }
        return formatstr.substring(0, 1).toUpperCase() + formatstr.substring(1);
    }

   
    public static String toFormatVariable(String formatstr) {
        if (formatstr == null) {
            return "";
        }
        return formatstr.substring(0, 1).toLowerCase() + formatstr.substring(1);
    }

   
    public static final ArrayList parseStringToArrayList(String strlist,
            String ken) {
        StringTokenizer st = new StringTokenizer(strlist, ken);

        if (strlist == null || strlist.equals("") || st.countTokens() <= 0) {
            return new ArrayList();
        }

        int size = st.countTokens();
        ArrayList<String> strv = new ArrayList<String>();

        for (int i = 0; i < size; i++) {
            String nextstr = st.nextToken();
            if (!nextstr.equals("")) {
                strv.add(nextstr);
            }
        }
        return strv;
    }

   
    public static final String[] SplitString(String szSource, String token) {
        if (szSource == null || token == null) {
            return null;
        }
        StringTokenizer st1 = new StringTokenizer(szSource, token);
        String d1[] = new String[st1.countTokens()];
        for (int x = 0; x < d1.length; x++) {
            if (st1.hasMoreTokens()) {
                d1[x] = st1.nextToken().trim();
            }
        }
        return d1;
    }

   
    public static final String replace(String line, String oldString,
            String newString) {
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }

    public static final String toCNString(String str) {
        if (str == null) {
            return null;
        }
        try {
            byte[] bt = str.getBytes("ISO8859-1");
            str = new String(bt, "GB2312");
        } catch (Exception ex) {
            return null;
        }
        return str;
    }

    public static final String convertEncode(String str, String from, String to) {
        if (str == null || from == null || to == null) {
            return null;
        }
        try {
            byte[] bt = str.getBytes(from);
            str = new String(bt, to);
        } catch (Exception e) {
            return null;
        }
        return str;
    }

    public static String writeObject(Serializable obj) throws Exception {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(o);
        out.writeObject(obj);
        out.flush();
        out.close();
        o.close();
        byte[] data = o.toByteArray();

        String str = "";
        for (int i = 0; i < data.length; i++) {
            if (i == 0) {
                str += data[i];
            } else {
                str += "." + data[i];
            }
        }
        return str;
    }

    public static Object readObject(String str) throws Exception {
        StringTokenizer st = new StringTokenizer(str, ".");
        ArrayList<String> al = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            al.add(st.nextToken());
        }

        byte[] bt = null;
        if (!al.isEmpty()) {
            String[] s_bt = new String[al.size()];
            al.toArray(s_bt);
            bt = new byte[al.size()];
            for (int i = 0; i < bt.length; i++) {
                bt[i] = Byte.parseByte(s_bt[i]);
            }
        }
        ByteArrayInputStream i = new ByteArrayInputStream(bt);
        ObjectInputStream in = new ObjectInputStream(i);
        Object obj = in.readObject();
        in.close();
        i.close();
        return obj;
    }

    public static String subString(String source, String tag, int pos) {

        int start = source.indexOf("<" + tag + ">", pos) + tag.length() + 2;
        int end = source.indexOf("</" + tag + ">", pos + tag.length() + 3);
        if (start == tag.length() + 1) { // û�ҵ���ʼ��־
            return "";
        }
        return source.substring(start, end);
    }

    public static HashMap split(String source, String tag) {
        int pos = 0;
        String temp = "";
        HashMap<String,String> hash = new HashMap<String,String>();
        while (!(temp = subString(source, tag, pos)).equals("")) {
            pos = source.indexOf(temp) + temp.length();
            int _pos = temp.indexOf("$");
            hash.put(temp.substring(0, _pos), temp.substring(_pos + 1));
        }
        return hash;
    }

   
    public static String ArrayToString(Object[] objs, String prefix,
            String delim) {
        StringBuffer sb = new StringBuffer();

        int len = objs == null ? 0 : objs.length;

        if (len > 0) {
            sb.append(prefix).append(objs[0]);

            for (int i = 1; i < len; i++) {
                sb.append(delim).append(prefix).append(objs[i]);
            }

        }

        return sb.toString();
    }

    public String firstCharToUpperCase(String name) {
        return new StringBuffer().append(Character.toUpperCase(name.charAt(0)))
                .append(name.substring(1)).toString();
    }

    public String firstCharToLowerCase(String name) {
        return new StringBuffer().append(Character.toLowerCase(name.charAt(0)))
                .append(name.substring(1)).toString();
    }

    
    public static String convertByLetter(String letters) {
        return convertByLetter(letters, 1, -1);
    }

    public static String convertByLetter(String letters, int increament) {
        return convertByLetter(letters, increament, -1);
    }

  
    public static String convertByLetter(String letters, int increament, int pos) {
        int len = letters.length();
        int value;
        char letter;
        String strPlace;

        if (pos == -1) {
            pos = len;
        }
        if (pos == 0) {
            letters = "A" + letters;
            return letters;
        }
        letter = letters.charAt(pos - 1);
        value = letter + increament;
        strPlace = letters.substring(pos - 1, pos);
        if (value - 90 > 0) {
            strPlace = strPlace.replace(letter, (char) (64 + (value - 90)));
            // ת��Ϊ��д��ĸ
        } else {
            strPlace = strPlace.replace(letter, (char) value);
        }
        letters = letters.substring(0, pos - 1) + strPlace
                + letters.substring(pos);
        if (value - 90 > 0) { // �����̿
            letters = convertByLetter(letters, 1, pos - 1);
        }

        return letters;

    }

   
    public static String copyValue(char c, int count) {
        String string = new String("");
        if (count < 1) {
            return "";
        }
        for (int i = 0; i < count; i++) {
            string += c;
        }
        return string;
    }

    
    public static String replace(String source, char c, int pos) {
        String string;
        char[] sa = source.toCharArray();
        for (int i = 0; i < sa.length; i++) {
            if (i == pos) {
                sa[i] = c;
            }
        }
        string = new String(sa);
        return string;
    }

   
    final static String CARRY_SHI = "ʰ";

    final static String CARRY_BAI = "��";

    final static String CARRY_QIAN = "Ǫ";

    final static String CARRY_WAN = "��";

    final static String CARRY_YI = "��";

    final static String CUR_YUAN = "Ԫ";

    final static String CUR_FEN = "��";

    final static String CUR_JIAO = "��";

    final static String[] digits = { "��", "Ҽ", "�E", "��", "��", "��", "½", "��",
            "��", "��", };

    public static String getValueOfCurrency2CN(String priValue) {
        String value = String.valueOf(priValue);
        int dot_pos = String.valueOf(value).indexOf('.');
        String int_value;
        String fraction_value;
        if (dot_pos == -1) {
            int_value = value;
            fraction_value = "00";
        } else {
            int_value = value.substring(0, dot_pos);
            fraction_value = value.substring(dot_pos + 1, priValue.length())
                    + "00".substring(0, 2);
        }

        StringBuffer cn_currency = new StringBuffer();

        int len = int_value.length();
        boolean zero_flag = false;

        // if (len == 16)
        // int_value.substring(0, 15);
        if (len > 12 && len <= 16) {
            String temp = int_value.substring(0, len - 12);
            if (Integer.parseInt(temp) != 0) {
                cn_currency.append(
                        cell2CH(int_value.substring(0, len - 12), zero_flag))
                        .append(CARRY_WAN);
            }
            int_value = int_value.substring(len - 12, len);
            len = 12;
            zero_flag = true;
        }
        if (len > 8 && len <= 12) {
            String temp = int_value.substring(0, len - 8);
            if (Integer.parseInt(temp) != 0) {
                cn_currency.append(
                        cell2CH(int_value.substring(0, len - 8), zero_flag))
                        .append(CARRY_YI);
            }
            int_value = int_value.substring(len - 8, len);
            len = 8;

        }
        if (len > 4 && len <= 8) {
            // zero_flag = false;
            String temp = int_value.substring(0, len - 4);
            if (Integer.parseInt(temp) != 0) {
                cn_currency.append(
                        cell2CH(int_value.substring(0, len - 4), zero_flag))
                        .append(CARRY_WAN);
            }
            int_value = int_value.substring(len - 4, len);
            len = 4;
            zero_flag = true;
        }

        cn_currency.append(cell2CH(int_value.substring(0, len), zero_flag));

        if (!cn_currency.toString().equals("")) {
            cn_currency.append(CUR_YUAN);

        }
        int t = Character.getNumericValue(fraction_value.charAt(0));
        if (t != 0) {
            cn_currency.append(digits[t]).append(CUR_JIAO);

        }
        t = Character.getNumericValue(fraction_value.charAt(1));
        if (t != 0) {
            cn_currency.append(digits[t]).append(CUR_FEN);

        }
        String returnVaule = cn_currency.toString();
        returnVaule = StringHelper.replace(returnVaule, "����", "��");
        returnVaule = StringHelper.replace(returnVaule, "��Ԫ", "Ԫ");
        returnVaule = StringHelper.replace(returnVaule, "����", "��");
        returnVaule = StringHelper.replace(returnVaule, "����", "��");
        if (!returnVaule.endsWith(CUR_FEN)) {
            returnVaule = returnVaule + "��";
        }
        return returnVaule;
    }

    private static String cell2CH(String cellValue, boolean zero_flag) {

       
        StringBuffer cellBuffer = new StringBuffer();
        cellValue = removeZero(cellValue);
        int pos = cellValue.length();
        int digit;
        if (pos == 4) {
            digit = Character.getNumericValue(cellValue.charAt(0));
            if (digit != 0) {
                cellBuffer.append(digits[digit]).append(CARRY_QIAN);
            } else {
                cellBuffer.append(digits[0]);
            }
            pos = pos - 1;
            cellValue = cellValue.substring(1, 4);
        }
        if (pos == 3) {
            digit = Character.getNumericValue(cellValue.charAt(0));
            if (digit != 0) {
                cellBuffer.append(digits[digit]).append(CARRY_BAI);
            } else {
                cellBuffer.append(digits[0]);
            }
            pos = pos - 1;
            cellValue = cellValue.substring(1, 3);
        }
        if (pos == 2) {
            digit = Character.getNumericValue(cellValue.charAt(0));
            if (digit != 0) {
                cellBuffer.append(digits[digit]).append(CARRY_SHI);
            } else {
                cellBuffer.append(digits[0]);
            }
            pos = pos - 1;
            cellValue = cellValue.substring(1, 2);
        }
        if (pos == 1) {
            digit = Character.getNumericValue(cellValue.charAt(0));
            if (digit != 0) {
                cellBuffer.append(digits[digit]);
            }
        }

        if (zero_flag && cellBuffer.length() >= 2
                && cellBuffer.charAt(1) != 'Ǫ') {
            cellBuffer.insert(0, "��");
        }
        if (zero_flag && cellBuffer.length() == 1) {
            cellBuffer.insert(0, "��");
        }
        return cellBuffer.toString();
    }

    private static String removeZero(String _cellValue) {
        String cellValue = _cellValue;
        if (cellValue.startsWith("0")) {
            cellValue = cellValue.substring(1);
            return cellValue = removeZero(cellValue);
        } else {
            return cellValue;
        }
    }

    public static Timestamp getTimestamp(String timestamp, String pattern) {
        Timestamp t = null;

        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);

        try {
            t = new Timestamp(sdf.parse(timestamp).getTime());
        } catch (Exception e) {
        }

        return t;
    }

   
    public static String replace(String line, String flag, String[] param) {
        ArrayList list = parseStringToArrayList(line, flag);
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            if (!StringHelper.isValid(param, list.size() - 1)) {
                throw new IllegalArgumentException("param is invalid");
            }

            for (int i = 0; i < list.size(); i++) {
                String str = (String) list.get(i);
                sb.append(str);
                if (i < list.size() - 1) {
                    sb.append(param[i]);
                }
            }

        }
        return sb.toString();
    }

   
    public static String trimStr(String src) {
        if (isValid(src)) {
            return src.trim();
        } else {
            return "";
        }
    }

   
    public static String getCurNo(int curno, int length, String fillStr) {
        int temp = curno;
        StringBuffer sb = new StringBuffer(length);
        int count = 0;
        while (curno / 10 != 0) {
            curno = curno / 10;
            count++;
        }
        int size = length - count - 1;
        for (int i = 0; i < size; i++) {
            sb.append(fillStr);
        }
        sb.append(temp);
        return sb.toString();
    }

   
    public static boolean containValue(String[] src, String value) {
        if (!StringHelper.isValid(src)) {
            return false;
        }
        boolean result = false;
        for (int i = 0; i < src.length; i++) {
            if (src[i].equals(value)) {
                result = true;
                break;
            }
        }
        return result;
    }

   
    public static String MD5Encode(String origin) {
        String resultString = null;

        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString
                    .getBytes()));
        } catch (Exception ex) {

        }
        return resultString;
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
