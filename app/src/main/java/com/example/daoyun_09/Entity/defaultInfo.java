package com.example.daoyun_09.Entity;

public class defaultInfo {

    private int request_id;
    private String result_code;
    private String result_desc;

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public void setResult_desc(String result_desc) {
        this.result_desc = result_desc;
    }

    public String getResult_code() {
        return result_code;
    }

    public String getResult_desc() {
        return result_desc;
    }

    @Override
    public String toString() {
        return "defaultInfo{" +
                "request_id=" + request_id +
                ", result_code='" + result_code + '\'' +
                ", result_desc='" + result_desc + '\'' +
                '}';
    }
}
