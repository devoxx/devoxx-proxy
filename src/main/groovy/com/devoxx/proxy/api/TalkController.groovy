package com.devoxx.proxy.api

import com.devoxx.proxy.api.dto.TalkDetail
import com.devoxx.proxy.api.dto.TalkListItem
import com.devoxx.proxy.api.exception.ResourceNotFoundException
import com.devoxx.proxy.service.TalkService
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
@RequestMapping('/api')
class TalkController {
    @Autowired
    TalkService talkService

    @RequestMapping(value = "/talks", method = RequestMethod.GET)
    List<TalkListItem> index(@RequestParam(name = "withVideo", required = false, defaultValue = "false")boolean withVideo) {
        return talkService.findAllTalksWithVideo(withVideo).content
    }

    @RequestMapping(value = "/paged/talks", method = RequestMethod.GET)
    Page<TalkListItem> indexPaged(@RequestParam(name = "withVideo", required = false, defaultValue = "false")boolean withVideo, Pageable pageRequest) {
        return talkService.findAllTalksWithVideo(withVideo, pageRequest)
    }

    @RequestMapping(value = "/talks/{talkId}", method = RequestMethod.GET)
    TalkDetail get(@PathVariable("talkId") String talkId) {
        TalkDetail talk = talkService.findTalkByTalkId(talkId)
        if(talk == null) throw new ResourceNotFoundException(talkId)
        else return talk
    }

    @RequestMapping(value = "/talks/top", method = RequestMethod.GET)
    List<TalkListItem> getTopTalks(@RequestParam(name = "withVideo", required = false, defaultValue = "false")boolean withVideo,
                                   @RequestParam(name="count", required = false, defaultValue = "10") int count){
        return talkService.getTopTalks(withVideo, count)
    }

    @RequestMapping(value = "/talks/search", method = RequestMethod.GET)
    List<TalkListItem> searchTalks(@RequestParam(name = "q", required = true)String q, @RequestParam(name = "withVideo", required = false, defaultValue = "false")boolean withVideo){
        return talkService.searchTalks(q, withVideo)
    }
}

