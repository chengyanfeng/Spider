package cn.datahunter.spider.util;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/6/15 0015.
 */

public class PayForUtil {
   public final static String APPID="wxcef2b3836a816181";
   public final static String secret="67525a90d4eae7c7ad47c592f982c1c2";
    public static List<String> NAMELIST=new ArrayList<>();
    public static List<Object> DATALIST=new ArrayList<>();
    public static  List<String> getCategory(String category) {

        if(category.equals("usersummary")){
           List list=new ArrayList<>();
            list.add("new_user");
            list.add("cancel_user");
            list.add("user_source");
            list.add("time");
           /* try {
                list.add( URLEncoder.encode("时间","UTF-8"));
            }catch (Exception e){

            }*/
        return list;
        }else if(category.equals("upstreammsghour")){
            List list=new ArrayList<>();

            list.add("user_source");
            list.add("msg_user");
            list.add("msg_type");
            list.add("msg_count");
            list.add("ref_hour");
            list.add("date");
        /*    try {
                list.add( URLEncoder.encode("时间","gbk"));
            }catch (Exception e){

            }*/



            return list;
        }else if(category.equals("interfacesummaryhour")){
            List<String> list=new ArrayList<>();

            list.add("callback_count");
            list.add("fail_count");
            list.add("total_time_cost");
            list.add("max_time_cost");
            list.add("ref_hour");
            list.add("date");
           return list;
        }
        else if(category.equals("articlesummary")){
            List list=new ArrayList<>();

            list.add("title");//标题
            list.add("int_page_read_count");//原文页的阅读次数
            list.add("ori_page_read_count");//图文页的阅读次数
            list.add("add_to_fav_count");//收藏的次数
            list.add("int_page_read_user");//图文页（点击群发图文卡片进入的页面）的阅读人数
            list.add("msgid");//请注意：这里的msgid实际上是由msgid（图文消息id，这也就是群发接口调用后返回的msg_data_id）和index（消息次序索引）组成， 例如12003_3， 其中12003是msgid，即一次群发的消息的id； 3为index，假设该次群发的图文消息共5个文章（因为可能为多图文），3表示5个中的第3个
            list.add("ori_page_read_user");//原文页（点击图文页“阅读原文”进入的页面）的阅读人数，无原文页时此处数据为0
            list.add("user_source");//在获取图文阅读分时数据时才有该字段，代表用户从哪里进入来阅读该图文。0:会话;1.好友;2.朋友圈;3.腾讯微博;4.历史消息页;5.其他
            list.add("add_to_fav_user");//收藏的人数
            list.add("share_user");//分享的人数
            list.add("share_count");//分享的次数
        list.add("date"); //日期



            return list;
        }else if(category.equals("userreadhour")){
            List list=new ArrayList<>();

            list.add("total_online_time");//阅读在线时长
            list.add("int_page_read_count");//原文页的阅读次数
            list.add("ori_page_read_count");//图文页的阅读次数
            list.add("add_to_fav_count");//收藏的次数
            list.add("int_page_read_user");//图文页（点击群发图文卡片进入的页面）的阅读人数
            list.add("ori_page_read_user");//原文页（点击图文页“阅读原文”进入的页面）的阅读人数，无原文页时此处数据为0
            list.add("user_source");//在获取图文阅读分时数据时才有该字段，代表用户从哪里进入来阅读该图文。0:会话;1.好友;2.朋友圈;3.腾讯微博;4.历史消息页;5.其他
            list.add("add_to_fav_user");//收藏的人数
            list.add("share_user");//分享的人数
            list.add("share_count");//分享的次数
            list.add("ref_hour");

                list.add("date"); //日期



            return list;
        }else if(category.equals("usersharehour")){
            List list=new ArrayList<>();
            list.add("share_scene");//分享的场景1代表好友转发 2代表朋友圈 3代表腾讯微博 255代表其他
            list.add("user_source");//在获取图文阅读分时数据时才有该字段，代表用户从哪里进入来阅读该图文。0:会话;1.好友;2.朋友圈;3.腾讯微博;4.历史消息页;5.其他
            list.add("share_user");//分享的人数
            list.add("share_count");//分享的次数
            list.add("ref_hour");

                list.add("date"); //日期



            return list;
        }
    return new ArrayList<>();
    }
    public  static String getCategoryName(String category){
        switch (category){
            case "usersummary": return "微信用户管理统计";
            case "upstreammsghour": return "微信消息管理统计";
            case "interfacesummaryhour":return "微信接口管理统计";
            case "articlesummary":return "微信图文群发数据统计";
            case "userreadhour": return "微信图文数据统计";
            case "usersharehour": return "微信图文分享转发数据统计";
        }
        return "";
    }
    //获取每天的日期
    public static String getDate(int i,String category){
        if(!CommonUtils.loctionFileexist("/data/dataspider/InterfaceAPI/"+getCategoryName(category)+".csv")){
        Date date=new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.set(2017, 04, 22);
        calendar.add(calendar.DAY_OF_YEAR,+i);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
        date=calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
        }
        else{

            Date date=new Date();
            String formatterDate = getFormatterDate(date);
            return formatterDate;


        }

    }
    //获取直到今天为止时间长度
    public static Integer getDateLong(String category){
        if(!CommonUtils.loctionFileexist("/data/dataspider/InterfaceAPI/"+getCategoryName(category)+".csv")) {
            Calendar cal = Calendar.getInstance();
            cal.set(2017, 04, 22);
            long time1 = cal.getTimeInMillis();
            cal.setTime(new Date());
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);

            return Integer.parseInt(String.valueOf(between_days));
        }else{
            return 1;
        }
    }
//格式化日期并返回2017-03-01 的string类型
    public static  String getFormatterDate(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DAY_OF_YEAR,-1);
        date=calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

        public static  String getthType(String name){
            if(name.contains("int")||name.contains("count")||name.contains("page")||name.contains("share")||name.contains("user")||name.contains("type")||name.contains("数")||name.contains("人口")||name.contains("比")||name.contains("cost")||name.contains("元")||name.contains("温度")||name.contains("AQI"))
            {
                return "number";
            }
            else if(name.contains("msgid")||name.contains("total")||name.contains("max")||name.contains("地区")||name.contains("季度")||name.contains("ref_hour")){
                return "text";
            }
            else if(name.contains("时间")||name.contains("年份")||name.contains("date")){
                return "date";
            }else {
                return "text";
            }}

    public static  String getthName(String name){

        if(name.equals("地级区划数(个)"))
        {
            return "diqucount";
        }
        if(name.equals("地级市数(个)"))
        {
            return "dijishishu";
        }
        if(name.equals("县级区划数(个)"))
        {
            return "xianjiqushu";
        }
        if(name.equals("市辖区数(个)"))
        {
            return "shixiaqushu";
        }
        if(name.equals("县级市数(个)"))
        {
            return "xianjishishu";
        }
        if(name.equals("县数(个)"))
        {
            return "xianshu";
        }
        if(name.equals("自治县数(个)"))
        {
            return "zhizixianshu";
        }

        if(name.equals("ref_date"))
        {
            return "数据日期";
        }
        else if(name.equals("stat_date")){
            return "统计日期";
        }
        else if(name.equals("msgid")){
            return "图文id";
        }
        else if(name.equals("msgid")){
            return "图文id";
        }
        else if(name.equals("title")){
            return "图文消息的标题";
        }
        else if(name.equals("int_page_read_user")){
            return "图文页的阅读人数";
        }
        else if(name.equals("int_page_read_count")){
            return "图文页的阅读次数";
        }
        else if(name.equals("ori_page_read_user")){
            return "原文页的阅读人数";
        }
        else if(name.equals("ori_page_read_count")){
            return "原文页的阅读次数";
        }
        else if(name.equals("share_scene")){
            return "分享的场景";
        }
        else if(name.equals("share_user")){
            return "分享的人数";
        }
        else if(name.equals("share_count")){
            return "分享的次数";
        }
        else if(name.equals("add_to_fav_user")){
            return "收藏的人数";
        }
        else if(name.equals("add_to_fav_count")){
            return "收藏的次数";
        }
        else if(name.equals("target_user")){
            return "送达人数";
        }
        else if(name.equals("user_source")){
            return "在获取图文阅读";
        } else if(name.equals("new_user")){
            return "新增的用户数量";
        }
        else if(name.equals("cancel_user")){
            return "取消关注的用户数量";
        }
        else if(name.equals("cumulate_user")){
            return "总用户量";
        }
        else if(name.equals("msg_type")){
            return "消息类型";
        }
        else if(name.equals("msg_user")){
            return "上行发送了消息的用户数";
        }
        else if(name.equals("msg_count")){
            return "上行发送了消息总数";
        }else if(name.equals("count_interval")){
            return "当日发送消息量";
        }
        else if(name.equals("count_interval")){
            return "当日发送消息量";
        }
        else if(name.equals("date")){
            return "时间";
        }
        else if(name.equals("callback_count")){
            return "通过服务器配置地址";
        }
        else if(name.equals("fail_count")){
            return "上述动作的失败次数";
        }
        else if(name.equals("total_time_cost")){
            return "上述动作的失败总次数";
        }
        else if(name.equals("max_time_cost")){
            return "最大耗时";
        }
        else if(name.equals("ref_hour")){
            return "时辰";
        }
        else if(name.equals("年")){
            return "year";
        }
        else if(name.equals("季度")){
            return "quarter";
        }
        else if(name.equals("地区生产总值(亿元)")){
            return "gdp";
        }
        else if(name.equals("地区")){
            return "distcmap";
        }
        else if(name.equals("年份")){
            return "year";
        }
        else if(name.equals("农产品生产价格指数_当季值")){
            return "agproducts";
        }else if(name.equals("种植业产品生产价格指数_当季值")){
            return "plant";
        }else if(name.equals("粮食生产价格指数_当季值")){
            return "food";
        }else if(name.equals("谷物生产价格指数_当季值")){
            return "cereal";
        }else if(name.equals("小麦生产价格指数_当季值")){
            return "wheat";
        }else if(name.equals("稻谷生产价格指数_当季值")){
            return "paddy";
        }else if(name.equals("玉米生产价格指数_当季值")){
            return "maize";
        }else if(name.equals("豆类生产价格指数_当季值")){
            return "bean";
        }else if(name.equals("大豆生产价格指数_当季值")){
            return "soy";
        }else if(name.equals("薯类生产价格指数_当季值")){
            return " potato";
        }else if(name.equals("油料生产价格指数_当季值")){
            return "oil";
        }else if(name.equals("棉花生产价格指数_当季值")){
            return "cotton";
        }else if(name.equals("糖料生产价格指数_当季值")){
            return "sugar ";
        }else if(name.equals("烟叶生产价格指数_当季值")){
            return " leaf";
        }else if(name.equals("蔬菜生产价格指数_当季值")){
            return "greens";
        }else if(name.equals("水果生产价格指数_当季值")){
            return "homegrown";
        }else if(name.equals("茶叶生产价格指数_当季值")){
            return "tea";
        }else if(name.equals("林业产品生产价格指数_当季值")){
            return "forestry";
        }else if(name.equals("木材生产价格指数_当季值")){
            return "wood";
        }else if(name.equals("竹材生产价格指数_当季值")){
            return "bamboo";
        }else if(name.equals("胶脂和果实类林产品生产价格指数_当季值")){
            return "rubber";
        }else if(name.equals("畜牧业产品生产价格指数_当季值")){
            return "animal";
        }else if(name.equals("猪(毛重)生产价格指数_当季值")){
            return "pig";
        }else if(name.equals("牛(毛重)生产价格指数_当季值")){
            return "cow";
        }else if(name.equals("羊(毛重)生产价格指数_当季值")){
            return "sheep";
        }else if(name.equals("肉禽(毛重)生产价格指数_当季值")){
            return "meat";
        }else if(name.equals("禽蛋生产价格指数_当季值")){
            return "eggs";
        }else if(name.equals("奶类生产价格指数_当季值")){
            return "mike";
        }
        else if(name.equals("毛绒类生产价格指数_当季值")){
            return "plush";
        }else if(name.equals("渔业产品生产价格指数_当季值")){
            return "fishery";
        }else if(name.equals("海水水产品生产价格指数_当季值")){
            return "seawater";
        }else if(name.equals("海水捕捞产品生产价格指数_当季值")){
            return "catch";
        }else if(name.equals("海水养殖产品生产价格指数_当季值")){
            return "breed";
        }else if(name.equals("淡水捕捞产品生产价格指数_当季值")){
            return "freshwater";
        }else if(name.equals("淡水养殖产品生产价格指数_当季值")){
            return "freshwaterulture";
        }else if(name.equals("居民人均可支配收入_累计值(元)")){
            return "resident";
        }else if(name.equals("城镇居民人均可支配收入_累计值(元)")){
            return "towner";
        }else if(name.equals("农村居民人均可支配收入_累计值(元)")){
            return "ruralresidents";
        }else if(name.equals("居民人均消费支出_累计值(元)")){
            return "disbursement";
        }
        else if(name.equals("城镇居民人均消费支出_累计值(元)")){
            return "townerconsume";
        }
        else if(name.equals("农村居民人均消费支出_累计值(元)")){
            return "countryconsume";
        }else if(name.equals("年末常住人口(万人)")){
            return "yearallpeople";
        }else if(name.equals("城镇人口(万人)")){
            return "towerpeople";
        }
        else if(name.equals("乡村人口(万人)")){
            return "countrypeople";
        }
        else if(name.equals("乡村人口(万人)")){
            return "countrypeople";
        }
        else if(name.equals("性别比(女=100)(人口抽样调查)(女=100)")){
            return "proportion";
        }
        else if(name.equals("年末总人口(万人)")){
            return "allpeople";
        }else if(name.equals("0-14岁人口(万人)")){
            return "onetofoutteen";
        }else if(name.equals("15-64岁人口(万人)")){
            return "fifteentosixty";
        }else if(name.equals("65岁及以上人口(万人)")){
            return "upsixtyfive";
        }
        else if(name.equals("老年抚养比(%)")){
            return "oldpratio";
        }
        else if(name.equals("总抚养比(%)")){
            return "allratio";
        }
        else if(name.equals("少儿抚养比(%)")){
            return "yangratio";
        }
        else if(name.equals("人口出生率(‰)")){
            return "populaceratio";
        }
        else if(name.equals("人口死亡率(‰)")){
            return "dieratio";
        }
        else if(name.equals("人口自然增长率(‰)")){
            return "natureratio";
        }
        else if(name.equals("普通高等学校数(所)")){
            return "school";
        }else if(name.equals("普通高等学校招生数(万人)")){
            return "recruitstudent";
        }else if(name.equals("普通高等学校本科招生数(万人)")){
            return "undergraduate";
        }else if(name.equals("普通高等学校专科招生数(万人)")){
            return "juniorcollege";
        }else if(name.equals("普通高等学校在校学生数(万人)")){
            return "allstudent";
        }else if(name.equals("普通高等学校本科在校学生数(万人)")){
            return "undergraduate_student";
        }else if(name.equals("普通高等学校专科在校学生数(万人)")){
            return "juniorcollege_stduent";
        }else if(name.equals("普通高等学校预计毕业生数(万人)")){
            return "predictstudent";
        }else if(name.equals("普通高等学校本科预计毕业生数(万人)")){
            return "predict_ben_student";
        }else if(name.equals("普通高等学校专科预计毕业生数(万人)")){
            return "predict_zhuan_student";
        }else if(name.equals("普通高等学校毕(结)业生数(万人)")){
            return "graduate_stduent";
        }else if(name.equals("普通高等学校本专科授予学位数(万人)")){
            return "predict_zhuan_accredit";
        }
        else if(name.equals("普通高等学校专科毕(结)业生数(万人)")){
            return "graduate_zhuan_stduent";
        }
        else if(name.equals("普通高等学校本科毕(结)业生数(万人)")){
            return "graduate_ben_stduent";
        }
        else if(name.equals("城市")){
            return "city";
        }else if(name.equals("省份")){
            return "province";
        }else if(name.equals("天气")){
            return "weather";
        }else if(name.equals("最低气温℃")){
            return "losttemperature";
        }else if(name.equals("最高气温℃")){
            return "toptemperature";
        }else if(name.equals("风向")){
            return "wind";
        }else if(name.equals("风速")){
            return " windspeed";
        }
        else if(name.equals("空气质量级别")){
            return "air";
        }
        else if(name.equals("时间")){
            return "date";
        }
        else if(name.equals("省市")){
            return "city";
        }
        else if(name.equals("年度")){
            return "year";
        }
































        else
        {return "日期";}

        }
}
