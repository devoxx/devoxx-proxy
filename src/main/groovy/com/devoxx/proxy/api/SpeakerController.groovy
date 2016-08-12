package com.devoxx.proxy.api

import com.devoxx.proxy.api.dto.SpeakerDetail
import com.devoxx.proxy.api.dto.SpeakerListItem
import com.devoxx.proxy.api.exception.ResourceNotFoundException
import com.devoxx.proxy.service.SpeakerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Created by sarbogast on 16/01/2016.
 */
@RestController
@RequestMapping("/api")
class SpeakerController {
    @Autowired
    SpeakerService speakerService

    @RequestMapping(value="/speakers", method = RequestMethod.GET)
    List<SpeakerListItem> index(@RequestParam(name = "withVideo", required = false, defaultValue = "false")boolean withVideo) {
        return speakerService.getSpeakers(withVideo)
    }

    @RequestMapping(value = "/speakers/{uuid}", method = RequestMethod.GET)
    SpeakerDetail get(@PathVariable("uuid") String uuid) {
        SpeakerDetail speaker = speakerService.findSpeakerByUuid(uuid)
        if(speaker == null) throw new ResourceNotFoundException(uuid)
        else return speaker
    }
}
