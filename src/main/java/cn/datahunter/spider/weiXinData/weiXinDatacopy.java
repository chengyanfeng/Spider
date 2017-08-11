package cn.datahunter.spider.weiXinData;/*
package cn.datahunter.spider.weiXinData;


import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//这是爬微信的第二种方法，备份方法。
public class weiXinDatacopy {

    public static final String ADD_URL = "https://api.weixin.qq.com/datacube/getusercumulate?access_token=JBh8euJENIpv4l5tbv-JDvvzqz4VBGjUV6JEtBfJKJu-zeu_1TfK0oL3eXPzP3_GMvM7p2g9r7xMfdst9lXpD1faRvBDKHpK3HM2TVBrD2yk7_skO4xUP6ZlZ7BLPk8ERGSfADAETF";

    public static void appadd() {

        try {
            //创建连接
            URL url = new URL(ADD_URL);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.connect();

            //POST请求
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());
            JSONObject obj = new JSONObject();
            obj.put("begin_date", "2017-06-07");
            obj.put("end_date", "2017-06-12");


            out.writeBytes(obj.toString());
            out.flush();
            out.close();

            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            System.out.println(sb);
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

   // public static void main(String[] args) {
       // appadd();
   // }

}*/
