package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Random;
import java.util.UUID;

/**
 * @author tao
 * @date 2022/3/22
 */
public class Tools {
    /**
     * 同一文件夹下文件名随机返回工具
     * @param Filepath 文件夹绝对路径
     * @return 文件夹下随机文件名
     */
    public String randomFilename(String Filepath){
        int fileCount = 0;//文件数
        int folderCount = 0;//文件夹数
        File d = new File(Filepath);
        File list[] = d.listFiles();
        for(int i = 0; i < list.length; i++){
            if(list[i].isFile()){
                fileCount++;
            }else{
                folderCount++;
            }
        }
        int s = (new Random().nextInt(fileCount))+1;
        int fileCounts = 0;
        for(int i = 0; i < list.length; i++){
            if(list[i].isFile()){
                fileCounts++;
                if (fileCounts==s){
                    File tmpFile=new File(String.valueOf(list[i]));
                    String fileName=tmpFile.getName();
                    System.out.println(fileName);
                    return fileName;
                }
            }
        }
        return null;
    }

    /**
     * 同一文件夹下文件路径随机返回工具
     * @param Filepath 文件夹绝对路径
     * @return 文件夹下随机文件绝对路径
     */
    public String randomFileAbsolutePath(String Filepath){
        System.out.println(Filepath);
        int fileCount = 0;//文件数
        int folderCount = 0;//文件夹数
        File d = new File(Filepath);
        File list[] = d.listFiles();
        for(int i = 0; i < list.length; i++){
            if(list[i].isFile()){
                fileCount++;
            }else{
                folderCount++;
            }
        }
        int s = (new Random().nextInt(fileCount))+1;
        System.out.println("随机数:"+s);
        int fileCounts = 0;
        for(int i = 0; i < list.length; i++){
            if(list[i].isFile()){
                fileCounts++;
                if (fileCounts==s){
                    File tmpFile=new File(String.valueOf(list[i]));
                    String fileAbsolutePath=tmpFile.getAbsolutePath();
                    System.out.println(fileAbsolutePath);
                    return fileAbsolutePath;
                }
            }
        }
        return null;
    }

    /**
     * Json文件读取
     * @param fileName 文件绝对路径
     * @return Json格式字符串（String）
     */
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 下载（客户端）文件
     * @param response 响应体（位配置响应头）
     * @param filePath 文件绝对路径
     * @throws IOException
     */
    public static void responseFile(HttpServletResponse response,String filePath) throws IOException {
        //1、设置response 响应头
        response.reset(); //设置页面不缓存,清空buffer
        response.setCharacterEncoding("UTF-8"); //字符编码
        //2、 读取文件--输入流
        InputStream input=new FileInputStream(filePath);
        //3、 写出文件--输出流
        OutputStream out = response.getOutputStream();

        byte[] buff =new byte[1024];
        int index;
        //4、执行 写出操作
        while((index= input.read(buff))!= -1){
            out.write(buff, 0, index);
            out.flush();
        }
        out.close();
        input.close();
    }


    /**
     * 随机生成文件名
     * @param fileName
     * @return java.lang.String
     */
    public static String getRandomName(String fileName){
        int index=fileName.lastIndexOf(".");
        String houzhui=fileName.substring(index);//获取后缀名
        String uuidFileName= UUID.randomUUID().toString().replace("-","")+houzhui;
        return uuidFileName;
    }



}
