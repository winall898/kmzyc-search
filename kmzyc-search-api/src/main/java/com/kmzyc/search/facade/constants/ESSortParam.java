package com.kmzyc.search.facade.constants;

import com.alibaba.fastjson.JSONObject;
import com.kmzyc.search.param.DocFieldName;

public class ESSortParam {

    // 默认
    private static final int A = 0;

    // 价格
    private static final int B = 1; // ASC
    private static final int C = 2; // DESC
    // 时间
    private static final int D = 3; // ASC
    private static final int E = 4; // DESC
    // 销量
    private static final int F = 5; // ASC
    private static final int G = 6; // DESC
    // 促销
    private static final int H = 7; // ASC
    public static final int I = 8; // DESC

    public static JSONObject getSortText(int type) {
        JSONObject json = new JSONObject();
        switch (type) {
            case A:
                return json;
            case B:
                json.put(DocFieldName.PRICE, ORDER.asc);
                break;
            case C:
                json.put(DocFieldName.PRICE, ORDER.desc);
                break;
            case D:
                json.put(DocFieldName.UP_TIME, ORDER.asc);
                break;
            case E:
                json.put(DocFieldName.UP_TIME, ORDER.desc);
                break;
            case F:
                json.put(DocFieldName.SALES, ORDER.asc);
                break;
            case G:
                json.put(DocFieldName.SALES, ORDER.desc);
                break;
            case H:
                json.put(DocFieldName.PROMOTION, ORDER.asc);
                break;
            case I:
                json.put(DocFieldName.PROMOTION, ORDER.desc);
                break;
        }
        return json;
    }
}
