package com.bdqn.controller;

import cn.itrip.common.DtoUtil;
import cn.itrip.common.ItripTokenVO;
import cn.itrip.common.RedisUtil;
import cn.itrip.common.TokenBiz;
import cn.itrip.dao.itripHotel.ItripHotelMapper;
import cn.itrip.dao.itripUser.ItripUserMapper;
import cn.itrip.pojo.ItripHotel;
import cn.itrip.pojo.ItripUser;
import cn.itrip.pojo.ItripUserVO;
import com.alibaba.fastjson.JSONArray;

import com.alibaba.fastjson.JSONObject;
import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.apache.ibatis.javassist.compiler.TokenId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller

public class itripController {

        @Resource
        ItripHotelMapper dao;
        @Resource
         ItripUserMapper dao1;
        @Resource
         TokenBiz biz;
        @Resource
        RedisUtil redisUtil;

   /* @RequestMapping("li")
    public String  list(HttpServletRequest request,String index){
        int indexn=index==""?1:new Integer(index);
        List<ItripHotel> list= dao.list(indexn);
        int num=dao.count();
        request.setAttribute("l",list);
        request.setAttribute("num",num%8==0?num/8:num/8+1);
        request.setAttribute("index",indexn);
        return "list";
    }*/
    //无语了
   @RequestMapping(value="/api/dologin",produces="application/json; charset=utf-8")
   @ResponseBody
   public Object login(String name,String password,HttpServletRequest request) throws Exception {
       Map b=new HashMap();
       b.put("a",name);
       b.put("b",password);
       ItripUser user=dao1.getItripUserListByMap(b);
       if (user!=null){
           //模拟session的票据-------
           String token=biz.generateToken(request.getHeader("User-Agent"),user);
           //把这个token存储到redis中
           //用fastjson把当前用户转换成字符串类型
           redisUtil.setRedis(token,JSONArray.toJSONString(user));
           ItripTokenVO obj=new ItripTokenVO(token, Calendar.getInstance().getTimeInMillis()*3600*2,Calendar.getInstance().getTimeInMillis());
           return DtoUtil.returnDataSuccess(obj);
       }
      /* return JSONArray.toJSONString(user);*/
       return DtoUtil.returnFail("登录失败","1000");
   }

    @RequestMapping(value="/api/registerbyphone",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object Register(@RequestBody ItripUserVO vo) throws Exception {
    ItripUser itripUser=new ItripUser();
    itripUser.setUserCode(vo.getUserCode());
    itripUser.setUserPassword(vo.getUserPassword());
    itripUser.setUserName(vo.getUserName());
    dao1.insertUser(itripUser);
    Random random=new Random(4);
    int sj=random.nextInt(9999);
    sentSms(vo.getUserCode(),sj+"");
    redisUtil.setRedis(vo.getUserCode(),sj+"");
    return DtoUtil.returnSuccess();
    }

    @RequestMapping(value="/api/validatephone",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object Activate(String user,String code,HttpServletRequest request) throws Exception {
       String s=redisUtil.getstr(user);
       if (s.equals(code)){
           dao1.updateUser(user);
           return DtoUtil.returnSuccess();
       }
           return DtoUtil.returnFail("失败","10000");

    }

    @RequestMapping(value="/api/ckusr",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object Ckusr(String name) throws Exception {

       ItripUser user=dao1.selectUserName(name);
       if (user!=null){
       return DtoUtil.returnFail("没有","10000");
       }
       else{
           return DtoUtil.returnSuccess();
       }
    }

    @RequestMapping(value="/api/doregister",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object Doregister(@RequestBody ItripUserVO vo) throws Exception {
        ItripUser itripUser=new ItripUser();
        itripUser.setUserCode(vo.getUserCode());
        itripUser.setUserPassword(vo.getUserPassword());
        itripUser.setUserName(vo.getUserName());
        int num=dao1.insertUser(itripUser);
        Random random=new Random(4);
        int sj=random.nextInt(9999);
        Mail(vo.getUserCode(),sj+"");
        redisUtil.setRedis(vo.getUserCode(),sj+"");
        if (num>0){
            return DtoUtil.returnSuccess();
        }
        return DtoUtil.returnFail("失败","10000");
    }
    @RequestMapping(value="/api/activate",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object Activate(String user,String code) throws Exception {
        String s=redisUtil.getstr(user);
        if (s.equals(code)){
            dao1.updateUser(user);
            return DtoUtil.returnSuccess();
        }

        return DtoUtil.returnFail("失败","10000");
    }



   public static void sentSms(String phone,String message){
       //生产环境请求地址：app.cloopen.com
       String serverIp = "app.cloopen.com";
       //请求端口
       String serverPort = "8883";
       //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
       String accountSId = "8a216da87f63aaf1017f6c33f2f90197";
       String accountToken = "b8afaca39d6b4b0f91e6cf54593a27d9";
       //请使用管理控制台中已创建应用的APPID
       String appId = "8a216da87f63aaf1017f6c33f48f019d";
       CCPRestSmsSDK sdk = new CCPRestSmsSDK();
       sdk.init(serverIp, serverPort);
       sdk.setAccount(accountSId, accountToken);
       sdk.setAppId(appId);
       sdk.setBodyType(BodyType.Type_JSON);
       String to = phone;
       String templateId= "1";
       String[] datas = {message};
       //String subAppend="1234";  //可选	扩展码，四位数字 0~9999
       //String reqId="fadfafas";  //可选 第三方自定义消息id，最大支持32位英文数字，同账号下同一自然天内不允许重复
       HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas);
       // HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas);
       if("000000".equals(result.get("statusCode"))){
           //正常返回输出data包体信息（map）
           HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
           Set<String> keySet = data.keySet();
           for(String key:keySet){
               Object object = data.get(key);
               System.out.println(key +" = "+object);
           }
       }else{
           //异常返回输出错误码和错误信息
           System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
       }
   }
   public static void Mail(String Muser,String message) throws MessagingException, UnsupportedEncodingException {
       Properties props = new Properties();
       //设置邮件地址
       props.put("mail.smtp.host", "smtp.126.com");
       //开启认证
       props.put("mail.smtp.auth", "true");
       Session session = Session.getDefaultInstance(props, null);
       Transport transport = session.getTransport();
       //用户名
       String user = "wuzhang0310@126.com";
       //授权码
       String password = "WOKKJMHMHKLBTFCL";
       transport.connect(user, password);
       //创建邮件消息
       MimeMessage msg = new MimeMessage(session);
       msg.setSentDate(new Date());
       //邮件发送人
       InternetAddress fromAddress = new InternetAddress(user, "邮件服务");
       msg.setFrom(fromAddress);
       //邮件接收人
       String to = Muser;
       InternetAddress[] toAddress = new InternetAddress[]{new InternetAddress(to)};
       msg.setRecipients(Message.RecipientType.TO, toAddress);
       //邮件主题
       msg.setSubject("爱旅行", "UTF-8");
       //邮件内容和格式
       msg.setContent(message, "text/html;charset=UTF-8");
       msg.saveChanges();
       //发送
       transport.sendMessage(msg, msg.getAllRecipients());
   }



    @RequestMapping(value="clist",produces="application/json; charset=utf-8")
    @ResponseBody
    public String glist() throws Exception {
        ItripHotel list=dao.getItripHotelById(new Long(56));
        return JSONArray.toJSONString(list);
    }



    @RequestMapping("clist1")
    public String  clist(){

        return "clist1";
    }


}
