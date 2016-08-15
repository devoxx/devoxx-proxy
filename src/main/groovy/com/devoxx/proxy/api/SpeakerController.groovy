package com.devoxx.proxy.api

import com.devoxx.proxy.api.dto.SpeakerDetail
import com.devoxx.proxy.api.dto.SpeakerListItem
import com.devoxx.proxy.api.exception.ResourceNotFoundException
import com.devoxx.proxy.service.SpeakerService
import org.omg.CORBA.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
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
        return speakerService.getSpeakers(withVideo).content
    }

    @RequestMapping(value="/paged/speakers", method= RequestMethod.GET)
    Page<SpeakerListItem> indexPaged(@RequestParam(name="withVideo", required = false, defaultValue = "false")boolean withVideo, Pageable pageRequest) {
        return speakerService.getSpeakers(withVideo, pageRequest)
    }

    @RequestMapping(value = "/speakers/{uuid}", method = RequestMethod.GET)
    SpeakerDetail get(@PathVariable("uuid") String uuid) {
        SpeakerDetail speaker = speakerService.findSpeakerByUuid(uuid)
        if(speaker == null) throw new ResourceNotFoundException(uuid)
        else return speaker
    }
}
