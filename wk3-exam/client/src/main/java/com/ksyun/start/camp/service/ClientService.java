package com.ksyun.start.camp.service;

import com.ksyun.start.camp.dto.ResponseDTO;

public interface ClientService {

    /**
     * 向调用方返回信息，此部分信息组合了 TimeService 的返回的时间
     *
     * @return 相关信息
     */
    ResponseDTO getInfo();

}
