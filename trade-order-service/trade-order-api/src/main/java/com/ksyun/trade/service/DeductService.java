package com.ksyun.trade.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ksyun.trade.dto.ConfigsDTO;
import com.ksyun.trade.dto.DeductDTO;
import com.ksyun.trade.dto.RegionDTO;
import com.ksyun.trade.entity.KscTradeOrder;
import com.ksyun.trade.entity.KscVoucherDeduct;
import com.ksyun.trade.mapper.KscTradeProductConfigMapper;
import com.ksyun.trade.mapper.KscVoucherDeductMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DeductService {

    @Autowired
    private KscTradeOrderService kscTradeOrderService;

    @Autowired
    private KscVoucherDeductService kscVoucherDeductService;


    @Transactional
    public Object deduct(String orderId, String voucherNo, String amount) {
        DeductDTO deductDTO = new DeductDTO();// 返回的封装对象
        KscVoucherDeduct kscVoucherDeduct = new KscVoucherDeduct(); // 实体类对象
        Object o = null;
        BigDecimal afterDeductAmount=new BigDecimal("0"); // 抵扣后的金额
        // 先看一下该优惠卷是否用过,用过了则返回提醒
        LambdaQueryWrapper<KscVoucherDeduct> kscVoucherDeductLambdaQueryWrapper = new LambdaQueryWrapper<>();
        kscVoucherDeductLambdaQueryWrapper.eq(KscVoucherDeduct::getVoucherNo, voucherNo);
        int isUsed = kscVoucherDeductService.count(kscVoucherDeductLambdaQueryWrapper);
        if (isUsed > 0) {
            log.info("该优惠卷已使用过，无法再用");
            deductDTO.setCode(200);
            deductDTO.setMsg("该优惠卷已使用过，无法再用");
            String requestId = MDC.get("traceId");
            deductDTO.setRequestId(requestId);
            o = JSONObject.toJSON(deductDTO);
            return o;
        }
        // 先判断是否存在该订单号对应的数据条数，如果存在数据，则取出最新的一条数据进行处理
        LambdaQueryWrapper<KscVoucherDeduct> kscVoucherDeductLambdaQueryWrapperCount = new LambdaQueryWrapper<>();
        kscVoucherDeductLambdaQueryWrapperCount.eq(KscVoucherDeduct::getOrderId, Integer.valueOf(orderId));
        int count = kscVoucherDeductService.count(kscVoucherDeductLambdaQueryWrapperCount);
        if (count > 0) { // 存在对应的数据，不是第一次插入的
            // 得到该订单对应的最新一条数据
            List<KscVoucherDeduct> kscVoucherDeductList = kscVoucherDeductService.list(new QueryWrapper<KscVoucherDeduct>().orderByDesc("id").last("limit 1"));
            KscVoucherDeduct oldKscVoucherDeduct = kscVoucherDeductList.get(0);
            kscVoucherDeduct.setOrderId(Integer.valueOf(orderId));
            kscVoucherDeduct.setVoucherNo(voucherNo);
            kscVoucherDeduct.setAmount(new BigDecimal(amount));
            // 价格需要判断是否小于0,小于0则总价为0
            BigDecimal deductAmount = oldKscVoucherDeduct.getAfterDeductAmount();
            afterDeductAmount = deductAmount.subtract(new BigDecimal(amount));
            if (afterDeductAmount.compareTo(BigDecimal.ZERO) <= 0) {
                afterDeductAmount = BigDecimal.ZERO;
            }
            kscVoucherDeduct.setBeforeDeductAmount(deductAmount);
            kscVoucherDeduct.setAfterDeductAmount(afterDeductAmount);
        }else{// 不存在对应的数据，是第一次插入的
            // 根据id查出对应的原始金额，插入对应的数据
            LambdaQueryWrapper<KscTradeOrder> kscTradeOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
            kscTradeOrderLambdaQueryWrapper.eq(KscTradeOrder::getId, Integer.valueOf(orderId));
            KscTradeOrder tradeOrder = kscTradeOrderService.getOne(kscTradeOrderLambdaQueryWrapper);
            BigDecimal priceValue = tradeOrder.getPriceValue();
            // 插入新数据
            kscVoucherDeduct.setOrderId(Integer.valueOf(orderId));
            kscVoucherDeduct.setVoucherNo(voucherNo);
            kscVoucherDeduct.setAmount(new BigDecimal(amount));
            kscVoucherDeduct.setBeforeDeductAmount(priceValue);
            // 价格需要判断是否小于0,小于0则总价为0
            afterDeductAmount = priceValue.subtract(new BigDecimal(amount));
            if (afterDeductAmount.compareTo(BigDecimal.ZERO) <= 0) {
                afterDeductAmount = BigDecimal.ZERO;
            }
            kscVoucherDeduct.setAfterDeductAmount(afterDeductAmount);
        }

        // 开始插入数据
        boolean b = kscVoucherDeductService.saveOrUpdate(kscVoucherDeduct);
        // 如果成功返回200
        if(b){
            deductDTO.setCode(200);
            if (afterDeductAmount.compareTo(BigDecimal.ZERO) == 0) {
                deductDTO.setMsg("优惠卷金额大于或等于原始金额，该商品已免费");
            } else {
                deductDTO.setMsg("ok");
            }
            String requestId = MDC.get("traceId");
            deductDTO.setRequestId(requestId);
            o = JSONObject.toJSON(deductDTO);
        }else{
            deductDTO.setCode(500);
            deductDTO.setMsg("优惠失败，请稍后重试");
            String requestId = MDC.get("traceId");
            deductDTO.setRequestId(requestId);
            o = JSONObject.toJSON(deductDTO);
        }
        return o;
    }
}
