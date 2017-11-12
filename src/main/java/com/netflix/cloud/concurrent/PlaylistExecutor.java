package com.netflix.cloud.concurrent;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class PlaylistExecutor {

    @Value("${thread.core-pool}")
    private int corePoolSize;

    @Value("${thread.max-pool}")
    private int maxPoolSize;

    @Value("${queue.capacity}")
    private int queueCapacity;

    @Value("${thread.timeout}")
    private int threadTimeout;

    @Bean
    @Qualifier("playlistExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setKeepAliveSeconds(threadTimeout);

        return threadPoolTaskExecutor;
    }
}
