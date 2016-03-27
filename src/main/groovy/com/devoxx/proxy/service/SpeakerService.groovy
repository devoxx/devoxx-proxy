package com.devoxx.proxy.service

import com.devoxx.proxy.api.dto.SpeakerDetail
import com.devoxx.proxy.api.dto.SpeakerListItem
import com.devoxx.proxy.api.dto.TalkListItem
import com.devoxx.proxy.domain.Speaker
import com.devoxx.proxy.repository.SpeakerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by sarbogast on 16/01/2016.
 */
@Service
class SpeakerService {
    @Autowired
    SpeakerRepository speakerRepository

    List<SpeakerListItem> getSpeakers(boolean withVideo) {
        return speakerRepository.findAllByOrderByLastNameAsc().findAll {speaker -> speaker.talks?.size() > 0 && speaker.talks.find {talk-> withVideo ? talk.youtubeVideoId != null : true}}.collect { speaker ->
            new SpeakerListItem(
                    uuid: speaker.uuid,
                    firstName: speaker.firstName,
                    lastName: speaker.lastName,
                    lang: speaker.lang,
                    avatarUrl: speaker.avatarUrl,
                    company: speaker.company
            )
        }
    }

    SpeakerDetail findTalkByUuid(String uuid) {
        Speaker speaker = speakerRepository.findByUuid(uuid)
        if(speaker == null) return null
        return new SpeakerDetail(
                uuid: speaker.uuid,
                firstName: speaker.firstName,
                lastName: speaker.lastName,
                bio: speaker.bio,
                bioAsHtml: speaker.bioAsHtml,
                company: speaker.company,
                lang: speaker.lang,
                avatarUrl: speaker.avatarUrl.toString(),
                blog: speaker.blog,
                twitter: speaker.twitter,
                talks: speaker.talks.findAll {talk->talk.youtubeVideoId != null}.collect { talk ->
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
                            speakerUuids: talk.speakers.collect {spkr -> spkr.uuid},
                            speakerNames: talk.speakers.collect {spkr -> spkr.fullName},
                            trackTitle: talk.track?.title
                    )
                }.sort{it.title}
        )
    }
}
