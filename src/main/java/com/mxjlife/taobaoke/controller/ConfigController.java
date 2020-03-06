package com.mxjlife.taobaoke.controller;

import com.mxjlife.taobaoke.pojo.base.BaseQueryParams;
import com.mxjlife.taobaoke.pojo.base.ConfigInfoPO;
import com.mxjlife.taobaoke.pojo.base.PageInfo;
import com.mxjlife.taobaoke.pojo.base.ResponseVO;
import com.mxjlife.taobaoke.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * description: 配置接口
 * author mxj
 * email mengxiangjie@hualala.com
 * date 2019/8/15 16:14
 */
@Profile({"dev","test","prod"})
@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/sys/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseVO<ConfigInfoPO> get(@PathVariable("id") Integer id){
        ConfigInfoPO config = configService.getConfigById(id);
        return ResponseVO.getSuccess(config);
    }

    @GetMapping(value = "/p", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseVO<PageInfo<ConfigInfoPO>> getPage(@RequestBody BaseQueryParams params){
        PageInfo<ConfigInfoPO> configs = configService.getConfigs(params);
        return ResponseVO.getSuccess(configs);
    }

    @PutMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseVO add(@RequestBody ConfigInfoPO config){
        int i = configService.insertConfig(config);
        return ResponseVO.getSuccess();
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseVO update(@RequestBody ConfigInfoPO config){
        int i = configService.updateConfig(config);
        return ResponseVO.getSuccess();
    }

    @DeleteMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseVO delete(@PathVariable("id") Integer id){
        int i = configService.deleteConfig(id);
        return ResponseVO.getSuccess();
    }
}

