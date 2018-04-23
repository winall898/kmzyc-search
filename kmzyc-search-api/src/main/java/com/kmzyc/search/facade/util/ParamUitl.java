package com.kmzyc.search.facade.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.kmzyc.search.param.Action;
import com.kmzyc.search.param.Params;



public class ParamUitl {

    public static Map<Params, Map<Action, String[]>> copyParam(
            final Map<Params, Map<Action, String[]>> params) {
        Map<Params, Map<Action, String[]>> target =
                new HashMap<Params, Map<Action, String[]>>(params.size());
        Iterator<Entry<Params, Map<Action, String[]>>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Params, Map<Action, String[]>> entry = it.next();
            Params key = entry.getKey();
            Map<Action, String[]> entryValue = entry.getValue();

            Action targetKey = Action.add;
            String[] temp = entryValue.get(Action.add);
            if (null == temp) {
                temp = entryValue.get(Action.set);
                targetKey = Action.set;
            }
            String[] targetArr = ArrayUtils.clone(temp);

            Map<Action, String[]> targetValue = new HashMap<Action, String[]>();
            targetValue.put(targetKey, targetArr);
            target.put(key, targetValue);
        }
        return target;
    }

    /**
     * 字符串转义
     * 
     * @author zhoulinhong
     * @param s
     * @return
     * @since 20160621
     */
    public static String escapeQueryChars(String s) {
        if (StringUtils.isBlank(s)) {

            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // These characters are part of the query syntax and must be escaped
            if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':'
                    || c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}'
                    || c == '~' || c == '*' || c == '?' || c == '|' || c == '&' || c == ';'
                    || c == '/' || Character.isWhitespace(c)) {
                sb.append(' ');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
