package cn.datahunter.spider.schedule;

import cn.datahunter.spider.process.AgriculturalSidelineProductsProcess;
import cn.datahunter.spider.util.CommonUtils;
import cn.datahunter.spider.util.GNPandGDPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

/**
 * Created by Administrator on 2017/3/21.
 */
@Component
public class AgriculturalSidelineProducts {


    @Autowired
    private static AgriculturalSidelineProducts Gdp;


    public void doExecute() {
        agriculturalProducts();
    }

    /*public static void main(String[] args) {
        agriculturalProducts();

    }*/

    //各地的农产品价格--年度
    //@Scheduled(cron = "0 0 0 15 1/4 ?", zone = "Asia/Shanghai")

    @Scheduled(cron="0 0 0 1 1/6 ?", zone = "Asia/Shanghai")

    public static void agriculturalProducts() {
        GNPandGDPUtil.DATALIST.clear();
        GNPandGDPUtil.NAMELIST.clear();
        GNPandGDPUtil.NAMELIST.add("省市");
        GNPandGDPUtil.NAMELIST.add("年份");
        GNPandGDPUtil.NAMELIST.add("季度");
        String url = "";
       char ch='A';
        String month=null;
        //判断文件是否存在，如果存在就获取最近一个月的，如果不存在就获取所有的18个月的。
        if(CommonUtils.loctionFileexist("/data/dataspider/InterfaceAPI/全国地区农产品价格指数.csv"))
        {
            month="1";
        }else
        {
            month="18";
        }



        for (int i = 1; i < 36; i++) {
            if (i <10) {
                GNPandGDPUtil.timp =i;
              url=  "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=fsjd&rowcode=reg&colcode=sj&wds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A05010"+i+"%22%7D%5D&dfwds=%5B%7B%22wdcode%22%3A%22sj%22%2C%22valuecode%22%3A%22LAST"+month+"%22%7D%5D&k1=1497069482643";
            } else {
                GNPandGDPUtil.timp =i;
                int T=ch+i-10;
                url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=fsjd&rowcode=reg&colcode=sj&wds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A05010"+(char)(T)+"%22%7D%5D&dfwds=%5B%7B%22wdcode%22%3A%22sj%22%2C%22valuecode%22%3A%22LAST"+month+"%22%7D%5D&k1=1497069482643";

            }
            AgriculturalSidelineProductsProcess.ARG="AREA_AGRICULTURAL";
            url = url.replace(CommonUtils.getBeforeMonth(0), CommonUtils.getBeforeMonth(0));
            //各地区农业价格指数-----季度，年份，地区

            Spider.create(new AgriculturalSidelineProductsProcess()).addUrl(url)
                    .thread(1).run();
        }
    }



}
