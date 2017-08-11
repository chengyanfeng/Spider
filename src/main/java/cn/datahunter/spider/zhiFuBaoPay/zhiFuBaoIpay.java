package cn.datahunter.spider.zhiFuBaoPay;

import cn.datahunter.spider.util.PayForUtil;
import cn.datahunter.spider.util.ZhiFuBaoDownFile;
import cn.datahunter.spider.util.uplaodAndURL;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class zhiFuBaoIpay {


    //支付宝

    @Scheduled(cron = "0 10 12 * * *", zone = "Asia/Shanghai")
    public void getData() {

        String mykey="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCQ8ddXfsFIROJ45" +
            "GXkUDMeqGIWObEejCcRAuN54n/yRSsjSt+6C3VKIuKJbplpOEcPQqpqrr4+rx3ptGjfKVpX" +
            "cPvxLDTpEHHgyMyrHXepovFgnf80KQAlcYOp/Eujma0Q6WKPc7kE9qcD2XbIE8dni/A4h" +
            "B9QiikTy4jwE4t0NdYYXu+a7fotyVC6W4IQujctV8TGFEuUKKUcxDs9w/krihQoNG0G2Z" +
            "u8eFBNenW2Ew2wlDhKqZVSjuyGmAsx/eKa1qXvDGtb6Pzt4zHtEiA+p/uj8XaLuk+GIY" +
            "vXR97wgmVz4UJgtl39MRyIyCLcds6BHgeCPTWSywKw5eWC+pmXAgMBAAECggEBAI" +
            "dS841Bz6zse1EJVMQDyKZv6Rv9AwuVKIguXTkXqgMEZxENP7IQLpJn3X8YXmypWVa7" +
            "FCaEPSSDFHd0s5Dep/UJjH+PmVqbuuZ2MvfzjBw6AAgHmxQUz4kl4RpcbjYxLS6j5xw" +
            "tm+kxa58hck+8f6iCM2ZgnGfyhgVwOsqCROnaTewqDxZR6/c3KI6Cyf0tGhQcPCllvp" +
            "8mIOdXiHPLMcw18TvqtUzROmpoAaTbedSfU+wN9vx22qm3qm56vWADhBMVugDHWMRpQ" +
            "uzxTpnbyRhNvyDYXLPryETuhpOcLcsOuUNTmrLAb6H6HasAdOJKEyJ3Hpl901SQl5Z" +
            "kTi25r0ECgYEAw5d89t8xpT3q8AoDFglGa1rRk+XIuLe1/jAKkojQhsVm2AgA/MB/jvkAwVju" +
            "4dEM8rWN7FuXkC71rReOAd715T4Ox5mbgbh+WaVc1f3f3RJnVz4uO++6IbDSLQ5xbD" +
            "ikY08BeHGS9sJRYaOFzdHgC5mkMT0E8zClfIBM3ExxtaECgYEAvbXuLd4Clii+d9PB" +
            "pif3Cit9PYz7UF3undB8fcdZWf4cQAWGT/CBH03NM9n8QFZvoAkDB2EC1hBMBEKjl" +
            "PEkHQx502/Qm81bFbqNP2qOjy98wSv/aXfDD1V2rycDliNaf7PYx9puf5Ur/LqIW8" +
            "hdy7vxAbFGg7v0R3K9z17cFDcCgYEAlnO04IGXQrB6Q+KvtQe1b2cQx19+IvjNuZVizI0" +
            "JOmImVg7yE3pPpL7NYezy9ClGeHxHi4R49aPpvxfbWH0VUR2HaOozwHkWWAbnfoQm63r4mxdtoRU" +
            "qbDgDtDzj6oc3jFx9mRRkJAQdbanHhybl3IZ20plJcXf+E+LtPJhg74ECgYBFnKx5SC3" +
            "Ky8iYT6uPPLr5kFu0ajcNlmoonogP30Z6LzROibhvPOEki3fwvtxA/3aWUrg5zO8tfW" +
            "1nYsyDqriFsD5Dx5PUl9YEcx/BVWhaQiKU/4AJQ1jgF4wlWTuHrBFZKrUxIIXLawFpC0ld5HgGekBFTr" +
            "eBP0azsPbVUQmQoQKBgF6PuXXxnzHjcEfIt03+Vt6pEy3qiZVaBq+kkpvBybFMo+c1XH8R+uHPhgESPIHDQfOg/SZ1H9pXgvXQt0cOcfWIVCedtkkMgewxb3jjdx2XgaBDpEcT1S8PQFxCpvPSUjcQDoiHoH18Kct7p3sHHITO7+hHY2yL2UzXu3Pddddo";

    String publickey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMI" +
            "IBCgKCAQEAxrwh0U50ZY3ZqvgBMRmW7CFR+aSP/2P" +
            "OShqUUFOQmKTebfGPljU9ZZZ+abVYZ22yyYkydwWnA0" +
            "CM3E/yG+ZU/AYlamLZWMVhFx724TU7Y7mEMnTXMo28J" +
            "cRLyLPa3H2QZKzlxt5agHRuYPHAsTV9LRqaVVQe4ZrX" +
            "YRmhTZVshyeuS8Dnwpl2Lx705DzMwqDTe69svlJ3lo" +
            "VZvt4/lWQ4MxI5dw7e0eRzDZeOz4grUymn011eShv7S" +
            "gNZjbYuxgY3VqTh44rIivQKB2ZC3hfJdVrlr1P3/DDE" +
            "+itQP7YMHmUVN0KsXV/XpQh4n4Y+Izxj5at6D" +
            "kWgxdzriX0g0FcvhwIDAQAB";

AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do","2016080600181280",mykey,"json","GBK",publickey,"RSA2");
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
    request.setBizContent("{" +
            "\"bill_type\":\"trade\"," +
            "\"bill_date\":\""+ "2017-06-25"+"\"" +
                "  }");
      try {
            AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);

        if(response.isSuccess()){
            System.out.println("-----------------获取URL调用成功----------------------------");
            boolean b = ZhiFuBaoDownFile.downloadFile(response.getBillDownloadUrl(), "F:\\data\\dataspider\\InterfaceAPI\\");
            if(b){
                System.out.print("下载解压包成功+解压成功");
                //上传到服务器中
                List<String> th=new ArrayList<>();
                for(String s : PayForUtil.NAMELIST){
                    String typeAndName = PayForUtil.getthType(s);
                    th.add("{"+"\""+"o"+"\""+":"+"\""+PayForUtil.getthName(s)+"\""+","+"\""+"n"+"\""+":"+"\""+s+"\""+","+"\""+"type"+"\""+":"+"\""+typeAndName+"\""+"}");
                }
                uplaodAndURL.upload("支付宝交易账单", new File("/data/dataspider/InterfaceAPI/支付宝交易账单" + PayForUtil.getFormatterDate(new Date()) + ".csv"), "mrocker", "2", "gdp",th.toString());
                System.out.print("上传服务器成功");
            }
            else{
                System.out.print("下载解压包失败");
            }


        } else {
            System.out.println("调用失败");
        }

        }
        catch (Exception e){
            e.toString();
        }
    }
    }


