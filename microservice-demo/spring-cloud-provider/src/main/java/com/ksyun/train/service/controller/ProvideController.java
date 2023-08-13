package com.ksyun.train.service.controller;

import com.ksyun.train.service.dto.TimeDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;

@RestController
public class ProvideController {
    // 返回形如yyyy-MM-dd HH:mm:ss的json字符串数据，表示今天的日期
    @RequestMapping(value = "/api/getDateTime",method = RequestMethod.GET, produces = "application/json")
    public Object getDateTime() {
        long timestamp = System.currentTimeMillis();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
        TimeDTO timeDTO = new TimeDTO();
        timeDTO.setDate(date);
        timeDTO.setTimetamp(timestamp);
        return timeDTO;
    }
}
