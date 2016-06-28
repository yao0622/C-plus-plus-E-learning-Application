package com.example.yaofa.client.util;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by yaofa on 2016/4/20.
 */
public class HttpUtil {
    private static final String CharSet = "UTF-8";
    public static final String BASE_URL = "http://192.168.253.1:8080";

    public static String get(String uri, Map<String, String> parameters) {
        String result = null;
        URL url;
        try {
            url = new URL(String.format("%s?%s", uri, stringifyParameters(parameters, true)));
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();  //创建一个HTTP连接
            urlConn.setRequestMethod("GET");
            urlConn.setReadTimeout(10000);// 设置超时的时间
            urlConn.setConnectTimeout(10000);// 设置链接超时的时间
            InputStream in = new BufferedInputStream(urlConn.getInputStream()); // 获得读取的内容
            result = readInStream(in);
            in.close(); //关闭字符输入流对象
            urlConn.disconnect();   //断开连接
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String readInStream(InputStream in) {
        Scanner scanner = new Scanner(in).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    public static String post(String uri, Map<String, String> parameters, boolean encode){
        String result = null;
        URL url;
        try {
            url = new URL(uri);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection(); // 创建一个HTTP连接
            urlConn.setRequestMethod("POST"); // 指定使用POST请求方式
            urlConn.setDoOutput(true); // 从连接中读取数据
            urlConn.setUseCaches(false); // 禁止缓存
            urlConn.setDefaultUseCaches(false);
            urlConn.setConnectTimeout(10000); // 10秒
            urlConn.setReadTimeout(10000); // 10秒
            DataOutputStream out = new DataOutputStream(urlConn.getOutputStream()); // 获取输出流
            String param = stringifyParameters(parameters, encode); //连接要提交的数据
            out.writeBytes(param);//将要传递的数据写入数据输出流
            out.flush();//输出缓存
            out.close();//关闭数据输出流
            // 判断是否响应成功
            InputStream in = null;
            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = new BufferedInputStream(urlConn.getInputStream()); // 获得读取的内容
            } else {
                in = new BufferedInputStream(urlConn.getErrorStream());
            }
            result = readInStream(in);
            in.close();
            urlConn.disconnect();   //断开连接
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String post( String uri, Map<String, String> parameters) {
        return post(uri, parameters, true);
    }

    private static String stringifyParameters(Map<String, String> parameters, boolean encode){
        try {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(encode ? URLEncoder.encode(entry.getValue(), CharSet) : entry.getValue());
                sb.append("&");
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}