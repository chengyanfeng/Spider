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
public class PopulationStructureProcess implements PageProcessor {

    private Site site =
            Site.me()
                    .setCharset("utf-8")
                    .setSleepTime(3000)
                    .setRetryTimes(3)
                    .setCycleRetryTimes(3)
                    .setTimeOut(60000)
                    .addCookie("JSESSIONID", "FAF311F08F2A174E67E5776D2B8D1C2C")
                    .addCookie("u", "6")
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
        //备用list
        List<String> dataOut = new ArrayList<>();
        List<String> dataLst = new ArrayList<>();
        //List<String> nameLst = new ArrayList<>();
        //解析page
        String rawText = page.getRawText();
        JSONObject jsonObject = JSON.parseObject(rawText);
        JSONObject returnData  = jsonObject.getJSONObject("returndata");
        //人口结构
        Map<String, List> populationMap = getPopulation(dataLst, returnData);

        List<String> datalist = populationMap.get("data");
        if(GNPandGDPUtil.NAMELIST.size()==0) {
            GNPandGDPUtil.NAMELIST.addAll(populationMap.get("namelist"));
        }
      if(GNPandGDPUtil.ColumnName.equals("Structure")) {
          for (int i = 0; i < datalist.size(); i = i + 8) {
              GNPandGDPUtil.DATALIST.add(datalist.get(i) + "," +
                      datalist.get(i + 1) + "," +
                      datalist.get(i + 2) + "," +
                      datalist.get(i + 3) + "," +
                      datalist.get(i + 4) + "," +
                      datalist.get(i + 5) + "," +
                      datalist.get(i + 6) + "," +
                      datalist.get(i + 7));
          }
      }else if(GNPandGDPUtil.ColumnName.equals("Increase")) {
          for (int i = 0; i < datalist.size(); i = i + 4) {
              GNPandGDPUtil.DATALIST.add(datalist.get(i) + "," +
                      datalist.get(i + 1) + "," +
                      datalist.get(i + 2) + "," +
                      datalist.get(i + 3));
          }

      }
        else if(GNPandGDPUtil.ColumnName.equals("Student")){
          for(int i = 0; i < datalist.size(); i = i + 15){
              GNPandGDPUtil.DATALIST.add(datalist.get(i) + "," +
                      datalist.get(i + 1) + "," +
                      datalist.get(i + 2) + "," +
                      datalist.get(i + 3) + "," +
                      datalist.get(i + 4) + "," +
                      datalist.get(i + 5) + "," +
                      datalist.get(i + 6) + "," +
                      datalist.get(i + 7) + "," +
                      datalist.get(i + 8) + "," +
                      datalist.get(i + 9) + "," +
                      datalist.get(i + 10) + "," +
                      datalist.get(i + 11) + "," +
                      datalist.get(i + 12) + "," +
                      datalist.get(i + 13) + "," +

                       datalist.get(i + 14));
          }

      }
        try {
        if(GNPandGDPUtil.ColumnName.equals("Structure")&&GNPandGDPUtil.timp==2015) {
            dataOut.add(CommonUtils.removeBrackets(GNPandGDPUtil.NAMELIST.toString()));
            dataOut.addAll(GNPandGDPUtil.DATALIST);
            FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + "人口结构比" + CommonUtils.getBeforeMonth(0) + ".csv"), "UTF-8", dataOut);
            FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + "人口结构比" + ".csv"), "UTF-8", dataOut);
            List<String> th=new ArrayList<>();
            for(String s : GNPandGDPUtil.NAMELIST){
                String typeAndName = PayForUtil.getthType(s);
                th.add("{"+"\""+"o"+"\""+":"+"\""+PayForUtil.getthName(s)+"\""+","+"\""+"n"+"\""+":"+"\""+s+"\""+","+"\""+"type"+"\""+":"+"\""+typeAndName+"\""+"}");
            }
            uplaodAndURL.upload("人口结构比", new File("/data/dataspider/InterfaceAPI/" + "人口结构比" + CommonUtils.getBeforeMonth(0) + ".csv"), "mrocker", "2", "gdp",th.toString());
        }
        else if(GNPandGDPUtil.ColumnName.equals("Increase")&&GNPandGDPUtil.timp==2015){
            dataOut.add(CommonUtils.removeBrackets(GNPandGDPUtil.NAMELIST.toString()));
            dataOut.addAll(GNPandGDPUtil.DATALIST);
            FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + "人口增长率" + CommonUtils.getBeforeMonth(0) + ".csv"), "UTF-8", dataOut);
            FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + "人口增长率" + ".csv"), "UTF-8", dataOut);
            List<String> th=new ArrayList<>();
            for(String s : GNPandGDPUtil.NAMELIST){
                String typeAndName = PayForUtil.getthType(s);
                th.add("{"+"\""+"o"+"\""+":"+"\""+PayForUtil.getthName(s)+"\""+","+"\""+"n"+"\""+":"+"\""+s+"\""+","+"\""+"type"+"\""+":"+"\""+typeAndName+"\""+"}");
            }
            uplaodAndURL.upload("人口增长率", new File("/data/dataspider/InterfaceAPI/" + "人口增长率" + CommonUtils.getBeforeMonth(0) + ".csv"), "mrocker", "2", "gdp",th.toString());
        }else if(GNPandGDPUtil.ColumnName.equals("Student")){
            dataOut.add(CommonUtils.removeBrackets(GNPandGDPUtil.NAMELIST.toString()));
            dataOut.addAll(GNPandGDPUtil.DATALIST);
            FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + "普通高校毕业人数" + CommonUtils.getBeforeMonth(0) + ".csv"), "UTF-8", dataOut);
            FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + "普通高校毕业人数" + ".csv"), "UTF-8", dataOut);
            List<String> th=new ArrayList<>();
            for(String s : GNPandGDPUtil.NAMELIST){
                String typeAndName = PayForUtil.getthType(s);
                th.add("{"+"\""+"o"+"\""+":"+"\""+PayForUtil.getthName(s)+"\""+","+"\""+"n"+"\""+":"+"\""+s+"\""+","+"\""+"type"+"\""+":"+"\""+typeAndName+"\""+"}");
            }
            uplaodAndURL.upload("普通高校毕业人数", new File("/data/dataspider/InterfaceAPI/" + "普通高校毕业人数" + CommonUtils.getBeforeMonth(0) + ".csv"), "mrocker", "2", "gdp",th.toString());

        }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
    }

    public Map<String,List> getPopulation(List<String> dataLst,JSONObject returnData){
        int b=20;
        if(GNPandGDPUtil.ColumnName.equals("Structure")){
            b=1;
        }
        Map<String,List> map=new HashMap<>();
        List<String> namelist=new ArrayList<>();
        namelist.add("年份");
        JSONArray dataArrname = returnData.getJSONArray("wdnodes");
        for(int i=0;i<dataArrname.getJSONObject(0).getJSONArray("nodes").size();i++) {
          String  ColumnName = dataArrname.getJSONObject(0).getJSONArray("nodes").getJSONObject(i).getString("cname")
                    + "(" + dataArrname.getJSONObject(0).getJSONArray("nodes").getJSONObject(i).getString("unit") + ")";

            namelist.add(ColumnName);
        }
        JSONArray nodes=dataArrname.getJSONObject(1).getJSONArray("nodes");
        JSONArray dataArr = returnData.getJSONArray("datanodes");



            for (int i = 0; i < nodes.size(); i++) {
                dataLst.add(nodes.getJSONObject(i).get("cname").toString().substring(0,4)+"-01-01");

                for (int a=0;a<dataArr.size();a=a+b) {
                    JSONObject data = (JSONObject) dataArr.getJSONObject(a+i).
                            getJSONObject("data");
                    if (null != data && StringUtils.isNotBlank(data.toString())) {
                        String strdata = data.getString("strdata");
                        dataLst.add(strdata);

                    }
                }
            }



        map.put("data",dataLst);
        map.put("namelist",namelist);
        return map;
    }
}
