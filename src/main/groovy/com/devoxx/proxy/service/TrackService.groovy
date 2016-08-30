package com.devoxx.proxy.service

import com.devoxx.proxy.api.dto.TalkListItem
import com.devoxx.proxy.api.dto.TrackListItem
import com.devoxx.proxy.domain.Talk
import com.devoxx.proxy.repository.TrackRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by sarbogast on 16/01/2016.
 */
@Service
class TrackService {
    @Autowired
    TrackRepository trackRepository

    List<TrackListItem> getTracks(boolean withVideo, boolean loadTalks) {
        return trackRepository.findAll().collect { track ->
            return new TrackListItem(
                    trackId: track.trackId,
                    title: track.title,
                    talks: loadTalks ? track.talks?.findAll { Talk talk -> withVideo ? talk.youtubeVideoId != null : true}?.sort { it.title }?.collect { talk ->
                        new TalkListItem(
                                talkId: talk.talkId,
                                title: talk.title,
                                summary: talk.summary,
                                talkType: talk.talkType,
                                trackId: talk.track?.trackId,
                                lang: talk.lang,
                                averageRating: talk.averageRating,
                                numberOfRatings: talk.numberOfRatings,
                                youtubeVideoId: talk.youtubeVideoId,
                                thumbnailUrl: talk.thumbnailUrl,
                                conferenceEventCode: talk.conference?.eventCode,
                                speakerUuids: talk.speakers.collect {speaker -> speaker.businessId},
                                speakerNames: talk.speakers.collect {speaker -> speaker.fullName},
                                trackTitle: talk.track?.title,
                                durationInSeconds: talk.youtubeVideoDurationInSeconds
                        )
                    } : [],
                    numberOfTalks: track.talks?.size()
            )
        }.findAll {track -> track.numberOfTalks > 0}.sort {it.title}
    }
}
