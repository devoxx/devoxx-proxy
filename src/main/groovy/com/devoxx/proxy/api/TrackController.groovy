package com.devoxx.proxy.api

import com.devoxx.proxy.api.dto.TrackListItem
import com.devoxx.proxy.service.TrackService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Created by sarbogast on 16/01/2016.
 */
@RestController
@RequestMapping("/api")
class TrackController {
    @Autowired
    TrackService trackService

    @RequestMapping(value="/tracks", method = RequestMethod.GET)
    List<TrackListItem> index(@RequestParam(name = "withVideo", required = false, defaultValue = "false")boolean withVideo,
                              @RequestParam(name = "loadTalks", required = false, defaultValue = "true")boolean loadTalks) {
        return trackService.getTracks(withVideo, loadTalks)
    }
}
