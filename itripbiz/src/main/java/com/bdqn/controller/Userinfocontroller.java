package com.bdqn.controller;

import cn.itrip.common.DtoUtil;
import cn.itrip.common.RedisUtil;
import cn.itrip.dao.itripUserLinkUser.ItripUserLinkUserMapper;
import cn.itrip.pojo.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class Userinfocontroller {

    @Resource
    ItripUserLinkUserMapper dao;
    @Resource
    RedisUtil jedis;

    //查询
    @RequestMapping(value="/api/userinfo/queryuserlinkuser",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object selectUserLinkUser(@RequestBody ItripSearchUserLinkUserVO vo,HttpServletRequest request) throws Exception {
        String token=request.getHeader("token");
        ItripUser u=(ItripUser) JSONArray.parseObject(jedis.getstr(token),ItripUser.class);
        String linkUserName=vo==null?null:vo.getLinkUserName();
        List<ItripUserLinkUser> list=dao.selectUserLinkUser(u.getId(),"%"+linkUserName+"%");
        return DtoUtil.returnDataSuccess(list);
    }

    //新增
    @RequestMapping(value="/api/userinfo/adduserlinkuser",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object insertUserLinkUser(@RequestBody ItripAddUserLinkUserVO itripAddUserLinkUserVO, HttpServletRequest request) throws Exception {
        String token=request.getHeader("token");
        ItripUser u=(ItripUser) JSONArray.parseObject(jedis.getstr(token),ItripUser.class);
        ItripUserLinkUser itripUserLinkUser=new ItripUserLinkUser();
        itripUserLinkUser.setLinkUserName(itripAddUserLinkUserVO.getLinkUserName());
        itripUserLinkUser.setLinkIdCard(itripAddUserLinkUserVO.getLinkIdCard());
        itripUserLinkUser.setLinkPhone(itripAddUserLinkUserVO.getLinkPhone());
        itripUserLinkUser.setUserId(u.getId());
        int num= dao.insertUserLinkUser(itripUserLinkUser);
        if (num>0){
        return DtoUtil.returnSuccess();
        }
        return DtoUtil.returnFail("新增失败","10000");
    }

    //删除
    @RequestMapping(value="/api/userinfo/deluserlinkuser",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object deleteUserLinkUser(Integer[] ids) throws Exception {
        int num= dao.deleteUserLinkUser(ids);
        if (num>0){
            return DtoUtil.returnSuccess();
        }
        return DtoUtil.returnFail("删除失败","10000");
    }

    //修改
    @RequestMapping(value="/api/userinfo/modifyuserlinkuser",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object updateUserLinkUser(@RequestBody ItripModifyUserLinkUserVO VO) throws Exception {
        ItripUserLinkUser itripUserLinkUser=new ItripUserLinkUser();
        itripUserLinkUser.setLinkUserName(VO.getLinkUserName());
        itripUserLinkUser.setLinkIdCard(VO.getLinkIdCard());
        itripUserLinkUser.setLinkPhone(VO.getLinkPhone());
        itripUserLinkUser.setId(VO.getId());
        int num= dao.updateUserLinkUser(itripUserLinkUser);
        if (num>0){
            return DtoUtil.returnSuccess();
        }
        return DtoUtil.returnFail("修改失败","100421");
    }


}
