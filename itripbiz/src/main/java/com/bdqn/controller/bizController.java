package com.bdqn.controller;


import cn.itrip.common.DtoUtil;
import cn.itrip.dao.itripAreaDic.ItripAreaDicMapper;
import cn.itrip.dao.itripLabelDic.ItripLabelDicMapper;
import cn.itrip.pojo.ItripAreaDic;
import cn.itrip.pojo.ItripLabelDic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class bizController {

    @Resource
    ItripAreaDicMapper dao;
    @Resource
    ItripLabelDicMapper dao1;

    //热门城市
    @RequestMapping(value="/api/hotel/queryhotcity/{type}",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object login(@PathVariable("type")int type) throws Exception {
        System.out.println(type);
        List<ItripAreaDic> list=dao.selectIsChina(type);
        return DtoUtil.returnDataSuccess(list);
    }
    // 查询酒店特色列表
    @RequestMapping(value="/api/hotel/queryhotelfeature",produces="application/json; charset=utf-8")
    @ResponseBody
    public Object selectfeature() throws Exception {
      List<ItripLabelDic> list=dao1.selectfeature();
        return DtoUtil.returnDataSuccess(list);
    }

}
