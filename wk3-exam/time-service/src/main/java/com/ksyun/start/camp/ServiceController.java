package com.ksyun.start.camp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ksyun.start.camp.constant.Constant;
import com.ksyun.start.camp.dto.RequestDTO;
import com.ksyun.start.camp.dto.ResponseDTO;
import com.ksyun.start.camp.service.SimpleTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ServiceController {

    @Autowired
    private SimpleTimeService simpleTimeService;

    // 在此实现简单时间服务的接口逻辑
    // 1. 调用 SimpleTimeService
    @RequestMapping(value = "/getDateTime", method = RequestMethod.GET,produces = "application/json")
    public JSONObject getDateTime(@RequestParam("style") String style) {
        String dateTime = simpleTimeService.getDateTime(style);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setServiceId(Constant.serviceId);
        responseDTO.setResult(dateTime);
        String s = JSON.toJSONString(responseDTO);
        JSONObject jsonObject = JSON.parseObject(s);
        return jsonObject;
    }
}
