package com.devoxx.proxy.service

import com.devoxx.proxy.api.dto.SpeakerListItem
import com.devoxx.proxy.api.dto.TalkDetail
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

    TalkDetail findTalkByTalkId(String talkId) {
        Talk talk = talkRepository.findByTalkId(talkId)
        if(talk == null) return null
        return new TalkDetail(
                durationInSeconds: talk.youtubeVideoDurationInSeconds,
                talkId: talk.talkId,
                talkType: talk.talkType,
                title: talk.title,
                summary: talk.summary,
                summaryAsHtml: talk.summaryAsHtml,
                trackTitle: talk.track?.title,
                lang: talk.lang,
                averageRating: talk.averageRating,
                numberOfRatings: talk.numberOfRatings,
                youtubeVideoId: talk.youtubeVideoId,
                thumbnailUrl: talk.thumbnailUrl,
                conferenceLabel: talk.conference?.label,
                speakers: talk.speakers.collect { speaker ->
                    new SpeakerListItem(
                            uuid: speaker.uuid,
                            firstName: speaker.firstName,
                            lastName: speaker.lastName,
                            lang: speaker.lang,
                            avatarUrl: speaker.avatarUrl,
                            company: speaker.company
                    )
                }.sort{it.lastName}
        )
    }

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
