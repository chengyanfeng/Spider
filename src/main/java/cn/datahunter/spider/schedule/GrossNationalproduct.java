package cn.datahunter.spider.schedule;

import cn.datahunter.spider.process.GrossNationalproductProcess;
import cn.datahunter.spider.util.CommonUtils;
import cn.datahunter.spider.util.GNPandGDPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.testng.annotations.Test;
import us.codecraft.webmagic.Spider;

/**
 * Created by Administrator on 2017/3/21.
 */
@Component
public class GrossNationalproduct {


    @Autowired
    private static GrossNationalproductProcess Gdp;


    public void doExecute() {
        GrossNationProcess();
    }

    /*public static void main(String[] args) {
        GrossNationProcess();

    }*/

    //
    @Test
    @Scheduled(cron = "0 0 0 1 1/6 ? ", zone = "Asia/Shanghai")
    public static void GrossNationProcess() {
        GNPandGDPUtil.DATALIST.clear();
        GNPandGDPUtil.NAMELIST.clear();
        GNPandGDPUtil.NAMELIST.add("省市");
        GNPandGDPUtil.NAMELIST.add("年份");
        GNPandGDPUtil.NAMELIST.add("季度");
        String url = "";
        char ch='A';
        String month=null;
        //判断文件是否存在，如果存在就获取最近一个月的，如果不存在就获取所有的18个月的。
        if(CommonUtils.loctionFileexist("/data/dataspider/InterfaceAPI/全国居民GNP统计.csv"))
        {
            month="1";
        }else
        {
            month="18";
        }



        for (int i = 1; i < 12; i=i+2) {
if(i<11) {
    GNPandGDPUtil.timp = i;
    url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=fsjd&rowcode=reg&colcode=sj&wds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A03000" + i + "%22%7D%5D&dfwds=%5B%7B%22wdcode%22%3A%22sj%22%2C%22valuecode%22%3A%22LAST" + month + "%22%7D%5D&k1=1497257356576";
}else{
    GNPandGDPUtil.timp = i;
    url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=fsjd&rowcode=reg&colcode=sj&wds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A0300"+i+"%22%7D%5D&dfwds=%5B%7B%22wdcode%22%3A%22sj%22%2C%22valuecode%22%3A%22LAST"+month+"%22%7D%5D&k1=1497260951878";
}
    Gdp.ARG="AREA_GNP_MONTH";
            url = url.replace(CommonUtils.getBeforeMonth(0), CommonUtils.getBeforeMonth(0));
            //各地区人均消费-----季度，年份，地区
            Spider.create(new GrossNationalproductProcess()).addUrl(url)
                    .thread(1).run();
        }
    }



}
