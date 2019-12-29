package com.jwong.education.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class PrefUtils {

    private static final String FILE_NAME = "share_data";

    private final static String PREF_KEY_NOTIFICATION_TITLE = "pref_key_notification_title";

    private final static String PREF_KEY_SEAL_NAME_CN = "pref_key_seal_name_cn";

    private final static String PREF_KEY_SEAL_NAME_EN = "pref_key_seal_name_en";


    public static void setPrefKeyNotificationTitle(Context context, String title) {
        put(context, PREF_KEY_NOTIFICATION_TITLE, title);
    }

    public static String getPrefKeyNotificationTitle(Context context, String defaultValue) {
        Object object = get(context, PREF_KEY_NOTIFICATION_TITLE, defaultValue);
        return object == null ? defaultValue : object.toString();
    }


    public static void setSealNameCn(Context context, String sealNameCn) {
        put(context, PREF_KEY_SEAL_NAME_CN, sealNameCn);
    }

    public static String getSealNameCn(Context context, String defaultValue) {
        Object object = get(context, PREF_KEY_SEAL_NAME_CN, defaultValue);
        return object == null ? defaultValue : object.toString();
    }


    public static void setSealNameEn(Context context, String sealNameCn) {
        put(context, PREF_KEY_SEAL_NAME_EN, sealNameCn);
    }

    public static String getSealNameEn(Context context, String defaultValue) {
        Object object = get(context, PREF_KEY_SEAL_NAME_EN, defaultValue);
        return object == null ? defaultValue : object.toString();
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    public static void put(Context context, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                Log.e("SharedPreferencesCompat", "findApplyMethod ex:", e);
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
                Log.e("SharedPreferencesCompat", "apply >>>", e);
            } catch (IllegalAccessException e) {
                Log.e("SharedPreferencesCompat", "apply >>>", e);
            } catch (InvocationTargetException e) {
                Log.e("SharedPreferencesCompat", "apply >>>", e);
            }
            editor.commit();
        }
    }
}
