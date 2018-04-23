package com.kmzyc.search.facade.vo;

/**
 * 提供给前端ajax接口的返回结果 User: lishiming Date: 13-10-14 Time: 下午3:45
 * 
 */
public class ReturnResult<T> {

    /**
     * 返回的结果代码： 200为成功，0为失败 <strong>这里可以根据各接口需要扩展定义其他返回码</strong>
     */
    private String code;

    /**
     * 返回的字符串消息，辅助提示返回的结果代码
     */
    private String message;

    /**
     * 接口返回的结果
     */
    private T returnObject;

    /**
     * 无参构造
     */
    public ReturnResult() {

    }

    public ReturnResult(String code, String message, T t) {
        this.code = code;
        this.message = message;
        this.returnObject = t;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(T returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public String toString() {
        return "ReturnResult{" + "code='" + code + '\'' + ", message='" + message + '\''
                + ", returnObject=" + returnObject + '}';
    }

}
