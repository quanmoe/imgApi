package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author tao
 * @date 2022/3/29
 */
@Controller
public class ImgApi {
    @RequestMapping(value = "/index")
    public String index(){
        return "imgApi";
    }

    @RequestMapping(value = "/img")
    public String def(Model model){
        model.addAttribute("type","moe");
        return "redirect:/img";
    }

    @RequestMapping(value = {"/img"},params = {"type"})
    public void img(String type,HttpServletResponse response) throws Exception{
        String jsonPath = this.getClass().getResource("/main.json").getPath();//加载main.json
        JSONObject jsonObject = JSON.parseObject(Tools.readJsonFile(jsonPath));//将main.json读入
        JSONArray typeNames = jsonObject.getJSONObject("config").getJSONArray("typeName");//读取json的config.typeName
        boolean isType = false;
        for (int i = 0; i < typeNames.size(); i++) {
            if (type.equals(typeNames.getString(i))){
                isType=true;
                break;
            }
        }//判断参数是否为typeName
        if (!isType){
            type=typeNames.getString(0);
        }//不属于则使用typeName[0]
        System.out.println(type);
        String imgPath = jsonObject.getJSONObject("config").getJSONObject("imgPath").getString(type);//读路径，将对应的config.type读出
        String path = new Tools().randomFileAbsolutePath(imgPath);//返回图片绝对路径
        //responseFile,使用前设置响应头
        response.setHeader("Content-type","image/jpeg");
        Tools.responseFile(response,path);
    }

    @RequestMapping(value = "/img",params = {"type=moeR18","R18=true"})
    public void imgR(String type,Boolean R18,HttpServletResponse response) throws IOException {
        String jsonPath = this.getClass().getResource("/main.json").getPath();//加载main.json
        JSONObject jsonObject = JSON.parseObject(Tools.readJsonFile(jsonPath));//将main.json读入
        JSONArray typeNames = jsonObject.getJSONObject("config").getJSONArray("typeName");//读取json的config.typeName
        for (int i = 0; i < typeNames.size(); i++) {
            if (typeNames.getString(i).equals(type)){
                if (R18){
                    String imgPath = jsonObject.getJSONObject("config").getJSONObject("R18Path").getString(type);//读路径，将对应的config.type读出
                    String path = new Tools().randomFileAbsolutePath(imgPath);//返回图片绝对路径
                    //responseFile,使用前设置响应头
                    response.setHeader("Content-type","image/jpeg");
                    Tools.responseFile(response,path);
                }
            }
        }
    }

    @RequestMapping(value = "/img/upload")
    public String imgForm(){
        return "imgUpload";
    }

    @RequestMapping(value = "/img/upload",params = {"token","type"})
    public String imgUpload(String token, String type, @RequestParam("file") CommonsMultipartFile file, HttpServletRequest request){

        //获取文件名 : file.getOriginalFilename();
        String uploadFileName = file.getOriginalFilename();
        //生成随机文件名
        uploadFileName = Tools.getRandomName(uploadFileName);
        //如果文件名为空，直接回到首页！
        System.out.println("上传文件名 : "+uploadFileName);
        //上传路径保存设置
        String jsonPath = this.getClass().getResource("/main.json").getPath();//加载main.json
        JSONObject jsonObject = JSON.parseObject(Tools.readJsonFile(jsonPath));//将main.json读入
        JSONObject config = jsonObject.getJSONObject("config");//读取json的config
        String path = null;
        if (token.equals(config.getString("token"))){
            if (type.equals("moeR18")){
                path = config.getString("R18Path");
            }
            else {
                path = config.getJSONObject("imgPath").getString(type);
            }
        }
        System.out.println(path);
        //如果路径不存在，创建一个
        File realPath = new File(path);
        if (!realPath.exists()){
            realPath.mkdir();
        }
        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        try {
            file.transferTo(new File(realPath +"/"+ uploadFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/index";
    }
}
