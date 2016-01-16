package com.devoxx.proxy.scheduled

import com.devoxx.proxy.service.CfpUpdateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Created by sarbogast on 15/01/2016.
 */
@Component
class Batches {
    @Autowired CfpUpdateService cfpUpdateService

    @Scheduled(cron = "0 2 * * * ?")
    void updateCfpData() {
        cfpUpdateService.updateData()
    }
}
