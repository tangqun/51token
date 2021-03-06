package com.sharex.token.api.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.Map;

/**
 * Created by TQ on 2017/12/6.
 * FindBugs & CheckStyle
 */
@Configuration
public class HttpUtil {

    private static Integer proxyStatus;

    @Value("${proxyStatus}")
    public void setProxyStatus(Integer proxyStatus) {
        this.proxyStatus = proxyStatus;
    }

    public static String get(String urlString, Map<String, String> headers) {

//        System.setProperty("http.proxyHost", "us3.telegram-u9unchat.top");
//        System.setProperty("http.proxyPort", "6191");
//        System.setProperty("http.proxyPassword", "qianligu02");

        String responseBody = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(urlString);

            URLConnection urlConnection;

            if (proxyStatus == null || proxyStatus == 0) {
                SocketAddress addr = new InetSocketAddress("127.0.0.1", 1080);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
                urlConnection = url.openConnection(proxy);
            } else {
                urlConnection = url.openConnection();
            }

            // 设置通用的请求属性
//            urlConnection.setRequestProperty("Accept", "application/json");
//            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");

            if (headers != null) {

                for (Map.Entry<String, String> entry : headers.entrySet()) {

                    urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
//                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                }
            }

            // 建立实际的连接
            urlConnection.connect();
            // 获取所有响应头字段
//            Map<String, List<String>> map = urlConnection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义BufferedReader输入流来读取URL的响应
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            responseBody = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseBody;
    }

    public static String post(String urlString, String param, String mediaType) {
//        PrintWriter printWriter = null;
        OutputStreamWriter outputStreamWriter = null;
//        DataOutputStream dataOutputStream = null;
        BufferedReader bufferedReader = null;
        String responseBody = null;
        try {
            URL url = new URL(urlString);

            HttpURLConnection connection;

            if (proxyStatus == null || proxyStatus == 0) {
                SocketAddress addr = new InetSocketAddress("127.0.0.1", 1080);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
                connection = (HttpURLConnection)url.openConnection(proxy);
            } else {
                connection = (HttpURLConnection)url.openConnection();
            }

            // 设置通用的请求属性
            connection.setRequestMethod("POST");
            // "application/x-www-form-urlencoded"
            connection.setRequestProperty("Content-Type", mediaType);
//            connection.setRequestProperty("Accept", "application/json");
//            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            if (null != param) {
//            printWriter = new PrintWriter(connection.getOutputStream());
                outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
//            dataOutputStream = new DataOutputStream(connection.getOutputStream());
                // 发送请求参数
//            printWriter.print(param);
                outputStreamWriter.write(param);
//            dataOutputStream.writeBytes(param);
                // flush输出流的缓冲
//            printWriter.flush();
                outputStreamWriter.flush();
//            dataOutputStream.flush();
            }

            // 定义BufferedReader输入流来读取URL的响应
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            responseBody = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            try{
//                if(printWriter != null){
//                    printWriter.close();
//                }
                if(bufferedReader != null){
                    bufferedReader.close();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        return responseBody;
    }

    public static void main(String[] args) throws IOException {
        String url = "http://test-account.9h-sports.com/Test/GetSMSCode?mobileNum=15210470906&appId=1";
        String responseBody = get(url, null);
        System.out.println(responseBody);

//        String url = "http://106.75.24.252:801/api/Manage/GrantCoins";
//        String param = "{ \"AppId\": 1, \"UnionUserId\": \"00000000-0000-0000-0000-000000000000\", \"Coins\": 10 }";
//        String responseBody = post(url, param);
//        System.out.println(responseBody);

    }
}
