
package com.yinxm.net.bean;


import com.alibaba.fastjson.annotation.JSONField;

public class NetworkModel<T> {

    // 消息码
    @JSONField(name = "errno")
    private int code;
    // 消息内容
    @JSONField(name = "errmsg")
    private String msg;
    // 实体数据
    @JSONField(name = "data")
    private T result;

    @JSONField(name = "log_id")
    private String logId;
    @JSONField(name = "timestamp")
    private int timestamp;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "\"NetworkModel\": {"
                + "\"code\": \"" + code
                + ", \"msg\": \"" + msg + '\"'
                + ", \"logId\": \"" + logId + '\"'
                + ", \"timestamp\": \"" + timestamp
                + '}';
    }
}
