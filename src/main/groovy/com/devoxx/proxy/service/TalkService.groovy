package com.devoxx.proxy.service

import com.devoxx.proxy.api.dto.TalkListItem
import com.devoxx.proxy.domain.Talk
import com.devoxx.proxy.repository.TalkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by sarbogast on 16/01/2016.
 */
@Service
class TalkService {
    @Autowired
    TalkRepository talkRepository

    List<TalkListItem> findAllTalksWithVideo(boolean withVideo) {
        Iterable<Talk> talks;
        if(withVideo) {
            talks = talkRepository.findAllByYoutubeVideoIdNotNull()
        } else {
            talks = talkRepository.findAll()
        }

        return talks.collect { talk ->
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
                    speakerUuids: talk.speakers.collect {speaker -> speaker.uuid}
            )
        }
    }
}
