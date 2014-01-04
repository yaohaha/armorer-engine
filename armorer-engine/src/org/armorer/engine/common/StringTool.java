package org.armorer.engine.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class StringTool {

    /**
     * 格式化字符串，将字符串中的token(#xx#)替换成need(？)
     * 
     * @param str
     * @param rep #
     * @param need ?
     * @return 格式化后的新字符串
     */
    public static String strTokenizer(String str, String rep, String need) {
        String res = null;
        Map<String, List<String>> map = strTokenizerForMore(str, rep, need);
        for(String item:map.keySet()){
            res = item;
            break;
        }
        return res;

    }

    /**
     * 格式化字符串，将字符串中的token(#xx#)替换成need(？)
     * 
     * @param str
     * @param rep #
     * @param need ?
     * @return map<格式化后的新字符串，被替换下来字符串(xx)的列表>
     */
    public static Map<String, List<String>> strTokenizerForMore(String str, String rep, String need) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        List<String> mappingList = new ArrayList<String>();
        StringTokenizer parser = new StringTokenizer(str, rep, true);
        StringBuffer newSqlBuffer = new StringBuffer();
        String token = null;
        String lastToken = null;
        while (parser.hasMoreTokens()) {
            token = parser.nextToken();
            if (rep.equals(lastToken)) {
                if (rep.equals(token)) {
                    newSqlBuffer.append(rep);
                    token = null;
                } else {
                    mappingList.add(token);
                    newSqlBuffer.append(need);
                    token = parser.nextToken();
                    token = null;
                }
            } else {
                if (!rep.equals(token)) {
                    newSqlBuffer.append(token);
                }
            }
            lastToken = token;
        }
        str = newSqlBuffer.toString();
        map.put(str, mappingList);
        return map;
    }

}
