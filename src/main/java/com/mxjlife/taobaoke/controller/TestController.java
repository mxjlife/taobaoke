package com.mxjlife.taobaoke.controller;

import com.mxjlife.taobaoke.pojo.base.ResponseVO;
import com.mxjlife.taobaoke.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * description: 测试接口
 * author mxj
 * email mengxiangjie@hualala.com
 * date 2019/8/15 16:14
 */
@Profile({"dev","test"})
@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ConfigService configService;


    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseVO test(@RequestParam("id") Integer id, @RequestParam("action") Integer action){
//    public ResponseVO test(@RequestBody(required = false)  @Validated PageInfo<ConfigInfo> pageInfo){
//        if(pageInfo == null){
//            pageInfo = new PageInfo<>();
//        }
//        GuavaCache.get(pageInfo.getKeyWord());
//        int count = configMapper.getCountByPage(pageInfo);
//        pageInfo.setTotalCount(count);
//        List<ConfigInfo> dataByPage = configMapper.getDataByPage(pageInfo);
//        pageInfo.setDataList(dataByPage);
//        log.info("res ->{}", pageInfo);

        return ResponseVO.getSuccess();
    }
}

