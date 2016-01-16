package com.devoxx.proxy.service

import com.devoxx.proxy.cfp.CfpConference
import com.devoxx.proxy.cfp.CfpLinks
import com.devoxx.proxy.cfp.CfpSchedule
import com.devoxx.proxy.cfp.CfpSpeaker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * Created by sarbogast on 16/01/2016.
 */
@Service
class CfpClientService {
    private RestTemplate template = new RestTemplate()

    CfpLinks getConferences(String url) {
        return template.getForObject(url, CfpLinks.class)
    }

    CfpConference getConference(String url) {
        return template.getForObject(url, CfpConference.class)
    }

    CfpLinks getSchedules(String url) {
        return template.getForObject(url, CfpLinks.class)
    }

    CfpSchedule getSchedule(String url) {
        return template.getForObject(url, CfpSchedule.class)
    }

    CfpSpeaker getSpeaker(String url) {
        return template.getForObject(url, CfpSpeaker.class)
    }
}
