package com.example.slstudiomini.batch;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Scheduled(fixedRate = 5000) // 5秒ごとに実行
    public void reportCurrentTime() {
        // ここに実行したい処理を書きます
        System.out.println("現在時刻: " + System.currentTimeMillis());
    }
}
