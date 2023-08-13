package com.ksyun.start.camp.service;

import com.ksyun.start.camp.dto.ApiResponse;
import com.ksyun.start.camp.dto.RequestDTO;

import java.util.List;
import java.util.Map;

public interface RegistryService {

    public ApiResponse register(RequestDTO requestDTO, Map<String, List<RequestDTO>> serviceMap);

    public ApiResponse hearBeat(RequestDTO requestDTO, Map<String, List<RequestDTO>> serviceMap);

    public ApiResponse unRegister(RequestDTO requestDTO, Map<String, List<RequestDTO>> serviceMap);

    public List<RequestDTO> discovery(String  serviceName, Map<String, List<RequestDTO>> serviceMap);
}
