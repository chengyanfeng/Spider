package cn.datahunter.spider.schedule;

import cn.datahunter.spider.process.PopulationStructureProcess;
import cn.datahunter.spider.util.GNPandGDPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.testng.annotations.Test;
import us.codecraft.webmagic.Spider;

/**
 * Created by Administrator on 2017/7/6 0006.
 */
public class PopulationStructure {

    @Autowired
    PopulationStructureProcess PopulationStructureProcess;
@Test
    @Scheduled(cron="0 0 0 1 1/6 ?", zone = "Asia/Shanghai")
    public void executeStructure() {
        GNPandGDPUtil.DATALIST.clear();
        GNPandGDPUtil.NAMELIST.clear();
        GNPandGDPUtil.ColumnName="";
        GNPandGDPUtil.ColumnName="Structure";
        GNPandGDPUtil.timp=0;
        for(int i=1955;i<2016;i++){
        GNPandGDPUtil.timp=i;
        String url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgnd&rowcode=zb&colcode=sj&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22sj%22%2C%22valuecode%22%3A%22"+i+"%22%7D%5D&k1=1500951488034";

            Spider.create(new PopulationStructureProcess()).addUrl(url)
                .thread(1).run();
        }
    }
    @Test
    @Scheduled(cron="0 0 0 1 1/6 ?", zone = "Asia/Shanghai")
    public void executeIncrease() {
        GNPandGDPUtil.DATALIST.clear();
        GNPandGDPUtil.NAMELIST.clear();
        GNPandGDPUtil.ColumnName="";
        GNPandGDPUtil.ColumnName="Increase";
        GNPandGDPUtil.timp=0;
        for(int i=1955;i<2016;i++) {
            GNPandGDPUtil.timp=i;
            String url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgnd&rowcode=sj&colcode=zb&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22sj%22%2C%22valuecode%22%3A%22"+i+
            "%22%7D%5D&k1=1500955704876";



        Spider.create(new PopulationStructureProcess()).addUrl(url)
                .thread(1).run();
        }
    }
    @Test
    @Scheduled(cron="0 0 0 1 1/6 ?", zone = "Asia/Shanghai")
    public void executeStudent() {
        GNPandGDPUtil.DATALIST.clear();
        GNPandGDPUtil.NAMELIST.clear();
        GNPandGDPUtil.ColumnName="";
        GNPandGDPUtil.ColumnName="Student";
        String url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgnd&rowcode=sj&colcode=zb&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22sj%22%2C%22valuecode%22%3A%22LAST20%22%7D%5D&k1=1500958970665";
        Spider.create(new PopulationStructureProcess()).addUrl(url)
                .thread(1).run();
    }

}
