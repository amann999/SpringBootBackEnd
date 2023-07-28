package com.connex.insurance;

public class Response {
    private String success;
    private double premium;
    private String quote_reference;
    private String msg;

    
    public Response() {
    }
    public String getSuccess() {
        return success;
    }
    public void setSuccess(String success) {
        this.success = success;
    }
    public double getPremium() {
        return premium;
    }
    public void setPremium(double premium) {
        this.premium = premium;
    }
    public String getQuote_reference() {
        return quote_reference;
    }
    public void setQuote_reference(String quote_reference) {
        this.quote_reference = quote_reference;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    
    

}
