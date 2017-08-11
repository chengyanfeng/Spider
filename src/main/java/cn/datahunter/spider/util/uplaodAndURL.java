package cn.datahunter.spider.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class uplaodAndURL {
    /**
     * 上传文件
     * key="mrocker"
     * mode不是累加的标记1，累加2
     * th:必填项，表头的json 字符串
     */
    public static void upload(String name,File file,String key,String mode,String fmt,String th) {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://dev.datahunter.cn/api/upload");
        try {
            //获取传进来的File文件
            FileBody bin = new FileBody(file);
            //StringBody传入文件的格式
            StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);

            HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("bin", bin).addPart("comment", comment).setCharset(CharsetUtils.get("UTF-8")).build();
            //httppost url 的链接和post请求的File拼装成post提交
            httppost.setEntity(reqEntity);

            System.out.println("执行请求 request " + httppost.getRequestLine());
            //执行post请求，返回response
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println("获取返回状态---->"+response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("获取返回内容---->"+"Response content length: " + resEntity.getContentLength());
                    // 打印响应内容,必须要转换格式！！！不然获取不到msg的内容！！！
                    /*System.out.println(EntityUtils.toString(resEntity,
                            Charset.forName("UTF-8")));*/
                    String s = EntityUtils.toString(resEntity,
                            Charset.forName("UTF-8"));
                    //String转成JSON
                    JSONObject jsStr = JSONObject.parseObject(s);
                    JSONObject msg = jsStr.getJSONObject("msg");
                    //获取JSon的url值
                    String url= msg.getString("url");
                    System.out.print("------获取url的值-->"+url.toString());
                    //上传url和name,和key
                    Post(name,url,key,mode,fmt,th);
                    System.out.print(name);

                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
     */

    public static void Post(String name,String url,String key,String mode,String fmt,String th){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost("http://dev.datahunter.cn/api/pub");          //这里用上本机的某个工程做测试
            //创建参数列表
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            //添加上post的请求携带的参数
            /**name:名称
             * url:生成的url
             * key:必填项
             * mode:是否为全量更新，1是全量，2是增量
             */

            list.add(new BasicNameValuePair("name", name));

            list.add(new BasicNameValuePair("url", url));
            list.add(new BasicNameValuePair("key",key));
            list.add(new BasicNameValuePair("mode",mode));
            list.add(new BasicNameValuePair("fmt",fmt));
            list.add(new BasicNameValuePair("th",th));
            //url格式编码
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list,"UTF-8");
            post.setEntity(uefEntity);
            System.out.println("\n"+"POST 请求...." + post.getURI());

            //执行请求
            CloseableHttpResponse httpResponse = httpclient.execute(post);
            try{
                System.out.println("获取返回状态---->"+httpResponse.getStatusLine());

                HttpEntity entity = httpResponse.getEntity();
                System.out.println(EntityUtils.toString(entity,
                        Charset.forName("UTF-8")));
                if (null != entity){
                    System.out.println("-------------------------------------------------------");
                    System.out.println(EntityUtils.toString(uefEntity));
                    System.out.println("-------------------------------------------------------");
                }
            } finally{
                httpResponse.close();
            }

        } catch( UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try{
                if(httpclient !=null){
                    httpclient.close();
                }


            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    /**
     * 获取所有的csv的文件列表。
     *
     */
    public List<File>  getListFiles(){
        List<File> files=new ArrayList<File>();
        files.add(new File("F:\\data\\dataspider\\InterfaceAPI\\weather_maincity\\天气数据.csv"));
        /*files.add(new File("E:\\InterfaceAPI\\地区居民年度数据.csv"));
        files.add(new File("E:\\InterfaceAPI\\各地区农副产品价格指数.csv"));
        files.add(new File("E:\\InterfaceAPI\\全国GDP每年的数据.csv"));
        files.add(new File("E:\\InterfaceAPI\\全国居民季度收支表.csv"));
        files.add(new File("E:\\InterfaceAPI\\全国居民年度数据.csv"));
        files.add(new File("E:\\InterfaceAPI\\人民币汇率.csv"));
        files.add(new File("E:\\InterfaceAPI\\省份GDP每年的数据.csv"));
        files.add(new File("E:\\InterfaceAPI\\industry\\工业指数.csv"));
        files.add(new File("E:\\InterfaceAPI\\maincity\\主要城市消费价格指数.csv"));
        files.add(new File("E:\\InterfaceAPI\\nonindustry\\非制造业指数.csv"));
        files.add(new File("E:\\InterfaceAPI\\populationall\\全国分类总人数.csv"));
        files.add(new File("E:\\InterfaceAPI\\populationcity\\全国大型市总人数.csv"));
        files.add(new File("E:\\InterfaceAPI\\populationprovince\\全国各省总人数.csv"));
        files.add(new File("E:\\InterfaceAPI\\postCode\\全国邮编.csv"));
        files.add(new File("E:\\InterfaceAPI\\province\\居民消费价格指数.csv"));
        files.add(new File("E:\\InterfaceAPI\\regioncode\\全国行政区位码.csv"));
*/

        return files;

    }
    /**
     * 发送post请求微信接口
     */

    public static JSONObject weiXinPost(String url,String beginData,String endData){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        JSONObject accessToken=null;
        try {

            HttpPost post = new HttpPost(url);
            //创建参数列表
            JSONObject jo = new JSONObject();
            jo.put("begin_date",beginData);
            jo.put("end_date",endData);
            //StringEntity格式编码,跟上面的不一样，因为 HttpEntity entity；不能传递json数据
            StringEntity stringEntity=new StringEntity(jo.toString(), HTTP.UTF_8);
            post.setEntity(stringEntity);
            System.out.println("\n"+"POST 请求...." + post.getURI());

            //执行请求
            CloseableHttpResponse httpResponse = httpclient.execute(post);
            try{
                System.out.println("获取返回状态---->"+httpResponse.getStatusLine());

                HttpEntity entity = httpResponse.getEntity();

                //获取接口的数据转换成json
               accessToken = JSONObject.parseObject(EntityUtils.toString(entity,
                        Charset.forName("UTF-8")));

                if (null != entity){
                    System.out.println("-------------------------------------------------------");

                    System.out.println("-------------------------------------------------------");
                }
            } finally{
                httpResponse.close();
            }

        } catch( UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try{
                if(httpclient !=null){
                    httpclient.close();
                }


            } catch(Exception e){
                e.printStackTrace();
            }
        }
return accessToken;
    }
    public static String weiXinGetAccess_token(String url){
        CloseableHttpClient httpclient = HttpClients.createDefault();
       String Access_token=null;
        try {

            HttpGet GET = new HttpGet(url);

            System.out.println("\n"+"GET 请求...." + GET.getURI());

            //执行请求
            CloseableHttpResponse httpResponse = httpclient.execute(GET);
            try{
                System.out.println("获取返回状态---->"+httpResponse.getStatusLine());

                HttpEntity entity = httpResponse.getEntity();
                //必须要用EntityUtils中utf-8 转换才能看到返回的信息数据。
               /* System.out.println(EntityUtils.toString(entity,
                        Charset.forName("UTF-8")));*/
                //把获取的数据以UTF-8转换成string.
                JSONObject accessToken = JSONObject.parseObject(EntityUtils.toString(entity,
                        Charset.forName("UTF-8")));

               //获取json数据中的access_token的值
                Access_token = accessToken.getString("access_token");
                if (null != entity){
                    System.out.println("-------------------------------------------------------");
                    //好吧!我终于发现问题了,原来是html = EntityUtils.toString(entity);这句导致了后面的写入文件错误.entity所得到的流是不可重复读取的也就是说所得的到实体只能一次消耗完,不能多次读取,所以在执行html = EntityUtils.toString(entity)后,流就关闭了,就导致后面的读和写显示错误.
                    System.out.println(accessToken.toString());
                    System.out.println("-------------------------------------------------------");
                }
            } finally{
                httpclient.close();

            }

        } catch( UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try{
                if(httpclient !=null){
                    httpclient.close();
                }


            } catch(Exception e){
                e.printStackTrace();
            }
        }
return Access_token;
    }
}
