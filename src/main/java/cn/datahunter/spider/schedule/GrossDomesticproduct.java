package cn.datahunter.spider.schedule;

import cn.datahunter.spider.process.GrossDomesticcproductProcess;
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
public class GrossDomesticproduct {


    @Autowired
    private static GrossDomesticcproductProcess Gdp;


    public void doExecute() {
        grossDomestcProduct();
    }
    @Test

     //各地的GDP的数据
    @Scheduled(cron = "0 0 0 1 1/6 ?", zone = "Asia/Shanghai")
    public static void grossDomestcProduct() {
        GNPandGDPUtil.DATALIST.clear();
        GNPandGDPUtil.NAMELIST.clear();
        GNPandGDPUtil.NAMELIST.add("省市");
        GNPandGDPUtil.NAMELIST.add("时间");
        GNPandGDPUtil.NAMELIST.add("年度");
        String url = "";
        char ch='A';
        String year=null;
        //判断文件是否存在，如果存在就获取最近一个月的，如果不存在就获取所有的20年的的。
        if(CommonUtils.loctionFileexist("/data/dataspider/InterfaceAPI/全国地区GDP统计.csv"))
        {
            year="1";
        }else
        {
            year="20";
        }
        for (int i = 1; i <= 1; i++) {
            if (i <=1) {
                GNPandGDPUtil.timp =i;
                url=  "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=fsnd&rowcode=reg&colcode=sj&wds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A02010"+i+"%22%7D%5D&dfwds=%5B%7B%22wdcode%22%3A%22sj%22%2C%22valuecode%22%3A%22LAST"+year+"%22%7D%5D&k1=1497248722710";

            }
            Gdp.ARG="AREA_GDP_YEAR";
            url = url.replace(CommonUtils.getBeforeMonth(0), CommonUtils.getBeforeMonth(0));
            //各地区GDP
            Spider.create(new GrossDomesticcproductProcess()).addUrl(url)
                    .thread(1).run();
        }
    }



}
