package com.blocktechwh.app.block.Utils;


import com.blocktechwh.app.block.Common.App;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Arix on 2017-12-11.
 */

public class PropertiesUtil {

    private static Properties properties;

    public PropertiesUtil() {
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
        return properties.getProperty(key);
    }
}
