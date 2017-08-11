package cn.datahunter.spider.process;

import cn.datahunter.spider.util.CommonUtils;
import cn.datahunter.spider.util.GNPandGDPUtil;
import cn.datahunter.spider.util.PayForUtil;
import cn.datahunter.spider.util.uplaodAndURL;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 2017/3/14.
 */
@Component
public class AgriculturalSidelineProductsProcess implements PageProcessor {

    private Site site =

            Site.me()
                .setCharset("utf-8")
                .setSleepTime(3000)
                .setRetryTimes(3)
                .setCycleRetryTimes(3)
                .setTimeOut(60000)
                    .addCookie("JSESSIONID","230C63250897C58564351909DCDE7B52")
                    .addCookie("u","6")
                    .addHeader("Accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .addHeader(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
    public static String ARG = StringUtils.EMPTY;
    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        //输出总数据dataOut---->生成.csv文件
        List<String> dataOut = new ArrayList<>();
        //获取接口的总数据
        List<String> dataLst = new ArrayList<>();
        //解析page
        String rawText = page.getRawText();
        JSONObject jsonObject = JSON.parseObject(rawText);
        JSONObject returnData  = jsonObject.getJSONObject("returndata");
       //农副产品--季度
        Map<String, List> AgriculturalMap = AgriculturalSidelineProducts( dataLst, returnData);
       //获取列名，每个页面获取一个名字。缓存到GNPandGDPUtil.NAMELIST里面去。
        GNPandGDPUtil.NAMELIST.add(GNPandGDPUtil.ColumnName);
        //获取整个页面的数据。
        List<String> datalist = AgriculturalMap.get("data");
        /**
         * 判断如果timp==1则是第一个页面，获取所有页面公用的数据头，例如2017年，第一季度，2017年第二季度。
         * 格式如下：
         * 北京市
         * 2017
         * 第三季度
         * 1023.13
         * 天津市
         * .
         * .
         * 每隔四个转成一行，第一个页面获取公共列，剩下的页面只取第四行数据就行。
         */
        if(GNPandGDPUtil.timp==1){
            for(int i=0;i<datalist.size();i=i+4) {
                GNPandGDPUtil.DATALIST.add(datalist.get(0+i).replace("自治区","").replace("省","").replace("市","").
                        replace("回族", "").replace("维吾尔","").replace("壮族","")
                        + "," +
                        datalist.get(1+i) + "," +
                        datalist.get(2+i)
                        + "," +
                        datalist.get(3+i));

            }
        }else{
            //只需要把第N个4行取出来放到前面的列中。
            for(int i=1;i< GNPandGDPUtil.DATALIST.size()+1;i++)
            {//把新的一页的数据放入到缓存的GNPandGDPUtil中。
                String s = GNPandGDPUtil.DATALIST.get(i-1)+","+datalist.get(i*4-1);
                GNPandGDPUtil.DATALIST.add(i-1,s);
                GNPandGDPUtil.DATALIST.remove(i);
                 }
        }
try {
     if(GNPandGDPUtil.timp==35){
         dataOut.add(CommonUtils.removeBrackets(GNPandGDPUtil.NAMELIST.toString()));
         dataOut.addAll(GNPandGDPUtil.DATALIST);
         FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + GNPandGDPUtil.getGNPName(ARG) + CommonUtils.getBeforeMonth(0) + ".csv"), "UTF-8", dataOut);
         FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + GNPandGDPUtil.getGNPName(ARG) + ".csv"), "UTF-8", dataOut);
            //上传到服务器中
         List<String> th=new ArrayList<>();
         for(String s : GNPandGDPUtil.NAMELIST){
             String typeAndName = PayForUtil.getthType(s);
             th.add("{"+"\""+"o"+"\""+":"+"\""+PayForUtil.getthName(s)+"\""+","+"\""+"n"+"\""+":"+"\""+s+"\""+","+"\""+"type"+"\""+":"+"\""+typeAndName+"\""+"}");
         }
         uplaodAndURL.upload(GNPandGDPUtil.getGNPName(ARG), new File("/data/dataspider/InterfaceAPI/" + GNPandGDPUtil.getGNPName(ARG) + CommonUtils.getBeforeMonth(0) + ".csv"), "mrocker", "2", "gdp",th.toString());
     }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //各省农产品----季度
    public Map<String,List> AgriculturalSidelineProducts(List<String> dataLst,JSONObject returnData){
        Map<String,List> map=new HashMap<>();
        JSONArray dataArrname = returnData.getJSONArray("wdnodes");
        GNPandGDPUtil.ColumnName=dataArrname.getJSONObject(0).getJSONArray("nodes").getJSONObject(0).getString("cname");
        JSONArray nodes=dataArrname.getJSONObject(1).getJSONArray("nodes");
        JSONArray dataArr = returnData.getJSONArray("datanodes");
        //获取众多个季度
        JSONArray jidu = dataArrname.getJSONObject(2).getJSONArray("nodes");

        for(int b=0;b<jidu.size();b++) {
          for (int i = 0; i < nodes.size(); i++) {
              //获取地区
              dataLst.add(nodes.getJSONObject(i).get("cname").toString());
              //获取年份
              dataLst.add(jidu.getJSONObject(b).getString("name").substring(0,4)+"-01-01");
              //获取季度
              dataLst.add(jidu.getJSONObject(b).getString("name"));
              JSONObject data = (JSONObject) dataArr.getJSONObject(i*jidu.size()+b).
                          getJSONObject("data");
              if (null != data && StringUtils.isNotBlank(data.toString())) {
                      String strdata = data.getString("strdata");
                      dataLst.add(strdata);

                  }
              }


          }
        //把数据列放到map中
        map.put("data",dataLst);
        return map;
    }
}
