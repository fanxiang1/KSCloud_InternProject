package com.ksyun.trade.controller.online;

import com.ksyun.trade.cache.TwoLevelCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;


@RestController
public class CacheController {
    @Autowired
    private TwoLevelCache twoLevelCache;

    @GetMapping("/cache/put")
    public Object put(@RequestParam(value = "key") String key,
                         @RequestParam(value = "value") String value) {
        twoLevelCache.put(key,value);
        return "数据放入成功";
    }

    @GetMapping("/cache/get")
    public Object getKey(@RequestParam(value = "key") String key) throws ExecutionException {
        Object o = twoLevelCache.get(key);
        return o;

    }
}
