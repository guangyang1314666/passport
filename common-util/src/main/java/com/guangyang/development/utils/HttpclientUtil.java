package com.guangyang.development.utils;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAlias;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 功能描述: http请求工具类
 * 版本号: 2.0
 * 作者: 沈光阳
 */
public class HttpclientUtil {

    /**
     * 封装get请求
     * @param url
     * @return 返回map对象
     */
    public static String doGetString(String url)   {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 设置超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000) //连接超时时间
                                        .setConnectionRequestTimeout(5000) // 连接请求超时时间
                                        .setSocketTimeout(5000)
                                        .setRedirectsEnabled(true) // 允许从定向
                                        .build();

        // 创建http GET请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
                httpclient.close();
                return result;
            }
            httpclient.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return  null;
    }

    public static Map<String,Object> doGet(String url)   {
        String returnData = doGetString(url);

        Optional<String> returnDataP = Optional.ofNullable(returnData);
        Map<String,Object> map = returnDataP.map(data -> {
            Map<String,Object> m = JSON.parseObject(data, Map.class);
            return m;
        }).orElse(null);

        return map;
    }


    /**
     * 封装post请求
     * @param url
     * @param paramMap
     * @return 返回是一个map对象
     */
    public static Map<String,Object> doPost(String url, Map<String,String> paramMap)   {
       // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建http Post请求
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {

            if(paramMap!=null){
                List<BasicNameValuePair> list=new ArrayList<>();
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    list.add(new BasicNameValuePair(entry.getKey(),entry.getValue())) ;
                }
                HttpEntity httpEntity=new UrlEncodedFormEntity(list,"utf-8");
                httpPost.setEntity(httpEntity);
            }

            // 执行请求
            response = httpclient.execute(httpPost);

            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);
                httpclient.close();
                return JSON.parseObject(result,Map.class);
            }
            httpclient.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return  null;
    }
}
