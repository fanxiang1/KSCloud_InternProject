package com.ksyun.start.camp;

import com.alibaba.fastjson.JSON;
import com.ksyun.start.camp.dto.ResponseDTO;
import com.ksyun.start.camp.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 默认的客户端 API Controller
 */
@RestController()
@RequestMapping("/api")
public class ClientController {

    // 在这里开始编写你的相关接口实现代码
    // 返回值对象使用 ApiResponse 类

    @Autowired
    private ClientService clientService;


    // 提示：调用 ClientService
    @RequestMapping(value = "/getInfo", method = RequestMethod.GET,produces = "application/json")
    public Object discovery() {
        ResponseDTO info = clientService.getInfo();

        return info;
    }

}
