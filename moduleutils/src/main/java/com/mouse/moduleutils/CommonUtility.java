package com.mouse.moduleutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaScannerConnection;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 简单工具类
 */
public class CommonUtility {

    /**
     * 普通操作工具类
     */
    public static final class Utility {
        /**
         * 获取指定范围内的随机数
         *
         * @param minNum
         * @param maxNum
         * @return
         */
        public static int getRandom(int minNum, int maxNum) {
            int s = (int) (minNum + Math.random() * (maxNum - minNum + 1));
            return s;
        }

        /**
         * 生成min - max位随机数字和字母     * @param min
         */
        public static String getStringRandom(int min, int max) {
            Random random1 = new Random();
            int s = random1.nextInt(max) % (max - min + 1) + min;
            String val = "";
            Random random = new Random();

            //参数length，表示生成几位随机数
            for (int i = 0; i < s; i++) {
                String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
                //输出字母还是数字
                if ("char".equalsIgnoreCase(charOrNum)) {
                    //输出是大写字母还是小写字母
                    int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                    val += (char) (random.nextInt(26) + temp);
                } else if ("num".equalsIgnoreCase(charOrNum)) {
                    val += String.valueOf(random.nextInt(10));
                }
            }
            return val;
        }

        /**
         * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
         * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
         *
         * @param anchorView  呼出window的view
         * @param contentView window的内容布局
         * @return window显示的左上角的xOff, yOff坐标
         */
        public static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
            final int windowPos[] = new int[2];
            final int anchorLoc[] = new int[2];
            // 获取锚点View在屏幕上的左上角坐标位置
            anchorView.getLocationOnScreen(anchorLoc);
            final int anchorHeight = anchorView.getHeight();
            // 获取屏幕的高宽
            final int screenHeight = UIUtility.getScreenHeight(anchorView.getContext());
            final int screenWidth = UIUtility.getScreenWidth(anchorView.getContext());
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            // 计算contentView的高宽
            final int windowHeight = contentView.getMeasuredHeight();
            final int windowWidth = contentView.getMeasuredWidth();
            // 判断需要向上弹出还是向下弹出显示
            final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
            if (isNeedShowUp) {
                windowPos[0] = screenWidth - windowWidth;
                windowPos[1] = anchorLoc[1] - windowHeight;
            } else {
                windowPos[0] = screenWidth - windowWidth;
                windowPos[1] = anchorLoc[1] + anchorHeight;
            }
            return windowPos;
        }

        /**
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
        public static int dip2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        /**
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         */
        public static int px2dip(Context context, float pxValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }

        /**
         * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
         * 定精度，以后的数字四舍五入。
         *
         * @param v1    被除数
         * @param v2    除数
         * @param scale 表示表示需要精确到小数点以后几位。
         * @return 两个参数的商
         */
        public static double divToDouble(double v1, double v2, int scale) {
            if (scale < 0) {
                throw new IllegalArgumentException(
                        "The scale must be a positive integer or zero");
            }
            BigDecimal b1 = new BigDecimal(Double.toString(v1));
            BigDecimal b2 = new BigDecimal(Double.toString(v2));
            return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        /**
         * md5加密
         *
         * @param string
         * @return
         */
        public static String getMD5Str(String string) {
            if (TextUtils.isEmpty(string)) {
                return "";
            }
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
                byte[] bytes = md5.digest(string.getBytes());
                String result = "";
                for (byte b : bytes) {
                    String temp = Integer.toHexString(b & 0xff);
                    if (temp.length() == 1) {
                        temp = "0" + temp;
                    }
                    result += temp;
                }
//                return result.toUpperCase();
                return result;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return "";
        }

        /**
         * utf-8 转换成 unicode
         *
         * @param inStr
         * @return
         * @author fanhui
         * 2007-3-15
         */
        public static String utf8ToUnicode(String inStr) {
            char[] myBuffer = inStr.toCharArray();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < inStr.length(); i++) {
                Character.UnicodeBlock ub = Character.UnicodeBlock.of(myBuffer[i]);
                if (ub == Character.UnicodeBlock.BASIC_LATIN) {
                    //英文及数字等
                    sb.append(myBuffer[i]);
                } else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                    //全角半角字符
                    int j = (int) myBuffer[i] - 65248;
                    sb.append((char) j);
                } else {
                    //汉字
                    short s = (short) myBuffer[i];
                    String hexS = Integer.toHexString(s);
                    String unicode = "\\u" + hexS;
                    sb.append(unicode.toLowerCase());
                }
            }
            return sb.toString();
        }

        /**
         * 获取百分数
         *
         * @param countNumberSize 总数
         * @param size            数量
         * @param lth             需要保留的小数
         * @return
         */
        public static String getPercentage(int countNumberSize, int size, int lth) {
            NumberFormat numberFormat = NumberFormat.getInstance();
            if (lth > 2) {
                numberFormat.setMaximumFractionDigits(2);
            } else if (lth >= 0 && lth < 3) {
                numberFormat.setMaximumFractionDigits(lth);
            }
            return numberFormat.format((float) size / (float) countNumberSize * 100);
        }

        /**
         * 获取屏幕宽度
         *
         * @param context
         * @return
         */
        public static int getPhoneWidth(Context context) {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            return width;
        }

        /**
         * method desc：判断参数值是否为空 null，空字符串，或者全部空格字符串或者"null"字符串都视为空
         *
         * @param o
         * @return
         */
        public static boolean isNull(Object o) {
            try {
                return null == o || "".equals(o.toString().replaceAll(" ", ""))
                        || "null".equals(o.toString());
            } catch (Exception e) {
            }
            return true;
        }

        /**
         * 验证邮箱地址是否正确
         *
         * @param email
         * @return
         */
        public static boolean checkEmail(String email) {
            boolean flag = false;
            try {
                String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email);
                flag = matcher.matches();
            } catch (Exception e) {
                flag = false;
            }

            return flag;
        }

        public static boolean checkUrl(String url) {
            if (TextUtils.isEmpty(url)) {
                return false;
            }
            return url.startsWith("http://") || url.startsWith("https://");
        }

        /**
         * 过滤特殊字符
         *
         * @param str
         * @return
         */
        public static String specialStr(String str) {
            String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.replaceAll("").trim();
        }

        /**
         * 过滤特殊字符
         *
         * @param str
         * @return
         */
        public static boolean isHasSpecialStr(String str) {
            String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.matches();
        }

        public static boolean isLetter(char c) {
            int k = 0x80;
            return c / k == 0 ? true : false;
        }

        /**
         * 转换钱
         *
         * @param monetInt
         * @return
         */
        public static String formatMoney(int monetInt) {
            float num = (float) monetInt / 100;
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数
            df.format(num).length();
            return UIUtility.subZeroAndDot(df.format(num));
        }

        /**
         * 对double进行处理
         *
         * @param f      需要处理的数据
         * @param length 保留的小数位数
         * @return
         */
        public static double formatDouble(double f, int length) {
            BigDecimal bigDecimal = new BigDecimal(f);
            return bigDecimal.setScale(length, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
        }

        public static double formatStr2Double(String str, int length) {
            return formatDouble(Double.parseDouble(str), length);
        }

        public static String formatStr2Num(String str, int length) {
            if (!Utility.isNull(str)) {
                return formatDouble(Double.parseDouble(str), length) + "";
            } else {
                return null;
            }
        }

        public static String formatDouble2String(double f, int length) {
            return formatDouble(f, length) + "";
        }

        public static String formatDouble2String(float f, int length) {
            return formatDouble((double) f, length) + "";
        }

        public static String formatDouble2String(double f) {
            if (f % 1.00 == 0) {
                return CommonUtility.UIUtility.formatString((int) f);
            }
            if (f % 0.1 == 0) {
                return formatDouble2String(f, 1);
            }
            return formatDouble2String(f, 2);
        }


        public static int getRandomNum() {
            return (int) (Math.random() * (10000 - 100) + 1);
        }


        public static String encode(String param) {
            try {
                return URLEncoder.encode(param, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        public static String decode(String param) {
            try {
                return URLEncoder.encode(param, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        public static Type getType(Class clazz, Class targetClass) {
            Class superClass = clazz.getSuperclass();
            if (superClass.getSimpleName().equals(targetClass.getSimpleName())) {
                return clazz.getGenericSuperclass();
            }
            return getType(superClass, targetClass);
        }

        public static Field[] getAllField(Class<?> clazz) {
            List<Field> fieldList = new ArrayList<>();
            Field[] dFields = clazz.getDeclaredFields();
            if (null != dFields && dFields.length > 0) {
                fieldList.addAll(Arrays.asList(dFields));
            }

            Class<?> superClass = clazz.getSuperclass();
            if (superClass != Object.class) {
                Field[] superFields = getAllField(superClass);
                if (null != superFields && superFields.length > 0) {
                    for (Field field : superFields) {
                        if (!isContain(fieldList, field)) {
                            fieldList.add(field);
                        }
                    }
                }
            }
            Field[] result = new Field[fieldList.size()];
            fieldList.toArray(result);
            return result;
        }

        /**
         * 检测Field List中是否已经包含了目标field
         *
         * @param fieldList
         * @param field     带检测field
         * @return
         */
        public static boolean isContain(List<Field> fieldList, Field field) {
            for (Field temp : fieldList) {
                if (temp.getName().equals(field.getName())) {
                    return true;
                }
            }
            return false;
        }

        public static String formatHtmlSource(String source) {
            return source.replaceAll("&amp;", "&")
                    .replaceAll("&nbsp;", "    ")
                    .replaceAll("&quot;", "\"")
                    .replaceAll("&lt;", "<")
                    .replaceAll("&gt;", ">")
                    .replaceAll("<html>", "<html><head><style>body{width:100%;} body img {max-width:100%;width:100%;}</style></head>");
        }

        /**
         * base64转换图片
         *
         * @param string
         * @return
         */
        public static Bitmap stringtoBitmap(String string) {
            //将字符串转换成Bitmap类型
            Bitmap bitmap = null;
            try {
                byte[] bitmapArray;
//                bitmapArray = Base64.decode(string, Base64.NO_WRAP);
                bitmapArray = Base64.decode(string, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        /**
         * 文件转base64字符串
         *
         * @param file
         * @return
         */
        public static String fileToBase64(File file) {
            String base64 = null;
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                byte[] bytes = new byte[in.available()];
                int length = in.read(bytes);
                base64 = Base64.encodeToString(bytes, 0, length, Base64.NO_WRAP);
//                base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return base64;
        }

        /**
         * bitmap转为base64
         *
         * @param bitmap
         * @return
         */
        public static String bitmapToBase64(Bitmap bitmap) {
            String result = null;
            ByteArrayOutputStream baos = null;
            try {
                if (bitmap != null) {
                    baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                    baos.flush();
                    baos.close();

                    byte[] bitmapBytes = baos.toByteArray();
                    result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
//                    result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (baos != null) {
                        baos.flush();
                        baos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        /**
         * Byte转Bitmap
         *
         * @param b
         * @return
         */
        public static Bitmap Bytes2Bimap(byte[] b) {
            if (b.length != 0) {
                return BitmapFactory.decodeByteArray(b, 0, b.length);
            } else {
                return null;
            }
        }

    }

    /**
     * Debug工具类
     */
    public static class DebugLog {
        private static final boolean DEBUG = true;
        static String className;
        static String methodName;
        static int lineNumber;

        public static boolean isDebuggable() {
            return DEBUG;
        }

        private static String createLog(Object log) {
            return CommonUtility.UIUtility.formatString(className, ":", "[", methodName, ":", lineNumber, "]", log);
        }

        private static String createLogWithClassName(Object log) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("[");
            buffer.append(className);
            buffer.append(":");
            buffer.append(methodName);
            buffer.append(":");
            buffer.append(lineNumber);
            buffer.append("]");
            buffer.append(log);

            return buffer.toString();
        }

        private static void getMethodNames(StackTraceElement[] sElements) {
            className = sElements[1].getFileName();
            methodName = sElements[1].getMethodName();
            lineNumber = sElements[1].getLineNumber();
        }

        public static void e(Object message) {
            if (!isDebuggable()) {
                return;
            }

            // Throwable instance must be created before any methods
            getMethodNames(new Throwable().getStackTrace());
            Log.e(className, createLog(message));
        }

        public static void e(String key, Object message) {
            if (Utility.isNull(message)) {
                message = "null";
            }
            if (!isDebuggable())
                return;

            // Throwable instance must be created before any methods
            getMethodNames(new Throwable().getStackTrace());
            Log.e(key, createLogWithClassName(message));
        }


        public static void i(Object message) {
            if (!isDebuggable())
                return;

            getMethodNames(new Throwable().getStackTrace());
            Log.i(className, createLog(message));
        }

        public static void d(Object message) {
            if (!isDebuggable())
                return;

            getMethodNames(new Throwable().getStackTrace());
            Log.d(className, createLog(message));
        }

        public static void v(Object message) {
            if (!isDebuggable())
                return;

            getMethodNames(new Throwable().getStackTrace());
            Log.v(className, createLog(message));
        }

        public static void w(Object message) {
            if (!isDebuggable())
                return;

            getMethodNames(new Throwable().getStackTrace());
            Log.w(className, createLog(message));
        }

        public static void wtf(Object message) {
            if (!isDebuggable())
                return;

            getMethodNames(new Throwable().getStackTrace());
            Log.wtf(className, createLog(message));
        }

    }

    /**
     * 跟用户界面相关的操作工具类
     */
    public static final class UIUtility {

        /**
         * 实现文本复制功能
         * add by wangqianzhou
         *
         * @param content
         */
        public static void textCopy(String content, Context context) {
            // 得到剪贴板管理器
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content.trim());
        }

        /**
         * 实现粘贴功能
         * add by wangqianzhou
         *
         * @param context
         * @return
         */
        public static String textPaste(Context context) {
            // 得到剪贴板管理器
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            return cmb.getText().toString().trim();
        }

        /**
         * 比较当前时间和服务器返回时间大小
         *
         * @param nowDate     前一个时间
         * @param compareDate 后一个时间
         * @return
         */
        public static boolean compareDate(String nowDate, String compareDate) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                Date now = df.parse(nowDate);
                Date compare = df.parse(compareDate);
                if (now.before(compare)) {
                    return true;
                } else {
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }

        /**
         * 秒数换算时分秒
         *
         * @param second
         * @return
         */
        public static String getDateTimeCountSencode(int second) {
            int day = 0;
            int h = 0;
            int d = 0;
            int s = 0;
//            int temp = second % 3600;
            int temp = 0;
            if (second > (24 * 3600)) {
                //天数
                day = second / (24 * 3600);
                temp = (second - (day * ((24 * 3600)))) % 3600;
                second = second - (day * (24 * 3600));
            } else {
                temp = second % 3600;
            }
            if (second > 3600) {
                h = second / 3600;
                if (temp != 0) {
                    if (temp > 60) {
                        d = temp / 60;
                        if (temp % 60 != 0) {
                            s = temp % 60;
                        }
                    } else {
                        s = temp;
                    }
                }
            } else {
                d = second / 60;
                if (second % 60 != 0) {
                    s = second % 60;
                }
            }
            return day + "-" + h + "-" + d + "-" + s;
        }

        /**
         * 获取时间差
         *
         * @return
         */
        public static String getDateTimeCount(String liveDate, String serviceDate) {
            Date Date1 = null;
            Date Date2 = null;
            try {
                //1.	设置两个时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date1 = formatter.parse(liveDate);
                Date2 = formatter.parse(serviceDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //Date.getTime()可以得到1970年01月1日0点零分以来的毫秒数,
            //2.用来获取两个时间相差的毫秒数
            long l = Date1.getTime() - Date2.getTime();
            //3.分别计算相差的天、小时、分、秒
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
//            System.out.println(day + "天");
//            System.out.println(day + "天" + hour + "小时");
//            System.out.println(day + "天" + hour + "小时" + min + "分");
//            System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒");
//            return day * 24 + hour + "-" + min + "-" + s;
            return day + "-" + hour + "-" + min + "-" + s;
        }

        public static String getCurrentTime() {
            Date dd = new Date();
            //格式化
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sim.format(dd);
        }

        public static String getCurrentTimeYearMonDay() {
            Date dd = new Date();
            //格式化
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
            return sim.format(dd);
        }

        /**
         * 获取InputMethodManager，隐藏软键盘
         *
         * @param context
         * @param token
         */
        public static boolean hideKeyboard(Context context, IBinder token) {
            if (token != null) {
                InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                return im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return false;
        }

        /**
         * 时间字符串转换时间戳
         *
         * @param date yyyy-MM-dd HH:mm:ss 格式
         * @return
         */
        public static String date2TimeStampMillon(String date) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return String.valueOf(sdf.parse(date).getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        /**
         * 时间字符串转换时间戳
         *
         * @param date yyyy-MM-dd HH:mm:ss 格式
         * @return
         */
        public static String date2TimeStamp(String date) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return String.valueOf(sdf.parse(date).getTime() / 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        /**
         * 时间戳转换时间
         *
         * @param milSecond
         * @return
         */
        public static String date2StrToTimeMillon(long milSecond) {
            Date date = new Date(milSecond);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(date);
        }

        /**
         * 时间戳转换时间
         *
         * @param milSecond
         * @return
         */
        public static String date2StrToTime(long milSecond) {
            Date date = new Date(milSecond * 1000);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(date);
        }

        /**
         * 设置View Enable
         *
         * @param view   View
         * @param enable 是否可以点击
         */
        public static void setViewEnable(View view, boolean enable) {
            view.setEnabled(enable);
        }


        /**
         * 设置editetxt length
         *
         * @param
         */
        public static void setEtCoustomLength(EditText editText, int length) {
            if (length > 0) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
            }
        }

        /**
         * TextView设置html表情文本
         */
        public static void setHtmlText(TextView view, String content) {
            if (view instanceof TextView) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    view.setText(Html.fromHtml(content));
                }
            }
        }

        /**
         * 去除小数点后面的0
         *
         * @param s
         * @return
         */
        public static String subZeroAndDot(String s) {
            if (s.indexOf(".") > 0) {
                s = s.replaceAll("0+?$", "");//去掉多余的0
                s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
            }
            return s;
        }

        /**
         * 用于判断点击屏幕外面区域,是否隐藏键盘
         *
         * @param v
         * @param event
         * @return true 执行隐藏键盘 ,false不隐藏
         */
        public static boolean isShouldHideKeyboard(View v, MotionEvent event) {
            if (v != null && (v instanceof EditText)) {
                int[] l = {0, 0};
                v.getLocationInWindow(l);
                int left = l[0],
                        top = l[1],
                        bottom = top + v.getHeight(),
                        right = left + v.getWidth();
                if (event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom) {
                    // 点击EditText的事件，忽略它。
                    return false;
                } else {
                    v.clearFocus();
                    return true;
                }
            }
            // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
            return false;
        }

        static View.OnClickListener mCoverViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClick) {
                    ViewGroup parent = (ViewGroup) v.getParent();
                    if (!Utility.isNull(parent)) {
                        View view = (View) v.getTag(); //获取添加的view
                        removeView(parent, v);
                        removeView(parent, view);
                    }
                }
            }
        };

        public static void inflate(int layout, ViewGroup group) {
            ((LayoutInflater) group.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout, group);
        }

        public static View inflate(Context context, int layout) {
            return LayoutInflater.from(context).inflate(layout, null);
        }

        private static Ringtone mRing;
        //遮罩和临时view变量
        private static View mCoverView = null, mTargetView = null;
        //是否点击cover后dismiss
        private static boolean isClick = false;


        public static float getDimensionPixelSize(Context context, int dimen) {
            return context.getResources().getDimensionPixelSize(dimen);
        }

        /**
         * 获取view的坐标
         *
         * @param view
         * @return
         */
        public static int[] getLocation(View view) {
            int[] location = new int[2];
            view.getLocationInWindow(location);
            return location;
        }

        public static int[] getLocationAlignParent(View view) {
            int[] location = new int[2];
            location[0] = view.getLeft();
            location[1] = view.getTop();
            return location;
        }

        /**
         * method desc：显示提示
         *
         * @param context
         * @param str
         */
        public static void toast(Context context, String str) {
            if (Utility.isNull(str)) {
                return;
            }
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
//            try {
//                CustomToast.showToast(context, str, Toast.LENGTH_SHORT);
//            } catch (Exception e) {
//            }
        }

        public static void toast(Context context, int strRes) {
            toast(context, context.getString(strRes));
        }

        public static void toastLong(Context context, String str) {
            if (Utility.isNull(str)) {
                return;
            }
            Toast.makeText(context, str, Toast.LENGTH_LONG).show();
//            try {
//                CustomToast.showToast(context, str, Toast.LENGTH_LONG);
//            } catch (Exception e) {
//            }
        }

        public static void toastLong(Context context, int strRes) {
            toastLong(context, context.getString(strRes));
        }


        /**
         * method desc：获取对应应用的service是否运行
         *
         * @param context
         * @param serviceClass
         * @return
         */
        public static boolean serviceIsRunning(Context context,
                                               Class serviceClass) {
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(Activity.ACTIVITY_SERVICE);
            List<RunningServiceInfo> mServiceList = mActivityManager
                    .getRunningServices(100);
            for (RunningServiceInfo service : mServiceList) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 限制数字为小数点#{position}位
         *
         * @param text
         * @param position
         */
        public static void numLimit(Editable text, int position) {
            int d = text.toString().indexOf(".");
            if (d < 0) return;
            if (text.length() - 1 - d > position) {
                text.delete(d + position + 1, d + position + 2);
            } else if (d == 0) {
                text.delete(d, d + 1);
            }
        }

        /**
         * 判断当前应用是否在前台活动
         *
         * @param ctx
         * @return
         */
        @RequiresApi(api = Build.VERSION_CODES.Q)
        public static boolean isAppOnForeground(Context ctx) {
            List<ActivityManager.RunningTaskInfo> tasksInfo = ((ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
            if (tasksInfo.size() > 0) {
//                Log.i("mark", "top Activity = " + tasksInfo.get(0).topActivity.getPackageName());
                // 应用程序位于堆栈的顶层
                if (ctx.getPackageName().equals(tasksInfo.get(0).topActivity.getPackageName())) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 判断某个activity当前是否在在栈顶
         *
         * @param mContext
         * @param cls      需要判断的界面
         * @return
         */
        @RequiresApi(api = Build.VERSION_CODES.Q)
        public static boolean isActivityOnTop(Context mContext,
                                              Class<? extends Activity> cls) {
            return isActivityOnTop(mContext, cls.getName());
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        private static boolean isActivityOnTop(Context context, String clsName) {
            String name = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                name = getTopActivityFullName(context);
            }
            if (clsName.equals(name)) {
                return true;
            }
            return false;
        }

        /**
         * 获取当前活动的activity的名字，包含保命
         *
         * @param context
         * @return
         */
        @RequiresApi(api = Build.VERSION_CODES.Q)
        public static String getTopActivityFullName(Context context) {
            ActivityManager manager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            String name = manager.getRunningTasks(1).get(0).topActivity
                    .getClassName();
            return name;
        }

        /**
         * 获取当前活动的activity的名字，不包含保命
         *
         * @param context
         * @return
         */
        @RequiresApi(api = Build.VERSION_CODES.Q)
        public static String getTopActivityClassName(Context context) {
            ActivityManager manager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName componentName = manager.getRunningTasks(1).get(0).topActivity;
            String name = componentName.getClassName().substring(componentName.getClassName().lastIndexOf(".") + 1);
            return name;
        }

        /**
         * 获取版本名称
         *
         * @return 当前应用的版本名称
         */
        public static String getVersionName(Context context) {
            try {
                PackageManager manager = context.getPackageManager();
                PackageInfo info = manager.getPackageInfo(
                        context.getPackageName(), 0);
                String version = info.versionName;
                return version;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        public static String getApplicationName(Context context) {
            PackageManager packageManager = null;
            ApplicationInfo applicationInfo = null;
            try {
                packageManager = context.getApplicationContext().getPackageManager();
                applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                applicationInfo = null;
            }
            String applicationName =
                    (String) packageManager.getApplicationLabel(applicationInfo);
            return applicationName;
        }

        /**
         * 获取版本号
         *
         * @return 当前应用的版本号
         */
        public static int getVersionCode(Context context) {
            try {
                PackageManager manager = context.getPackageManager();
                PackageInfo info = manager.getPackageInfo(
                        context.getPackageName(), 0);
                int version = info.versionCode;
                return version;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        public static void removeView(final Object container, final View view,
                                      int anim) {
            if (!Utility.isNull(view) && Thread.currentThread() == Looper.getMainLooper().getThread()) {
                Animation animation = AnimationUtils.loadAnimation(view.getContext(),
                        anim);
                animation.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ViewGroup rootView = null;
                        if (container instanceof Activity) {
                            rootView = (ViewGroup) ((Activity) container).getWindow()
                                    .getDecorView().findViewById(android.R.id.content);
                        } else if (container instanceof ViewGroup) {
                            rootView = (ViewGroup) container;
                        } else {
                            throw new IllegalArgumentException("container should be a view group.");
                        }
                        rootView.removeView(view);
                        rootView.removeView(mCoverView);
                    }
                });
                view.startAnimation(animation);
            }
        }

        public static void removeView(Object container, View view) {
            if (!Utility.isNull(view) && Thread.currentThread() == Looper.getMainLooper().getThread()) {
                try {
                    ViewGroup rootView = null;
                    if (container instanceof Activity) {
                        rootView = (ViewGroup) ((Activity) container).getWindow()
                                .getDecorView().findViewById(android.R.id.content);
                    } else if (container instanceof ViewGroup) {
                        rootView = (ViewGroup) container;
                    } else {
                        throw new IllegalArgumentException("container should be a container.");
                    }
                    rootView.removeView(view);
                    rootView.removeView(mCoverView);
                } catch (Exception e) {
                    /**
                     *
                     * 已经判断了是否在主线程了
                     * 可能出现 Only the original thread that created a view hierarchy can touch its views.
                     */
                }
            }
        }

        /**
         * 模拟遮罩的点击
         */
        public static void coverPerfomClick() {
            if (!Utility.isNull(mCoverView)) {
                mCoverView.performClick();
            }
        }

        public static boolean isShown(View view) {
            return view == mTargetView && UIUtility.isVisible(view);
        }


        /**
         * method desc：隐藏键盘
         *
         * @param view
         */
        public static void hideKeyboard(View view) {
            if (!Utility.isNull(view)) {
                if (view instanceof EditText) {
                    view.requestFocus();
                }
                InputMethodManager imm = (InputMethodManager) view.getContext()
                        .getApplicationContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                // 显示或者隐藏输入法
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        /**
         * method desc：隐藏键盘
         *
         * @param activity
         */
        public static void hideKeyboard(Activity activity) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!Utility.isNull(activity.getCurrentFocus())) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }

        /**
         * method desc：在对应的view上显示虚拟键盘
         *
         * @param view
         */
        public static void showKeyboard(final View view) {

            if (view instanceof EditText) {
                view.requestFocus();
            }

            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                public void run() {
                    InputMethodManager inputManager = (InputMethodManager) view
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(view, 0);
                }
            }, 150);

        }

        public static void backgroundAlternate(View view, int position, int... resIds) {
            int index = position % resIds.length;
            view.setBackgroundResource(resIds[index]);
        }

        /**
         * method desc：屏幕截图，只能截当前应用
         *
         * @param activity
         * @param v        为空则获取activity根目录
         * @return
         */
        public static Bitmap takeScreenShot(Activity activity, View v) {
            View view = v;
            if (Utility.isNull(v)) {
                view = activity.getWindow().getDecorView();
            }

            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap b1 = view.getDrawingCache();

            Bitmap b = Bitmap.createBitmap(b1, 0, 0,
                    view.getWidth(), view.getHeight());
            view.destroyDrawingCache();
            return b;
        }

        /**
         * Get the screen height.
         *
         * @param context
         * @return the screen height
         */
        @SuppressLint("NewApi")
        public static int getScreenHeight(Context context) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return dm.heightPixels;
        }

        /**
         * Get the screen width.
         *
         * @param context
         * @return the screen width
         */
        @SuppressLint("NewApi")
        public static int getScreenWidth(Context context) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return dm.widthPixels;
        }

        public static int getBarHeight(Context context) {
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            int x = 0, sbar = 38;//默认为38，貌似大部分是这样的
            try {
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                x = Integer.parseInt(field.get(obj).toString());
                sbar = context.getResources().getDimensionPixelSize(x);

            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return sbar;
        }

        /**
         * method desc：判断当前系统语言
         *
         * @param context
         * @return
         */
        public static boolean isZh(Context context) {
            Locale locale = context.getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            if (language.endsWith("zh"))
                return true;
            else
                return false;
        }

        /**
         * method desc：获取TextView 内容
         *
         * @param textView
         * @return
         */
        public static String getText(TextView textView) {
            return textView.getText().toString();
        }

        /**
         * method desc：获取EditText内容
         *
         * @param editText
         * @return
         */
        public static String getText(EditText editText) {
            return editText.getText().toString().trim();
        }

        /**
         * 将光标移到最后一个
         *
         * @param editText
         */
        public static void setEditTextSection2End(EditText editText) {
            editText.setSelection(editText.getText().length());
        }

        /**
         * 将光标移到指定位置
         *
         * @param editText
         */
        public static void setEditTextSection2End(EditText editText, int index) {
            editText.setSelection(index);
        }

        /**
         * method desc： 判断是否有sdcard
         *
         * @return
         */
        public static boolean isExistSDCard() {
            if (Environment.getExternalStorageDirectory().exists()) {
                return true;
            }
            return false;
        }


        /**
         * 将多个对象拼接成字符串
         *
         * @param object
         * @return
         */
        public static String formatString(Object... object) {
            StringBuilder builder = new StringBuilder();
            for (Object o : object) {
                if (o != null) {
                    builder.append(o);
                }
            }
            return builder.toString();
        }


        /**
         * 获取view的Visibility
         *
         * @param view
         * @return
         */
        public static boolean isVisible(View view) {
            return view.getVisibility() == View.VISIBLE;
        }

        private synchronized static Ringtone getRingtone(Context context) {
            if (mRing == null) {
                // http://stackoverflow.com/questions/15578812/troubles-play-sound-in-silent-mode-on-android
                Uri uriRing = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                if (uriRing == null)
                    uriRing = RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                mRing = RingtoneManager.getRingtone(context,
                        uriRing);
                if (mRing == null
                        && !uriRing.toString().equals(
                        RingtoneManager.getDefaultUri(
                                RingtoneManager.TYPE_RINGTONE).toString())) {
                    mRing = RingtoneManager.getRingtone(context, RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                }
                if (mRing != null) {
                    mRing.setStreamType(AudioManager.STREAM_ALARM);
                }

            }
            return mRing;
        }

        public static void playNotifRing(Context context) {
            try {
                if (getRingtone(context) != null) {
                    mRing.play();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * @param text 要计算的字符串
         * @param size 字提大小
         * @return
         */
        public static float getTextWidth(String text, float size) {
            TextPaint FontPaint = new TextPaint();
            FontPaint.setTextSize(size);
            return FontPaint.measureText(text);
        }

        /**
         * 设置text中划线
         *
         * @param textViews
         */
        public static void setTextViewStrikeThruTextFlag(TextView... textViews) {
            for (TextView textView : textViews) {
                textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线
            }

        }
    }

    /**
     * 动画辅助类
     */
    public static class AnimationUtility {
        public static void show(final View view, int anim) {
            Animation animation = view.getAnimation();
            if (!Utility.isNull(animation) || UIUtility.isVisible(view))
                return;
            animation = AnimationUtils.loadAnimation(view.getContext(), anim);
            view.startAnimation(animation);
            animation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.setVisibility(View.VISIBLE);
            view.setAlpha(1);
        }

        public static void hide(final View view, int anim) {
            Animation animation = view.getAnimation();
            if (!Utility.isNull(animation) || !UIUtility.isVisible(view))
                return;
            animation = AnimationUtils.loadAnimation(view.getContext(), anim);
            view.startAnimation(animation);
            animation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);
                    view.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        public static void playAnimation(View view) {
            try {
                AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
                animationDrawable.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public static void stopAnimation(View view) {

            try {
                AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
                animationDrawable.stop();
                animationDrawable.selectDrawable(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void play(View view, int anim) {
            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), anim));
        }

        public synchronized static void hideAnimationByResize(final View v) {
            Animation animation = v.getAnimation();
            if (!Utility.isNull(animation))
                return;
            if (UIUtility.isVisible(v)) {
                final int initialHeight = v.getHeight();
                Animation anim = new Animation() {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {

                        if (interpolatedTime == 1) {
                            v.setVisibility(View.GONE);
                            v.clearAnimation();
                        } else {
                            v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                            v.requestLayout();
                        }

                    }

                    @Override
                    public boolean willChangeBounds() {
                        return true;
                    }
                };
                anim.setDuration(300);
                v.startAnimation(anim);
            }
        }

        public synchronized static void showAnimationByResize(final View v, final int height) {
            Animation animation = v.getAnimation();
            if (!Utility.isNull(animation))
                return;
            if (!UIUtility.isVisible(v)) {
                v.setVisibility(View.VISIBLE);
                Animation anim = new Animation() {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {

                        if (interpolatedTime == 1) {
                            v.getLayoutParams().height = height;
                            v.requestLayout();
                            v.clearAnimation();
                        } else {
                            v.getLayoutParams().height = (int) (height * interpolatedTime);
                            v.requestLayout();
                        }
                    }

                    @Override
                    public boolean willChangeBounds() {
                        return true;
                    }
                };

                anim.setDuration(300);
                v.startAnimation(anim);
            }
        }

        public static void slideView(final View view, final float fromX, final float toX, long duration) {
            Animation animation = view.getAnimation();
            if (!Utility.isNull(animation))
                return;
            animation = new TranslateAnimation(fromX, toX, 0, 0);
            animation.setInterpolator(new OvershootInterpolator());
            animation.setDuration(duration);
            animation.setStartOffset(0);
            animation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    int left = view.getLeft() + (int) (toX - fromX);
                    int top = view.getTop();
                    int width = view.getWidth();
                    int height = view.getHeight();
                    view.clearAnimation();
                    view.layout(left, top, left + width, top + height);
                }
            });
            view.startAnimation(animation);
        }
    }

    /**
     * sharedPreferences工具类
     */
    public static class SharedPreferencesUtility {

        private static SharedPreferences mSharedPreferences = null;

        /**
         * 如果没有传入prefName获取默认的
         *
         * @param context
         * @return
         */
        public static SharedPreferences getSharedPreferences(Context context) {
            if (Utility.isNull(mSharedPreferences)) {
                mSharedPreferences = context.getSharedPreferences("MouseUtilsPrefName", 0);
            }
            return mSharedPreferences;
        }

        public static SharedPreferences getSharedPreferences(Context context, String prefName) {
            return context.getSharedPreferences(prefName, 0);
        }

        //**************************************************************************
        private static SharedPreferences getPreference(Context ctx) {
            if (mSharedPreferences == null) {
                mSharedPreferences = ctx.getApplicationContext()
                        .getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
            }
            return mSharedPreferences;
        }

        private static SharedPreferences.Editor getEditor(Context ctx) {
            return getPreference(ctx).edit();
        }

        public static void writeObject(Context ctx, String key, Object obj) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                String objBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
                getEditor(ctx).putString(key, objBase64).commit();
            } catch (Exception e) {
                Log.e("test", "saveObject error", e);
            }
        }

        public static Object readObject(Context ctx, String key) {
            try {
                String objBase64 = getPreference(ctx).getString(key, null);
                if (TextUtils.isEmpty(objBase64)) {
                    return null;
                }
                byte[] base64 = Base64.decode(objBase64, Base64.DEFAULT);
                ByteArrayInputStream bais = new ByteArrayInputStream(base64);
                ObjectInputStream bis = new ObjectInputStream(bais);
                return bis.readObject();
            } catch (Exception e) {
                Log.e("test", "readObject error", e);
            }
            return null;
        }
        //**************************************************************************

        /**
         * 通过PrefName放置相关的数据
         *
         * @param context
         * @param prefName
         * @param key
         * @param value
         */
        public static void put(Context context, String prefName, String key, Object value) {
            if (value != null) {
                if (value instanceof String) {
                    getSharedPreferences(context, prefName).edit().putString(key, value.toString())
                            .commit();
                } else if (value instanceof Integer) {
                    getSharedPreferences(context, prefName).edit()
                            .putInt(key, Integer.parseInt(value.toString()))
                            .commit();
                } else if (value instanceof Long) {
                    getSharedPreferences(context, prefName).edit()
                            .putLong(key, Long.parseLong(value.toString()))
                            .commit();
                } else if (value instanceof Boolean) {
                    getSharedPreferences(context, prefName).edit().putBoolean(key, Boolean.parseBoolean(value.toString())).commit();
                }
            } else {
                getSharedPreferences(context, prefName).edit().putString(key, null).commit();
            }
        }

        /**
         * @param key
         * @param value
         */
        public static void put(Context context, String key, Object value) {

            if (value != null) {
                if (value instanceof String) {
                    getSharedPreferences(context).edit().putString(key, value.toString())
                            .commit();
                } else if (value instanceof Integer) {
                    getSharedPreferences(context).edit()
                            .putInt(key, Integer.parseInt(value.toString()))
                            .commit();
                } else if (value instanceof Long) {
                    getSharedPreferences(context).edit()
                            .putLong(key, Long.parseLong(value.toString()))
                            .commit();
                } else if (value instanceof Boolean) {
                    getSharedPreferences(context).edit().putBoolean(key, Boolean.parseBoolean(value.toString())).commit();
                } else {
                    getSharedPreferences(context).edit().putString(key, value.toString())
                            .commit();
                }
            } else {
                getSharedPreferences(context).edit().putString(key, null).commit();
            }
        }

        public static String getString(Context context, String prefName, String key, String defaultValue) {
            return getSharedPreferences(context, prefName).getString(key, defaultValue);
        }

        public static int getInt(Context context, String prefName, String key, int defaultValue) {
            return getSharedPreferences(context, prefName).getInt(key, defaultValue);
        }

        public static long getLong(Context context, String prefName, String key, long defaultValue) {
            return getSharedPreferences(context, prefName).getLong(key, defaultValue);
        }

        public static boolean getBoolean(Context context, String prefName, String key, boolean defaultValue) {
            return getSharedPreferences(context, prefName).getBoolean(key, defaultValue);
        }

        public static boolean contains(Context context, String prefName, String key) {
            return getSharedPreferences(context, prefName).contains(key);
        }

        public static void remove(Context context, String prefName, String key) {
            getSharedPreferences(context, prefName).edit().remove(key).commit();
        }

        public static String getString(Context context, String key, String defaultValue) {
            return getSharedPreferences(context).getString(key, defaultValue);
        }

        public static int getInt(Context context, String key, int defaultValue) {
            return getSharedPreferences(context).getInt(key, defaultValue);
        }

        public static long getLong(Context context, String key, long defaultValue) {
            return getSharedPreferences(context).getLong(key, defaultValue);
        }

        public static boolean getBoolean(Context context, String key, boolean defaultValue) {

            return getSharedPreferences(context).getBoolean(key, defaultValue);
        }

        public static boolean contains(Context context, String key) {
            return getSharedPreferences(context).contains(key);
        }

        public static void remove(Context context, String key) {
            getSharedPreferences(context).edit().remove(key).commit();
        }

        public static void clear(Context context) {
            getSharedPreferences(context).edit().clear().commit();
        }

        public static void clear(Context context, String prefName) {
            getSharedPreferences(context, prefName).edit().clear().commit();
        }

        public SharedPreferences getSharedPreference() {
            return mSharedPreferences;
        }

    }

    /**
     * 缓存工具类
     */
    public static class CacheUtil {
        /**
         * 获取缓存大小
         *
         * @param context
         * @return
         * @throws Exception
         */
        public static String getTotalCacheSize(Context context) throws Exception {
            long cacheSize = getFolderSize(context.getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheSize += getFolderSize(context.getExternalCacheDir());
            }
            return getFormatSize(cacheSize);
        }

        /***
         * 清理所有缓存
         * @param context
         */
        public static void clearAllCache(Context context) {
            deleteDir(context.getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                deleteDir(context.getExternalCacheDir());
            }
        }

        private static boolean deleteDir(File dir) {
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
            return dir.delete();
        }

        // 获取文件
        //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
        //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
        public static long getFolderSize(File file) throws Exception {
            long size = 0;
            try {
                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    // 如果下面还有文件
                    if (fileList[i].isDirectory()) {
                        size = size + getFolderSize(fileList[i]);
                    } else {
                        size = size + fileList[i].length();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return size;
        }

        /**
         * 格式化单位
         *
         * @param size
         * @return
         */
        public static String getFormatSize(double size) {
            double kiloByte = size / 1024;
            if (kiloByte < 1) {
//            return size + "Byte";
                return "0MB";
            }

            double megaByte = kiloByte / 1024;
            if (megaByte < 1) {
                BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
                return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                        .toPlainString() + "MB";
            }

            double gigaByte = megaByte / 1024;
            if (gigaByte < 1) {
                BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
                return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                        .toPlainString() + "MB";
            }

            double teraBytes = gigaByte / 1024;
            if (teraBytes < 1) {
                BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
                return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                        .toPlainString() + "GB";
            }
            BigDecimal result4 = new BigDecimal(teraBytes);
            return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                    + "TB";
        }

    }

    /**
     * 身份证验证
     */
    public static final class IDCARD {
        static int[] WI = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

        static String[] VALIDATENUM = {"1", "0", "X", "9", "8", "7", "6", "5",
                "4", "3", "2"};

        public static boolean cardValidate(String cardNum) {
            if (!Utility.isNull(cardNum)) {
                if (cardNum.length() != 18) {
                    return false;
                }
                int count = 0;
                for (int i = 0; i < cardNum.length() - 1; i++) {
                    count += Integer.parseInt(cardNum.substring(i, i + 1))
                            * WI[i];
                }
                int mod = count % 11;
                String validateNum = VALIDATENUM[mod];
                if (!validateNum.equals(cardNum.substring(17))) {
                    return false;
                }
            }
            return true;
        }

        public static int validator(String id) {
            String str = "[1-9]{2}[0-9]{4}(19|20)[0-9]{2}"
                    + "((0[1-9]{1})|(1[1-2]{1}))((0[1-9]{1})|([1-2]{1}[0-9]{1}|(3[0-1]{1})))"
                    + "[0-9]{3}[0-9Xx]{1}";
            Pattern pattern = Pattern.compile(str);
            return pattern.matcher(id).matches() ? 0 : 1;
        }
    }

    /**
     * 文件操作类
     */
    public static final class FileUtility {

        public static String sd_card = Environment
                .getExternalStorageDirectory().getAbsolutePath();

        private static String TEMP_IMAGE_DIR_PATH;

        private static String TEMP_VOICE_DIR_PATH;

        private static String TEMP_DOCUMENT_DIR_PATH;

        public static void setTempImageDir(String path) {
            TEMP_IMAGE_DIR_PATH = path;
        }

        public static void setTempVoiceDir(String path) {
            TEMP_VOICE_DIR_PATH = path;
        }

        public static void setTempDocument(String path) {
            TEMP_DOCUMENT_DIR_PATH = path;
        }

        public static String getFilePathByContentResolver(Context context, Uri uri) {
            if (null == uri) {
                return null;
            }
            Cursor c = context.getContentResolver().query(uri, null, null, null,
                    null);
            String filePath = null;
            if (null == c) {
                throw new IllegalArgumentException("Query on " + uri
                        + " returns null result.");
            }
            try {
                if ((c.getCount() != 1) || !c.moveToFirst()) {
                } else {
                    filePath = c.getString(c
                            .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                }
            } finally {
                c.close();
            }
            return filePath;
        }

        /**
         * method desc：获取一个随机的完整的临时图片路径
         *
         * @return
         */
        public static String getUUIDImgPath() {
            createDir(TEMP_IMAGE_DIR_PATH);
            return CommonUtility.UIUtility.formatString(sd_card, TEMP_IMAGE_DIR_PATH, UUID.randomUUID().toString()
                    , ".png");// ".png.cache";
        }

        /**
         * 获取一个随机的完整的语音路径
         *
         * @return
         */
        public static String getUUIDVoicePath() {
            createDir(TEMP_VOICE_DIR_PATH);
            return CommonUtility.UIUtility.formatString(sd_card, TEMP_VOICE_DIR_PATH, UUID.randomUUID().toString());
        }

        public static String getVoiceDirPath() {
            createDir(TEMP_VOICE_DIR_PATH);
            return CommonUtility.UIUtility.formatString(sd_card, TEMP_VOICE_DIR_PATH);
        }

        public static String getVoiceTempDirPath() {
            return TEMP_VOICE_DIR_PATH;
        }

        /**
         * 获取当前时间，并生成文件名字
         *
         * @return
         */
        public static String getDocumentPath(String fileName) {
            createDir(TEMP_DOCUMENT_DIR_PATH);
            return CommonUtility.UIUtility.formatString(sd_card, TEMP_DOCUMENT_DIR_PATH, fileName);
        }

        /**
         * 获取下载的path
         *
         * @param path
         * @param fileName
         * @return
         */
        public static File getDownloadPath(String path, String fileName) {
            createDir(path);
            File file = new File(sd_card + path, fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return file;
        }

        /**
         * 默认会创建一个fileName的文件/data/data/your_packages/files/fileName
         *
         * @param context
         * @param fileName
         * @return
         */
        public static File getDataPath(Context context, String fileName) {
            String filePath = context.getFilesDir().getAbsolutePath();
            DebugLog.e("mark", "filePath:" + filePath);
            File file = new File(filePath, fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            DebugLog.e("mark", "file ----->:" + file.getPath());
            return file;
        }

        public static File getDataFilePath(Context context) {
            String filePath = context.getFilesDir().getAbsolutePath();
            DebugLog.e("mark", "filePath ----->:" + filePath);
            return new File(filePath);
        }

        /**
         * method desc：创建指定的路径的文件夹
         *
         * @param path
         */
        private static void createDir(String path) {
            if (!Utility.isNull(path)) {
                File file = new File(CommonUtility.UIUtility.formatString(sd_card, path));
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
        }

        /**
         * 获取地址的文件名
         *
         * @param path
         * @return
         */
        public static String getFileNameString(String path) {
            String p = path.substring(path.lastIndexOf("/") + 1).toLowerCase(
                    Locale.getDefault());
            StringBuilder sb = new StringBuilder(p);
            sb.append("_").append(".cache");
            return sb.toString();
        }


        /**
         * 解压zip文件到目标目录中
         *
         * @param targetDirectory 目标目录
         * @param zipFile         zip文件
         * @throws IOException
         */
        public static void unzip(File targetDirectory, File zipFile) throws IOException {
            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(new FileInputStream(zipFile)));
            try {
                ZipEntry ze;
                int count;
                byte[] buffer = new byte[8192];
                while ((ze = zis.getNextEntry()) != null) {
                    File file = new File(targetDirectory, ze.getName());
                    File dir = ze.isDirectory() ? file : file.getParentFile();
                    if (!dir.isDirectory() && !dir.mkdirs())
                        throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                    if (ze.isDirectory())
                        continue;
                    FileOutputStream fout = new FileOutputStream(file);
                    try {
                        while ((count = zis.read(buffer)) != -1)
                            fout.write(buffer, 0, count);
                    } finally {
                        fout.close();
                    }
                }
            } finally {
                zis.close();
            }
        }

        public static File unzip(Context context, File targetDirectory, String assetsFileName, String sqlName) {
            File sqlFile = new File(targetDirectory, sqlName);
            if (sqlFile.exists()) {//存在删除掉
                sqlFile.delete();
            }

            try {
                InputStream inputStream = context.getAssets().open(assetsFileName);
                ZipInputStream zis = new ZipInputStream(
                        new BufferedInputStream(inputStream));
                try {
                    ZipEntry ze;
                    int count;
                    byte[] buffer = new byte[8192];
                    while ((ze = zis.getNextEntry()) != null) {
                        File file = new File(targetDirectory, ze.getName());
                        File dir = ze.isDirectory() ? file : file.getParentFile();
                        if (!dir.isDirectory() && !dir.mkdirs())
                            throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                        if (ze.isDirectory())
                            continue;
                        FileOutputStream fout = new FileOutputStream(file);
                        try {
                            while ((count = zis.read(buffer)) != -1)
                                fout.write(buffer, 0, count);
                        } finally {
                            fout.close();
                        }
                    }
                } finally {
                    zis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sqlFile;
        }

        /**
         * 获取目录文件大小
         *
         * @param dir
         * @return
         */
        public static long getDirSize(File dir) {
            if (dir == null) {
                return 0;
            }
            if (!dir.isDirectory()) {
                return 0;
            }
            long dirSize = 0;
            File[] files = dir.listFiles();
            if (!Utility.isNull(files) && files.length > 0) {
                for (File file : files) {
                    if (file.isFile()) {
                        dirSize += file.length();
                    } else if (file.isDirectory()) {
                        dirSize += file.length();
                        dirSize += getDirSize(file); // 递归调用继续统计
                    }
                }
            }
            return dirSize;
        }

        /**
         * 转换文件大小
         *
         * @param fileS
         * @return B/KB/MB/GB
         */
        public static String formatFileSize(long fileS) {
            DecimalFormat df = new DecimalFormat("#.00");
            String fileSizeString = "";
            if (fileS < 1024) {
                fileSizeString = df.format((double) fileS) + "B";
            } else if (fileS < 1048576) {
                fileSizeString = df.format((double) fileS / 1024) + "KB";
            } else if (fileS < 1073741824) {
                fileSizeString = df.format((double) fileS / 1048576) + "MB";
            } else {
                fileSizeString = df.format((double) fileS / 1073741824) + "G";
            }
            return fileSizeString;
        }
    }

    /**
     * 系统操作工具类
     */
    public static final class SystemOperateUtility {

        /**
         * method desc: 发送短信
         *
         * @param smsBody
         */
        public static void sendSMS(Context context, String tel, String smsBody) {
            Uri smsToUri = Uri.parse("smsto:" + tel);
            Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
            intent.putExtra("sms_body", smsBody);
            context.startActivity(intent);
        }

        /**
         * 得到剪切板中的内容
         *
         * @param context
         * @return
         */
        public static String getClipboard(Context context) {
            ClipboardManager clipboard = (ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard.hasText()) {
                return clipboard.getText().toString();
            }
            return null;
        }

        /**
         * method desc：将内容复制到剪贴板上
         *
         * @param context
         * @param str
         * @param tip
         */
        @SuppressWarnings("deprecation")
        public static void copy2Clipboard(Context context, String str,
                                          String tip) {
            ClipboardManager clipboard = (ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(str);
            UIUtility.toast(context, Utility.isNull(tip) ? "内容已复制到剪贴板中。" : tip);
        }

        /**
         * method desc：将内容复制到剪贴板上
         *
         * @param context
         * @param str
         */
        public static void copy2Clipboard(Context context, String str) {
            copy2Clipboard(context, str, null);
        }

        public static String getProcessName(Context context, int pid) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            if (runningApps == null) {
                return null;
            }
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
            return null;
        }
    }

    /**
     * bitmap操作类
     */
    public static final class BitmapOperateUtility {
        /**
         * 将bitmap放入缓存中 method desc：
         *
         * @param bitmap
         * @param bitmaps
         */
        public static void addBitmap(Bitmap bitmap, ArrayList<Bitmap> bitmaps) {
            if (!Utility.isNull(bitmaps)) {
                bitmaps.add(bitmap);
            } else {
                bitmaps = new ArrayList<>();
                bitmaps.add(bitmap);
            }
        }

        /**
         * 销毁指定集合的bitmap，释放内存 method desc：
         *
         * @param bitmaps
         */
        public static void destroyBitmaps(ArrayList<Bitmap> bitmaps) {
            if (!Utility.isNull(bitmaps)) {
                for (Bitmap bitmap : bitmaps) {
                    if (!Utility.isNull(bitmap) && !bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                    bitmap = null;
                }
                bitmaps.clear();
                bitmaps = null;
            }
        }

        /**
         * 销毁bitmap，释放内存 method desc：
         *
         * @param bitmap
         */
        public static void destroyBitmap(Bitmap bitmap) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap = null;
        }

        public static Bitmap drawable2Bitmap(Drawable drawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        }

        /**
         * method desc：将图片按照指定角度旋转
         *
         * @param bitmap
         * @param degree
         * @return
         */
        public static Bitmap rotate(Bitmap bitmap, int degree) {
            if (bitmap.getHeight() < bitmap.getWidth()) {
                Matrix matrix = new Matrix();
                matrix.postRotate(degree);

                Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);
                destroyBitmap(bitmap);
                return b;
            } else {
                return bitmap;
            }
        }

        /**
         * 将彩色图转换为黑白图
         *
         * @param bmp
         * @return 返回转换好的位图
         */
        public static Bitmap convertToBlackWhite(Bitmap bmp) {
            int width = bmp.getWidth(); // 获取位图的宽
            int height = bmp.getHeight(); // 获取位图的高

            int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

            bmp.getPixels(pixels, 0, width, 0, 0, width, height);
            int alpha = 0xFF << 24;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int grey = pixels[width * i + j];

                    int red = ((grey & 0x00FF0000) >> 16);
                    int green = ((grey & 0x0000FF00) >> 8);
                    int blue = (grey & 0x000000FF);

                    //当原像素为透明时不处理
                    if (red != 0 || green != 0 || blue != 0) {
                        grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                        grey = alpha | (grey << 16) | (grey << 8) | grey;
                        pixels[width * i + j] = grey;
                    }
                }
            }
            Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
            return newBmp;
        }

        /**
         * 转换图片成圆形
         *
         * @param bitmap 传入Bitmap对象
         * @return
         */
        public static Bitmap toRoundBitmap(Bitmap bitmap) {
            if (!Utility.isNull(bitmap)) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                float roundPx;
                float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
                if (width <= height) {
                    roundPx = width / 2;

                    left = 0;
                    top = 0;
                    right = width;
                    bottom = width;

                    height = width;

                    dst_left = 0;
                    dst_top = 0;
                    dst_right = width;
                    dst_bottom = width;
                } else {
                    roundPx = height / 2;

                    float clip = (width - height) / 2;

                    left = clip;
                    right = width - clip;
                    top = 0;
                    bottom = height;
                    width = height;

                    dst_left = 0;
                    dst_top = 0;
                    dst_right = height;
                    dst_bottom = height;
                }

                Bitmap output = Bitmap
                        .createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);

                final Paint paint = new Paint();
                final Rect src = new Rect((int) left, (int) top, (int) right,
                        (int) bottom);
                final Rect dst = new Rect((int) dst_left, (int) dst_top,
                        (int) dst_right, (int) dst_bottom);
                final RectF rectF = new RectF(dst);

                paint.setAntiAlias(true);// 设置画笔无锯齿

                canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

                // 以下有两种方法画圆,drawRounRect和drawCircle
                canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
                // canvas.drawCircle(roundPx, roundPx, roundPx, paint);

                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
                canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

                return output;
            }
            return bitmap;
        }
    }

    /**
     * json 取值的简单封装，主要是封装每个key的异常处理
     */
    public static final class JSONObjectUtility {
        public static String optString(JSONObject object, String key) {
            try {
                String text = object.getString(key);
                return Utility.isNull(text) ? null : text;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static <T> ArrayList<T> convertJSONArray2Array(JSONArray jsonArray, Class<T> c) {
            ArrayList<T> objects = new ArrayList<>();
            if (!Utility.isNull(jsonArray)) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    objects.add(convertJSONObject2Obj(jsonArray.getJSONObject(i), c));
                }
            }
            return objects;
        }

        public static <T> ArrayList<T> convertJSONArray2Array(JSONArray jsonArray, Class<T> c, String addKey, String addValueKey) {
            ArrayList<T> objects = new ArrayList<>();
            if (!Utility.isNull(jsonArray)) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    objects.add(convertJSONObject2Obj(jsonArray.getJSONObject(i), c, addKey, addValueKey));
                }
            }
            return objects;
        }

        /**
         * @param jsonArray
         * @param c
         * @param <T>
         * @return
         */
        public static <T> ArrayList<T> convertJSONArray2ArrayReflect(JSONArray jsonArray, Class<T> c) {
            ArrayList<T> objects = new ArrayList<>();
            if (!Utility.isNull(jsonArray)) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    objects.add(convertJSONObject2ObjReflect(jsonArray.getJSONObject(i), c));
                }
            }
            return objects;
        }

        public static <T> T convertJSONObject2Obj(JSONObject jsonObject, Class<T> c) {
            return convertJSONObject2Obj(jsonObject.toString(), c);
        }

        public static <T> T convertJSONObject2Obj(String jsonStr, Class<T> c) {
            if (!Utility.isNull(jsonStr)) {
                try {
                    return JSON.parseObject(jsonStr.toString(), c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                return c.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static <T> T convertJSONObject2Obj(JSONObject jsonObject, Class<T> c, String addKey, String addValueKey) {
            if (!Utility.isNull(addKey) && !Utility.isNull(addValueKey)) {
                try {
                    jsonObject.put(addKey, jsonObject.getString(addValueKey));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return JSON.parseObject(jsonObject.toString(), c);
        }

        public static <T> T convertJSONObject2ObjReflect(JSONObject jsonObject, Class<T> c) {

            T o = null;
            try {
                o = c.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!Utility.isNull(jsonObject)) {
                Field[] fields = c.getDeclaredFields();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    if (!fieldName.equals("serialVersionUID") && !fieldName.equals("_id")) {
                        field.setAccessible(true);
                        String fieldValue = jsonObject.getString(fieldName);
                        try {
                            field.set(o, fieldValue);
                        } catch (Exception e) {
                            DebugLog.e(fieldName + "==========error");
                        }
                    }
                }
            }
            return o;
        }

        public static String map2JSONObject(HashMap<String, Object> params) {
            StringBuilder builder = new StringBuilder();
            for (Iterator it = params.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                if (!Utility.isNull(entry.getValue())) {
                    builder.append(entry.getKey()).append(entry.getValue());
                }
            }
            return builder.toString();
        }

        /**
         * 判断是否是json  true是的   false 不是
         */
        public static boolean isJSON2(String str) {
            boolean result = false;
            try {
                Object obj = JSON.parse(str);
                result = true;
            } catch (Exception e) {
                result = false;
            }
            return result;
        }
    }

    /**
     * 断密码输入是否符合格式
     *
     * @param
     * @param
     * @return
     * @note 判断密码输入是否符合格式
     */
    public static boolean regexPassword(String pwd) {
        boolean flag = true;

        String regExp = "^[A-Za-z0-9]+$";

        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(pwd.subSequence(0, 1));

        flag = matcher.matches();
        if (flag) {
            String[] pwds = pwd.split("");
            int length = pwds.length;
            for (int i = 0; i < length; i++) {
                if (pwds[i].equals(" ")) {
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }

    /**
     * 时间处理
     */
    public static final class DateUtils {
        /**
         * 一些时间格式
         */
        private final static String FORMAT_TIME = "HH:mm";
        private final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
        private final static String FORMAT_DATE_TIME_SECOND = "yyyy-MM-dd HH:mm:ss";
        private final static String FORMAT_MONTH_DAY_TIME = "MM-dd HH:mm";
        private final static String FORMAT_DATE = "yyyy-MM-dd";

        /**
         * 描述：计算两个日期所差的天数.
         *
         * @param date1 第一个时间的毫秒表示
         * @param date2 第二个时间的毫秒表示
         * @return int 所差的天数
         */
        public static int getOffsetDay(long date1, long date2) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(date1);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(date2);
            //先判断是否同年
            int y1 = calendar1.get(Calendar.YEAR);
            int y2 = calendar2.get(Calendar.YEAR);
            int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
            int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
            int maxDays = 0;
            int day = 0;
            if (y1 - y2 > 0) {
                maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
                day = d1 - d2 + maxDays;
            } else if (y1 - y2 < 0) {
                maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
                day = d1 - d2 - maxDays;
            } else {
                day = d1 - d2;
            }
            return day;
        }

        /**
         * 时间转换时间戳
         *
         * @param dateString 转换时间的字符串
         * @param pattern    转换类型
         * @return
         */
        public static long getStringToDate(String dateString, String pattern) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            Date date = new Date();
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return date.getTime();
        }


        /**
         * 判断2个时间大小
         * yyyy-MM-dd HH:mm 格式（自己可以修改成想要的时间格式）
         *
         * @param startTime
         * @param endTime
         * @return 1结束时间小于开始时间   2不分大小  3结束时间大于开始时间
         */
        public static int timeCompare(String startTime, String endTime) {
            int i = 0;
            //注意：传过来的时间格式必须要和这里填入的时间格式相同
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date date1 = dateFormat.parse(startTime);//开始时间
                Date date2 = dateFormat.parse(endTime);//结束时间
                // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
                if (date2.getTime() < date1.getTime()) {
                    //结束时间小于开始时间
                    i = 1;
                } else if (date2.getTime() == date1.getTime()) {
                    //开始时间与结束时间相同
                    i = 2;
                } else if (date2.getTime() > date1.getTime()) {
                    //结束时间大于开始时间
                    i = 3;
                }
            } catch (Exception e) {

            }
            return i;
        }
    }

    /**
     * 进制转换帮助类
     */
    public static final class HexUtils {

        /**
         * 字符串转换成十六进制字符串
         *
         * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
         */
        public static String str2HexStr(String str) {

            char[] chars = "0123456789ABCDEF".toCharArray();
            StringBuilder sb = new StringBuilder("");
            byte[] bs = str.getBytes();
            int bit;

            for (int i = 0; i < bs.length; i++) {
                bit = (bs[i] & 0x0f0) >> 4;
                sb.append(chars[bit]);
                bit = bs[i] & 0x0f;
                sb.append(chars[bit]);
                sb.append(' ');
            }
            return sb.toString().trim();
        }
    }

    /**
     * 二维码工具
     */
    public static class QrCodeUtils {
        /**
         * 向二维码中间添加logo图片(图片合成)
         *
         * @param srcBitmap   原图片（生成的简单二维码图片）
         * @param logoBitmap  logo图片
         * @param logoPercent 百分比 (用于调整logo图片在原图片中的显示大小, 取值范围[0,1] )
         * @return
         */
        public static Bitmap addLogo(Bitmap srcBitmap, Bitmap logoBitmap, float logoPercent) {
            if (srcBitmap == null) {
                return null;
            }
            if (logoBitmap == null) {
                return srcBitmap;
            }
            //传值不合法时使用0.2F
            if (logoPercent < 0F || logoPercent > 1F) {
                logoPercent = 0.2F;
            }

            /** 1. 获取原图片和Logo图片各自的宽、高值 */
            int srcWidth = srcBitmap.getWidth();
            int srcHeight = srcBitmap.getHeight();
            int logoWidth = logoBitmap.getWidth();
            int logoHeight = logoBitmap.getHeight();

            /** 2. 计算画布缩放的宽高比 */
            float scaleWidth = srcWidth * logoPercent / logoWidth;
            float scaleHeight = srcHeight * logoPercent / logoHeight;

            /** 3. 使用Canvas绘制,合成图片 */
            Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(srcBitmap, 0, 0, null);
            canvas.scale(scaleWidth, scaleHeight, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logoBitmap, srcWidth / 2 - logoWidth / 2, srcHeight / 2 - logoHeight / 2, null);

            return bitmap;
        }

        /**
         * 生成简单二维码
         *
         * @param content                字符串内容
         * @param width                  二维码宽度
         * @param height                 二维码高度
         * @param character_set          编码方式（一般使用UTF-8）
         * @param error_correction_level 容错率 L：7% M：15% Q：25% H：35%
         * @param margin                 空白边距（二维码与边框的空白区域）
         * @param color_black            黑色色块
         * @param color_white            白色色块
         * @return BitMap
         */
        public static Bitmap createQRCodeBitmap(String content, int width, int height,
                                                String character_set, String error_correction_level,
                                                String margin, int color_black, int color_white) {
            // 字符串内容判空
            if (TextUtils.isEmpty(content)) {
                return null;
            }
            // 宽和高>=0
            if (width < 0 || height < 0) {
                return null;
            }
            try {
                /** 1.设置二维码相关配置 */
                Hashtable<EncodeHintType, String> hints = new Hashtable<>();
                // 字符转码格式设置
                if (!TextUtils.isEmpty(character_set)) {
                    hints.put(EncodeHintType.CHARACTER_SET, character_set);
                }
                // 容错率设置
                if (!TextUtils.isEmpty(error_correction_level)) {
                    hints.put(EncodeHintType.ERROR_CORRECTION, error_correction_level);
                }
                // 空白边距设置
                if (!TextUtils.isEmpty(margin)) {
                    hints.put(EncodeHintType.MARGIN, margin);
                }
                /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象 */
                BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

                /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
                int[] pixels = new int[width * height];
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                        if (bitMatrix.get(x, y)) {
                            pixels[y * width + x] = color_black;//黑色色块像素设置
                        } else {
                            pixels[y * width + x] = color_white;// 白色色块像素设置
                        }
                    }
                }
                /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象 */
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
                return bitmap;
            } catch (WriterException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 照片帮助类
     */
    public static class PhotoUtils {
        /**
         * 扫描的三种方式
         */
        public static enum ScannerType {
            RECEIVER, MEDIA
        }

        /**
         * 首先保存图片
         */
        public static boolean saveImageToGallery(Context context, Bitmap bitmap, ScannerType type) {
            File appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/yietongdoctor/");
            if (!appDir.exists()) {
                // 目录不存在 则创建
                appDir.mkdirs();
            }
            String fileName = "a" + System.currentTimeMillis() + ".jpg";
            File file = new File(appDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); // 保存bitmap至本地
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (type == ScannerType.RECEIVER) {
                    ScannerByReceiver(context, file.getAbsolutePath());
                } else if (type == ScannerType.MEDIA) {
                    ScannerByMedia(context, file.getAbsolutePath());
                }
                if (!bitmap.isRecycled()) {
                    // bitmap.recycle(); 当存储大图片时，为避免出现OOM ，及时回收Bitmap
                    System.gc(); // 通知系统回收
                }
            }
            return true;
        }

        /**
         * Receiver扫描更新图库图片
         **/

        private static void ScannerByReceiver(Context context, String path) {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse("file://" + path)));
            Log.v("TAG", "receiver scanner completed");
        }

        /**
         * MediaScanner 扫描更新图库图片
         **/

        private static void ScannerByMedia(Context context, String path) {
            MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
            Log.v("TAG", "media scanner completed");
        }
    }

    /**
     * 密码工具类
     */
    public static class PwdCheckUtil {

        /**
         * 规则1：至少包含大小写字母及数字中的一种
         * 是否包含
         *
         * @param str
         * @return
         */
        public static boolean isLetterOrDigit(String str) {
            boolean isLetterOrDigit = false;//定义一个boolean值，用来表示是否包含字母或数字
            for (int i = 0; i < str.length(); i++) {
                if (Character.isLetterOrDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                    isLetterOrDigit = true;
                }
            }
            String regex = "^[a-zA-Z0-9]+$";
            boolean isRight = isLetterOrDigit && str.matches(regex);
            return isRight;
        }

        /**
         * 规则2：至少包含大小写字母及数字中的两种
         * 是否包含
         *
         * @param str
         * @return
         */
        public static boolean isLetterDigit(String str) {
            boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
            boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
            for (int i = 0; i < str.length(); i++) {
                if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                    isDigit = true;
                } else if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                    isLetter = true;
                }
            }
            String regex = "^[a-zA-Z0-9]+$";
            boolean isRight = isDigit && isLetter && str.matches(regex);
            return isRight;
        }

        /**
         * 规则3：必须同时包含大小写字母及数字
         * 是否包含
         *
         * @param str
         * @return
         */
        public static boolean isContainAll(String str) {
            boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
            boolean isLowerCase = false;//定义一个boolean值，用来表示是否包含字母
            boolean isUpperCase = false;
            for (int i = 0; i < str.length(); i++) {
                if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                    isDigit = true;
                } else if (Character.isLowerCase(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                    isLowerCase = true;
                } else if (Character.isUpperCase(str.charAt(i))) {
                    isUpperCase = true;
                }
            }
            String regex = "^[a-zA-Z0-9]+$";
            boolean isRight = isDigit && isLowerCase && isUpperCase && str.matches(regex);
            return isRight;
        }

        /**
         * 判断EditText输入的数字、中文还是字母方法
         */
        public static void whatIsInput(Context context, EditText edInput) {
            String txt = edInput.getText().toString();

            Pattern p = Pattern.compile("[0-9]*");
            Matcher m = p.matcher(txt);
            if (m.matches()) {
                Toast.makeText(context, "输入的是数字", Toast.LENGTH_SHORT).show();
            }
            p = Pattern.compile("[a-zA-Z]");
            m = p.matcher(txt);
            if (m.matches()) {
                Toast.makeText(context, "输入的是字母", Toast.LENGTH_SHORT).show();
            }
            p = Pattern.compile("[\u4e00-\u9fa5]");
            m = p.matcher(txt);
            if (m.matches()) {
                Toast.makeText(context, "输入的是汉字", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 系统工具类
     * Created by zhuwentao on 2016-07-18.
     */
    public static class PhoneSystemUtil {
        /**
         * 获取系统版本
         *
         * @return
         */
        public static String getOSVersion() {
            return Build.VERSION.RELEASE;
        }

        /**
         * 获取当前手机系统语言。
         *
         * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
         */
        public static String getSystemLanguage() {
            return Locale.getDefault().getLanguage();
        }

        /**
         * 获取当前系统上的语言列表(Locale列表)
         *
         * @return 语言列表
         */
        public static Locale[] getSystemLanguageList() {
            return Locale.getAvailableLocales();
        }

        /**
         * 获取当前手机系统版本号
         *
         * @return 系统版本号
         */
        public static String getSystemVersion() {
            return Build.VERSION.RELEASE;
        }

        /**
         * 获取手机型号
         *
         * @return 手机型号
         */
        public static String getSystemModel() {
            return Build.MODEL;
        }

        /**
         * 获取手机厂商
         *
         * @return 手机厂商
         */
        public static String getDeviceBrand() {
            return Build.BRAND;
        }

        /**
         * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
         *
         * @return 手机IMEI
         */
        public static String getIMEI(Context ctx) {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
            if (tm != null) {
                return tm.getDeviceId();
            }
            return null;
        }
    }

}
