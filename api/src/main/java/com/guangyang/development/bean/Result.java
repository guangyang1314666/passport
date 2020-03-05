package com.guangyang.development.bean;

import java.io.Serializable;

/**
 * 使用ajax请求访问时，可以用此工具类作为返回对象，也方便统一代码规范
 * @author 沈光阳
 * @version 2.0
 * @create 2020-02-19-14:20
 */
public class Result implements Serializable {

    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";
    public static final String WARN = "WARN";
    public static final String ERRORMSG = "ERRORMSG";//"服务器被坏人偷走了，我们正在抢夺服务器，请稍后重试";

    /**
     * 传递回去的数据，比如说从数据库中查询的数据，存放到此变量中
     */
    private Object data;

    /**
     * 返回地址，非重定向
     */
    private String returnUrl;

    /**
     * 用户重定向的路径
     */
    private String redirectReturnUrl;

    /**
     * 请使用本类的静态变量，SUCCESS,ERROR,WARN
     */
    private String result;

    /**
     * 对页面的消息提示，比如说出错的时候，给页面的错误提示信息
     */
    private String msg;

    /**
     * 用户token
     */
    private String token;

    public Result(){

    }

    public Result( Object data, String result, String msg,String returnUrl) {
        this.data = data;
        this.result = result;
        this.msg = msg;
        this.returnUrl = returnUrl;
    }


    public static void main(String[] args) {
        Result result = Result.builder().setReturnUrl("12312312");
        Result result1 = Result.builder().setData(new User());
        System.out.println("测试数据");
    }

    /**
     * 构建一个结果集对象
     * @return
     */
    public static Result builder(){
        return new Result();
    }

    /**
     *
     * @param msg
     * @param data
     * @param returnUrl
     */
    public void builderSuccess(String msg, Object data,String returnUrl){
        this.result = Result.SUCCESS;
        this.msg = msg;
        this.data = data;
        this.returnUrl = returnUrl;
    }

    /**
     * @param msg
     * @return
     */
    public void builderError(String msg, Object data,String returnUrl){
        this.result = Result.ERROR;
        this.msg = msg;
        this.data = data;
        this.returnUrl = returnUrl;
    }


    public void builderWarn(String msg, Object data,String returnUrl){
        this.returnUrl = returnUrl;
        this.msg = msg;
        this.data = data;
        this.result = Result.WARN;
    }


    public String getReturnUrl() {
        return returnUrl;
    }

    public Result setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
        return this;
    }

    public String getResult() {
        return result;
    }


    public Result setResult(String result) {
        this.result = result;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Result setToken(String token) {
        this.token = token;
        return this;
    }
}
