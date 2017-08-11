package cn.datahunter.spider.weiXinData;

import cn.datahunter.spider.util.CommonUtils;
import cn.datahunter.spider.util.PayForUtil;
import cn.datahunter.spider.util.uplaodAndURL;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class weiXinData {

   static SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd");

    //取图文群发每日数据
   // @Scheduled(cron = "0 10 07 * * *", zone = "Asia/Shanghai")
    public void getPhotoArticleAllData(){

        String category="articlesummary";

        //循环取出历史记录
        for(Integer i=0;i<PayForUtil.getDateLong(category);i++){
            //获取一天的数据

            String begin_date = PayForUtil.getDate(i,category);

            getPhotoArticleData(begin_date, begin_date, category);
        }

    }
    //获取图文群发每日数据json
    public   void getPhotoArticleData(String begin_date,String end_date,String category){


        JSONObject weiXinData = getWeiXinData(begin_date, end_date, category);

        //如果这个返回的数据为空的话，则自己添加数据
        if(weiXinData.getJSONArray("list").size()==0){
            if(PayForUtil.NAMELIST.size()==0){
                //通过相应的种类获取事先设定的参数
                PayForUtil.NAMELIST.addAll(PayForUtil.getCategory(category));
            }

            for(int i=0;i<12;i++){
                //有几列就添加几列空数据
                if(i==0){PayForUtil.DATALIST.add("");}
                else if(i==5){PayForUtil.DATALIST.add("0-0");}
               else if(i!=5&&i<11&&i>0) {PayForUtil.DATALIST.add("0");}

                else if(i==11){
                    PayForUtil.DATALIST.add(begin_date);
                }

            }



        }
        else{
            //不为空的情况下添加NAMELIST
            if(PayForUtil.NAMELIST.size()==0){
                PayForUtil.NAMELIST.addAll(PayForUtil.getCategory(category));

            }
            //不为空的情况下则取出DATALIST数据
            for(int i=0;i<weiXinData.getJSONArray("list").size(); i++){
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("title"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("int_page_read_count"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ori_page_read_count"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("add_to_fav_count"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("int_page_read_user"));
                if(weiXinData.getJSONArray("list").getJSONObject(i).get("msgid").toString().equals("0")){
                    PayForUtil.DATALIST.add("0-0");
                }else {
                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("msgid"));
                }
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ori_page_read_user"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("user_source"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("add_to_fav_user"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("share_user"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("share_count"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_date"));
            }



        }
        //若获取的就是昨天的日期的话，那么输出文件
        if(begin_date.equals(PayForUtil.getFormatterDate(new Date()))){
            List dataOut=new ArrayList<>();
            dataOut.add(CommonUtils.removeBrackets(PayForUtil.NAMELIST.toString()));
            for(int i=0;i<PayForUtil.DATALIST.size();i=i+12){
                dataOut.add(PayForUtil.DATALIST.get(i+0)+","+
                        PayForUtil.DATALIST.get(i+1)+","+
                        PayForUtil.DATALIST.get(i+2)+","+
                        PayForUtil.DATALIST.get(i+3)+","+
                        PayForUtil.DATALIST.get(i+4)+","+
                        PayForUtil.DATALIST.get(i+5)+","+
                        PayForUtil.DATALIST.get(i+6)+","+
                        PayForUtil.DATALIST.get(i+7)+","+
                        PayForUtil.DATALIST.get(i+8)+","+
                        PayForUtil.DATALIST.get(i+9)+","+
                        PayForUtil.DATALIST.get(i+10)+","+
                        PayForUtil.DATALIST.get(i+11));
            }
            try {
                FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + PayForUtil.getCategoryName(category) + begin_date + ".csv"), "UTF-8", dataOut);
                FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + PayForUtil.getCategoryName(category) + ".csv"), "UTF-8", dataOut);
                //上传到服务器中
                List<String> th=new ArrayList<>();
                for(String s :PayForUtil.NAMELIST){
                    String typeAndName = PayForUtil.getthType(s);
                    th.add("{"+"\""+"o"+"\""+":"+"\""+s+"\""+","+"\""+"n"+"\""+":"+"\""+PayForUtil.getthName(s)+"\""+","+"\""+"type"+"\""+":"+"\""+typeAndName+"\""+"}");
                }
            uplaodAndURL.upload(PayForUtil.getCategoryName(category), new File("/data/dataspider/InterfaceAPI/" + PayForUtil.getCategoryName(category) +  begin_date+ ".csv"), "mrocker", "2","gdp",th.toString());

            }catch (Exception e){
                System.out.print(e.toString());
            }finally {
                PayForUtil.DATALIST.clear();
                PayForUtil.NAMELIST.clear();
            }
        }
    }


    //获取图文统计分时数据
  //  @Scheduled(cron = "0 15 07 * * *", zone = "Asia/Shanghai")
    public void getPhotoArticleHourAllData(){

        String category="userreadhour";

        //循环取出历史记录
        for(Integer i=0;i<PayForUtil.getDateLong(category);i++){
            //获取一天的数据

            String begin_date = PayForUtil.getDate(i,category);

            getPhotoArticleHourData(begin_date, begin_date, category);
        }

    }
    //获取图文统计分时数据json
    public   void getPhotoArticleHourData(String begin_date,String end_date,String category){


        JSONObject weiXinData = getWeiXinData(begin_date, end_date, category);

        //如果这个返回的数据为空的话，则自己添加数据
        if(weiXinData.getJSONArray("list").size()==0){
            if(PayForUtil.NAMELIST.size()==0){
                //通过相应的种类获取事先设定的参数
                PayForUtil.NAMELIST.addAll(PayForUtil.getCategory(category));
            }

            for(int i=0;i<12;i++){
                //有几列就添加几列空数据
                if(i<10){PayForUtil.DATALIST.add("0");}
                else if(i==10) {PayForUtil.DATALIST.add("00:00:00");}

                else if(i==11){
                    PayForUtil.DATALIST.add(begin_date+" 00"+":"+"00"+":"+"00");
                }

            }



        }
        else{
            //不为空的情况下添加NAMELIST
            if(PayForUtil.NAMELIST.size()==0){
                PayForUtil.NAMELIST.addAll(PayForUtil.getCategory(category));

            }
            //不为空的情况下则取出DATALIST数据
            for(int i=0;i<weiXinData.getJSONArray("list").size(); i++){
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("total_online_time"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("int_page_read_count"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ori_page_read_count"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("add_to_fav_count"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("int_page_read_user"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ori_page_read_user"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("user_source"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("add_to_fav_user"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("share_user"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("share_count"));
                if(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().length()==1){
                    PayForUtil.DATALIST.add("00:00:00");
                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_date")+" 00:00:00");
                }else if(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().length()==3)
                {
                    PayForUtil.DATALIST.add("0"+weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().substring(0,1)+":"+"00"+":"+"00");
                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_date")+" "+"0"+weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().substring(0,1)+":"+"00"+":"+"00");
                }else {

                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().substring(0, 2) + ":" + "00" + ":" + "00");
                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_date")+" "+weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().substring(0,2)+":"+"00"+":"+"00");
                }


            }



        }
        //若获取的就是昨天的日期的话，那么输出文件
        if(begin_date.equals(PayForUtil.getFormatterDate(new Date()))){
            List dataOut=new ArrayList<>();
            dataOut.add(CommonUtils.removeBrackets(PayForUtil.NAMELIST.toString()));
            for(int i=0;i<PayForUtil.DATALIST.size();i=i+12){
                dataOut.add(PayForUtil.DATALIST.get(i+0)+","+
                        PayForUtil.DATALIST.get(i+1)+","+
                        PayForUtil.DATALIST.get(i+2)+","+
                        PayForUtil.DATALIST.get(i+3)+","+
                        PayForUtil.DATALIST.get(i+4)+","+
                        PayForUtil.DATALIST.get(i+5)+","+
                        PayForUtil.DATALIST.get(i+6)+","+
                        PayForUtil.DATALIST.get(i+7)+","+
                        PayForUtil.DATALIST.get(i+8)+","+
                        PayForUtil.DATALIST.get(i+9)+","+
                        PayForUtil.DATALIST.get(i+10)+","+
                        PayForUtil.DATALIST.get(i+11));
            }
            try {
                FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + PayForUtil.getCategoryName(category) + begin_date + ".csv"), "UTF-8", dataOut);
                FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + PayForUtil.getCategoryName(category) + ".csv"), "UTF-8", dataOut);
                //上传到服务器中
                List<String> th=new ArrayList<>();
                for(String s :PayForUtil.NAMELIST){
                    String typeAndName = PayForUtil.getthType(s);
                    th.add("{"+"\""+"o"+"\""+":"+"\""+s+"\""+","+"\""+"n"+"\""+":"+"\""+PayForUtil.getthName(s)+"\""+","+"\""+"type"+"\""+":"+"\""+typeAndName+"\""+"}");
                }
               uplaodAndURL.upload(PayForUtil.getCategoryName(category), new File("/data/dataspider/InterfaceAPI/" + PayForUtil.getCategoryName(category) +  begin_date+ ".csv"), "mrocker", "2","gdp",th.toString());

            }catch (Exception e){
                System.out.print(e.toString());
            }finally {
                PayForUtil.DATALIST.clear();
                PayForUtil.NAMELIST.clear();
            }
        }
    }

    //图文分享转发分时数据
   // @Scheduled(cron = "0 20 07 * * *", zone = "Asia/Shanghai")
    public void getShareHourAllData(){

        String category="usersharehour";

        //循环取出历史记录
        for(Integer i=0;i<PayForUtil.getDateLong(category);i++){
            //获取一天的数据

            String begin_date = PayForUtil.getDate(i,category);

            getShareHourData(begin_date, begin_date, category);
        }

    }

    //图文分享转发分时数据json
    public   void getShareHourData(String begin_date,String end_date,String category){


        JSONObject weiXinData = getWeiXinData(begin_date, end_date, category);

        //如果这个返回的数据为空的话，则自己添加数据
        if(weiXinData.getJSONArray("list").size()==0){
            if(PayForUtil.NAMELIST.size()==0){
                //通过相应的种类获取事先设定的参数
                PayForUtil.NAMELIST.addAll(PayForUtil.getCategory(category));
            }

            for(int i=0;i<6;i++){
                //有几列就添加几列空数据
                if(i<4){PayForUtil.DATALIST.add("0");}
                else if(i==4) {PayForUtil.DATALIST.add("00:00:00");}

                else if(i==5){
                    PayForUtil.DATALIST.add(begin_date+" 00"+":"+"00"+":"+"00");
                }

            }



        }
        else{
            //不为空的情况下添加NAMELIST
            if(PayForUtil.NAMELIST.size()==0){
                PayForUtil.NAMELIST.addAll(PayForUtil.getCategory(category));

            }
            //不为空的情况下则取出DATALIST数据
            for(int i=0;i<weiXinData.getJSONArray("list").size(); i++){
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("share_scene"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("user_source"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("share_user"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("share_count"));
                if(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().length()==1){
                    PayForUtil.DATALIST.add("00:00:00");
                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_date")+" 00:00:00");
                }else if(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().length()==3)
                {
                    PayForUtil.DATALIST.add("0"+weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().substring(0,1)+":"+"00"+":"+"00");
                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_date")+" "+"0"+weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().substring(0,1)+":"+"00"+":"+"00");
                }else {

                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().substring(0, 2) + ":" + "00" + ":" + "00");
                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_date")+" "+weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().substring(0,2)+":"+"00"+":"+"00");
                }


            }



        }
        //若获取的就是昨天的日期的话，那么输出文件
        if(begin_date.equals(PayForUtil.getFormatterDate(new Date()))){
            List dataOut=new ArrayList<>();
            dataOut.add(CommonUtils.removeBrackets(PayForUtil.NAMELIST.toString()));
            for(int i=0;i<PayForUtil.DATALIST.size();i=i+6){
                dataOut.add(PayForUtil.DATALIST.get(i+0)+","+
                        PayForUtil.DATALIST.get(i+1)+","+
                        PayForUtil.DATALIST.get(i+2)+","+
                        PayForUtil.DATALIST.get(i+3)+","+
                        PayForUtil.DATALIST.get(i+4)+","+
                        PayForUtil.DATALIST.get(i+5))
                       ;
            }
            try {
                FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + PayForUtil.getCategoryName(category) + begin_date + ".csv"), "UTF-8", dataOut);
                FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + PayForUtil.getCategoryName(category) + ".csv"), "UTF-8", dataOut);
                //上传到服务器中
                List<String> th=new ArrayList<>();
                for(String s :PayForUtil.NAMELIST){
                    String typeAndName = PayForUtil.getthType(s);
                    th.add("{"+"\""+"o"+"\""+":"+"\""+s+"\""+","+"\""+"n"+"\""+":"+"\""+PayForUtil.getthName(s)+"\""+","+"\""+"type"+"\""+":"+"\""+typeAndName+"\""+"}");
                }
                uplaodAndURL.upload(PayForUtil.getCategoryName(category), new File("/data/dataspider/InterfaceAPI/" + PayForUtil.getCategoryName(category) +  begin_date+ ".csv"), "mrocker", "2","gdp",th.toString());

            }catch (Exception e){
                System.out.print(e.toString());
            }finally {
                PayForUtil.DATALIST.clear();
                PayForUtil.NAMELIST.clear();
            }
        }
    }



    //消息返回数据
   // @Scheduled(cron = "0 08 07 * * *", zone = "Asia/Shanghai")
    public void getNoticeAllData(){

        String category="upstreammsghour";

        //循环取出历史记录
        for(Integer i=0;i<PayForUtil.getDateLong(category);i++){
            //获取一天的数据

            String begin_date = PayForUtil.getDate(i,category);

            getNoticeData(begin_date, begin_date,category);
        }

    }
 //获取返回消息json数据
    public   void getNoticeData(String begin_date,String end_date,String category){


        JSONObject weiXinData = getWeiXinData(begin_date, end_date, category);

        //如果这个返回的数据为空的话，则自己添加数据
        if(weiXinData.getJSONArray("list").size()==0){
            if(PayForUtil.NAMELIST.size()==0){
                //通过相应的种类获取事先设定的参数
                PayForUtil.NAMELIST.addAll(PayForUtil.getCategory(category));
            }

            for(int i=0;i<6;i++){
                //有几列就添加几列空数据
                if(i<4) {PayForUtil.DATALIST.add("0");}
                else if(i==4){
                    PayForUtil.DATALIST.add("00:00:00");
                }
                else if(i==5){
                    PayForUtil.DATALIST.add(begin_date+" 00"+":"+"00"+":"+"00");
                }

            }



        }
        else{
            //不为空的情况下添加NAMELIST
            if(PayForUtil.NAMELIST.size()==0){
                PayForUtil.NAMELIST.addAll(PayForUtil.getCategory(category));

            }
            //不为空的情况下则取出DATALIST数据
            for(int i=0;i<weiXinData.getJSONArray("list").size(); i++){
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("user_source"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("msg_user"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("msg_type"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("msg_count"));
                if(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().length()==1){
                    PayForUtil.DATALIST.add("00:00:00");
                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_date")+" 00:00:00");
                }else if(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().length()==3)
                {
                    PayForUtil.DATALIST.add("0"+weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().substring(0,1)+":"+"00"+":"+"00");
                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_date")+" "+"0"+weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().substring(0,1)+":"+"00"+":"+"00");
                }else {

                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().substring(0, 2) + ":" + "00" + ":" + "00");
                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_date")+" "+weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().substring(0,2)+":"+"00"+":"+"00");
                }
            }



        }
        //若获取的就是昨天的日期的话，那么输出文件
        if(begin_date.equals(PayForUtil.getFormatterDate(new Date()))){
            List dataOut=new ArrayList<>();
            dataOut.add(CommonUtils.removeBrackets(PayForUtil.NAMELIST.toString()));
            for(int i=0;i<PayForUtil.DATALIST.size();i=i+6){
                dataOut.add(PayForUtil.DATALIST.get(i+0)+","+
                        PayForUtil.DATALIST.get(i+1)+","+
                        PayForUtil.DATALIST.get(i+2)+","+
                        PayForUtil.DATALIST.get(i+3)+","+
                        PayForUtil.DATALIST.get(i+4)+","+
                        PayForUtil.DATALIST.get(i+5));
            }
            try {
                FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + "微信消息管理统计" + begin_date + ".csv"), "UTF-8", dataOut);
                FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + "微信消息管理统计" + ".csv"), "UTF-8", dataOut);
                //上传到服务器中
                List<String> th=new ArrayList<>();
                for(String s :PayForUtil.NAMELIST){
                    String typeAndName = PayForUtil.getthType(s);
                    th.add("{"+"\""+"o"+"\""+":"+"\""+s+"\""+","+"\""+"n"+"\""+":"+"\""+PayForUtil.getthName(s)+"\""+","+"\""+"type"+"\""+":"+"\""+typeAndName+"\""+"}");
                }
                uplaodAndURL.upload("微信消息管理统计", new File("/data/dataspider/InterfaceAPI/" + "微信消息管理统计" +  begin_date+ ".csv"), "mrocker", "2","gdp",th.toString());

            }catch (Exception e){

            }finally {
                PayForUtil.DATALIST.clear();
                PayForUtil.NAMELIST.clear();
            }
        }
    }

    //接口返回数据

    //@Scheduled(cron = "0 21 07 * * *", zone = "Asia/Shanghai")
    public void getInterfaceAllData(){

        String category="interfacesummaryhour";
        //循环取出历史记录
        for(Integer i=0;i<PayForUtil.getDateLong(category);i++){
            //获取一天的数据

            String begin_date = PayForUtil.getDate(i,category);

            getInterfaceData(begin_date, begin_date, category);
        }

    }
    //获取返回接口json数据
    public   void getInterfaceData(String begin_date,String end_date,String category){


        JSONObject weiXinData = getWeiXinData(begin_date, end_date, category);

        //如果这个返回的数据为空的话，则自己添加数据
        if(weiXinData.getJSONArray("list").size()==0){
            if(PayForUtil.NAMELIST.size()==0){
                //通过相应的种类获取事先设定的参数
                PayForUtil.NAMELIST.addAll(PayForUtil.getCategory(category));
            }

            for(int i=0;i<6;i++){
                //有几列就添加几列空数据
                if(i<4) {PayForUtil.DATALIST.add("0");}
                else if(i==4){
                    PayForUtil.DATALIST.add("00:00:00");
                }
                else if(i==5){
                    PayForUtil.DATALIST.add(begin_date+" 00"+":"+"00"+":"+"00");
                }

            }



        }
        else{
            //不为空的情况下添加NAMELIST
            if(PayForUtil.NAMELIST.size()==0){
                PayForUtil.NAMELIST.addAll(PayForUtil.getCategory(category));

            }
            //不为空的情况下则取出DATALIST数据
            for(int i=0;i<weiXinData.getJSONArray("list").size(); i++){
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("callback_count"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("fail_count"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("total_time_cost"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("max_time_cost"));
                if(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().length()==1){
                    PayForUtil.DATALIST.add("00:00:00");
                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_date")+" 00:00:00");
                }else if(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().length()==3)
                {
                    PayForUtil.DATALIST.add("0"+weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().substring(0,1)+":"+"00"+":"+"00");
                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_date")+" "+"0"+weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().substring(0,1)+":"+"00"+":"+"00");
                }else {

                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().substring(0, 2) + ":" + "00" + ":" + "00");
                    PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_date")+" "+weiXinData.getJSONArray("list").getJSONObject(i).get("ref_hour").toString().substring(0,2)+":"+"00"+":"+"00");
                }
            }



        }
        //若获取的就是昨天的日期的话，那么输出文件
        if(begin_date.equals(PayForUtil.getFormatterDate(new Date()))){
            List dataOut=new ArrayList<>();
            dataOut.add(CommonUtils.removeBrackets(PayForUtil.NAMELIST.toString()));
            for(int i=0;i<PayForUtil.DATALIST.size();i=i+6){
                dataOut.add(PayForUtil.DATALIST.get(i+0)+","+
                        PayForUtil.DATALIST.get(i+1)+","+
                        PayForUtil.DATALIST.get(i+2)+","+
                        PayForUtil.DATALIST.get(i+3)+","+
                        PayForUtil.DATALIST.get(i+4)+","+
                        PayForUtil.DATALIST.get(i+5));
            }
            try {
                FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + "微信接口管理统计" + begin_date + ".csv"), "UTF-8", dataOut);
                FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + "微信接口管理统计" + ".csv"), "UTF-8", dataOut);
                //上传到服务器中
                List<String> th=new ArrayList<>();
                for(String s :PayForUtil.NAMELIST){
                    String typeAndName = PayForUtil.getthType(s);
                    th.add("{"+"\""+"o"+"\""+":"+"\""+s+"\""+","+"\""+"n"+"\""+":"+"\""+PayForUtil.getthName(s)+"\""+","+"\""+"type"+"\""+":"+"\""+typeAndName+"\""+"}");
                }


              uplaodAndURL.upload("微信接口管理统计", new File("/data/dataspider/InterfaceAPI/" + "微信接口管理统计" + begin_date + ".csv"), "mrocker", "2", "gdp", th.toString());
            }catch (Exception e){

            }finally {
                PayForUtil.DATALIST.clear();
                PayForUtil.NAMELIST.clear();
            }
        }
    }




     //获取用户数据

  //  @Scheduled(cron = "0 24 07 * * *", zone = "Asia/Shanghai")
    public void getUserAllData(){
        String category="usersummary";
        //循环取出历史记录
        for(Integer i=0;i<PayForUtil.getDateLong(category);i++){
            //获取一天的数据

            String begin_date = PayForUtil.getDate(i,category);

            getUserData(begin_date, begin_date,category);
        }

    }
    //获取返回用户json数据
    public   void getUserData(String begin_date,String end_date,String category){


        JSONObject weiXinData = getWeiXinData(begin_date, end_date, category);

        //如果这个返回的数据为空的话，则自己添加数据
        if(weiXinData.getJSONArray("list").size()==0){
            if(PayForUtil.NAMELIST.size()==0){
                //通过相应的种类获取事先设定的参数
                PayForUtil.NAMELIST.addAll(PayForUtil.getCategory(category));
            }

            for(int i=0;i<4;i++){
                //有几列就添加几列空数据
                if(i<3) {PayForUtil.DATALIST.add("0");}
                if(i==3){PayForUtil.DATALIST.add(begin_date);}
            }



        }
        else{
            //不为空的情况下添加NAMELIST
            if(PayForUtil.NAMELIST.size()==0){

                Iterator<String> list = weiXinData.getJSONArray("list").getJSONObject(0).keySet().iterator();
                while (list.hasNext()){
                    String next = list.next();
                    PayForUtil.NAMELIST.add(next);

                }

            }
            //不为空的情况下则取出DATALIST数据
            for(int i=0;i<weiXinData.getJSONArray("list").size(); i++){

                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("new_user"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("cancel_user"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("user_source"));
                PayForUtil.DATALIST.add(weiXinData.getJSONArray("list").getJSONObject(i).get("ref_date"));
            }



        }
        //若获取的就是昨天的日期的话，那么输出文件
        if(begin_date.equals(PayForUtil.getFormatterDate(new Date()))){
            List dataOut=new ArrayList<>();
            dataOut.add(CommonUtils.removeBrackets(PayForUtil.NAMELIST.toString()));
            for(int i=0;i<PayForUtil.DATALIST.size();i=i+4){
                dataOut.add(PayForUtil.DATALIST.get(i+0)+","+
                        PayForUtil.DATALIST.get(i+1)+","+
                        PayForUtil.DATALIST.get(i+2)+","+
                        PayForUtil.DATALIST.get(i+3));
            }
            try {
                FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + "微信用户管理统计" + begin_date + ".csv"), "UTF-8", dataOut);
                FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + "微信用户管理统计" + ".csv"), "UTF-8", dataOut);
                //上传到服务器中
                List<String> th=new ArrayList<>();
                for(String s :PayForUtil.NAMELIST){
                    String typeAndName = PayForUtil.getthType(s);
                    th.add("{"+"\""+"o"+"\""+":"+"\""+s+"\""+","+"\""+"n"+"\""+":"+"\""+PayForUtil.getthName(s)+"\""+","+"\""+"type"+"\""+":"+"\""+typeAndName+"\""+"}");
                }
               uplaodAndURL.upload("微信用户管理统计", new File("/data/dataspider/InterfaceAPI/" + "微信用户管理统计" +  begin_date+ ".csv"), "mrocker", "2","gdp",th.toString());
            }catch (Exception e){

            }finally {
                PayForUtil.DATALIST.clear();
                PayForUtil.NAMELIST.clear();
            }
        }
    }
  //获取access_token
    public   String getAccess_token(){
        String access_token = uplaodAndURL.weiXinGetAccess_token("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + PayForUtil.APPID + "&secret=" + PayForUtil.secret);
        return access_token;
    }
    /* * 获取用户分析数据接口
     *begin和endtime的获取间隔时间
     *

*/
    public  JSONObject getWeiXinData(String begin_date,String end_data,String category){

        //获取接口数据
        JSONObject jsonObject = uplaodAndURL.weiXinPost("https://api.weixin.qq.com/datacube/" + "get" + category + "?access_token=" + getAccess_token(), begin_date, end_data);
        return jsonObject;
    }

}
