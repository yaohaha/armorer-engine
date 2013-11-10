package org.armorer.engine.util;

import org.apache.log4j.Logger;

public class CommonUtil {

    public static void showInfo(String className, String info) {
        Logger logger = Logger.getLogger(className);
        logger.info(info);
        System.out.println(info);
    }
}
