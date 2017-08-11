package cn.datahunter.spider.util;

import com.csvreader.CsvReader;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Administrator on 2017/6/23 0023.*/

public class ZhiFuBaoDownFile {

/* 下载远程文件并保存到本地
    * @param remoteFilePath 远程文件路径
    * @param localFilePath 本地文件路径
    */

    //下载和解压方法合并
    public static boolean  downloadFileCopy(String downFile, String filePath) {
        //此方法只能用户HTTP协议
       //输入压缩zip的流。
        ZipInputStream zis = null;
        //输出文件zip的流
        BufferedOutputStream bos = null;
        try { //解压输出的输出流
            URL url = new URL(downFile);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            zis = new ZipInputStream(connection.getInputStream(), Charset.forName("gbk"));
            ZipEntry entry = null;
              //这个是把压缩文件中的文件依次解压
            while ((entry = zis.getNextEntry()) != null&&!entry.isDirectory()) {

                        File target = new File(filePath, entry.getName());
                        if (!target.getParentFile().exists()) {
                            //假如没有文件路径创建文件父目录
                            target.getParentFile().mkdirs();
                        }
                        // 写入文件
                        bos = new BufferedOutputStream(new FileOutputStream(target));
                        int read = 0;
                     //限制的是十兆的下载文件。
                        byte[] buffer = new byte[1024 * 10];
                        while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                            bos.write(buffer, 0, read);
                        }
                        bos.flush();

            }
                    zis.closeEntry();
                } catch (IOException e) {
                    throw new RuntimeException(e);

                } finally {
                //关闭输出和输入文件流。
                    IOUtil.closeQuietly(zis, bos);

                }

        return true;
    }
    //这个是下载zip的方法----备用
    public static boolean  downloadFile1(String photoUrl, String fileName) {
        //此方法只能用户HTTP协议
        try {
            URL url = new URL(photoUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));
            //解压文件
            ZipInputStream zis = new ZipInputStream(in);
            ZipEntry entry = null;
            //
            byte[] buffer = new byte[4096];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.close();
            in.close();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    //这个是解压的zip的方法----备用
    public static void unzipMyzip(String zipPath,String outPath){
        File source = new File(zipPath);
        if (source.exists()) {
            ZipInputStream zis = null;
            BufferedOutputStream bos = null;
            try {

                zis = new ZipInputStream(new FileInputStream(source), Charset.forName("gbk"));
                ZipEntry entry = null;
                while ((entry = zis.getNextEntry()) != null
                        && !entry.isDirectory()) {
                    File target = new File(source.getParent(), entry.getName());
                    if (!target.getParentFile().exists()) {
                        // 创建文件父目录
                        target.getParentFile().mkdirs();
                    }
                    // 写入文件
                    bos = new BufferedOutputStream(new FileOutputStream(target));
                    int read = 0;
                    byte[] buffer = new byte[1024 * 10];
                    while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, read);
                    }
                    bos.flush();
                }
                zis.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtil.closeQuietly(zis, bos);

            }
        }
    }





    public static boolean  downloadFile(String downFilePath, String filePath) {
        //此方法只能用户HTTP协议
        //输入压缩zip的流。
        ZipInputStream zis = null;
        //输出文件zip的流
        BufferedOutputStream bos = null;
        try { //解压输出的输出流
            URL url = new URL(downFilePath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            zis = new ZipInputStream(connection.getInputStream(), Charset.forName("gbk"));
            ZipEntry entry = null;
            //这个是把压缩文件中的文件依次解压
            while ((entry = zis.getNextEntry()) != null&&!entry.isDirectory()) {
                File target = new File(filePath, entry.getName());
                if (!target.getParentFile().exists()) {
                    //假如没有文件路径创建文件父目录
                    target.getParentFile().mkdirs();
                }
                // 写入文件
                bos = new BufferedOutputStream(new FileOutputStream(target));
                int read = 0;
                //限制的是十兆的下载文件。
                byte[] buffer = new byte[1024 * 10];
                while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                    bos.write(buffer, 0, read);
                }
                bos.flush();
                //以下是获取中间的csv
                ArrayList<String[]> csvList = new ArrayList<String[]>(); //用来保存数据
                String csvFilePath = target.getPath();
                if (csvFilePath.contains("汇总")) {
                    CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("gbk"));    //一般用这编码读就可以了

                    reader.readHeaders(); // 跳过表头   如果需要表头的话，不要写这句。

                    while (reader.readRecord()) { //逐行读入除表头的数据
                        System.out.print(reader.getValues().toString());
                        csvList.add(reader.getValues());

                    }
                    reader.close();

                    List<String> list = new ArrayList<>();
                    for (int row = 0; row < csvList.size(); row++) {
                        String lie = "";
                        for (int i = 0; i < csvList.get(row).length; i++) {
                            String cell = csvList.get(row)[i];//取得第row行第0列的数据
                            lie =lie+ cell + ",";
                            System.out.println(lie);
                        }
                        if (!lie.contains("#")) {
                            if(!lie.contains("合计"))
                            list.add(lie.substring(0,lie.length()-1));
                        }

                    }

                    System.out.print(list.toString());
                    FileUtils.writeLines(new File("/data/dataspider/InterfaceAPI/" + "支付宝交易账单"+PayForUtil.getFormatterDate(new Date())+ ".csv"), "UTF-8", list);
                }
            }
            zis.closeEntry();


        } catch (IOException e) {
            throw new RuntimeException(e);

        } finally {
            //关闭输出和输入文件流。
            IOUtil.closeQuietly(zis, bos);

        }

        return true;
    }


}

