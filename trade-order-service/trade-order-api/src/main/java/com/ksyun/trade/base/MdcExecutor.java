package com.ksyun.trade.base;

import org.slf4j.MDC;

import java.util.concurrent.Executor;

public class MdcExecutor implements Executor {
    private Executor executor;
    public MdcExecutor(Executor executor) {
        this.executor = executor;
    }
    @Override
    public void execute(Runnable command) {
        final String requestId = MDC.get("traceId");
        executor.execute(() -> {
            MDC.put("traceId", requestId);
            try {
                command.run();
            } finally {
                MDC.remove("traceId");
            }
        });
    }
}
