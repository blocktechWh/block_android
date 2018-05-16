package com.blocktechwh.app.block.Utils;


import com.blocktechwh.app.block.Common.App;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by Arix on 2017-12-11.
 */

public class ConfigPropertiesUtil {

    private static Properties properties;

    public ConfigPropertiesUtil() {
        properties = new Properties();
        InputStream in = null;
        try {
            in = App.getContext().getAssets().open("config");
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static String getProperty(String key) {
        // 实现转码
        try {
            return new String(properties.getProperty(key).getBytes("ISO-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }    }
}
