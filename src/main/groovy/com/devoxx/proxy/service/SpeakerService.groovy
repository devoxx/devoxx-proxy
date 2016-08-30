package com.devoxx.proxy.service

import com.devoxx.proxy.api.dto.SpeakerDetail
import com.devoxx.proxy.api.dto.SpeakerListItem
import com.devoxx.proxy.api.dto.TalkListItem
import com.devoxx.proxy.domain.Speaker
import com.devoxx.proxy.repository.SpeakerRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * Created by sarbogast on 16/01/2016.
 */
@Service
class SpeakerService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SpeakerRepository speakerRepository

    Page<SpeakerListItem> getSpeakers(boolean withVideo, Pageable pageRequest = null) {
        log.info("Starting to load speakers from database...")

        List<Speaker> speakers
        if (withVideo) {
            speakers = speakerRepository.findAllWithTalksByLastNameAsc()
        } else {
            speakers = speakerRepository.findAllByOrderByLastNameAsc()
        }
        log.info("Loaded ${speakers.size()} speakers from database, now filtering...")

        long totalSpeakers = speakers.size()

        if(pageRequest) {
            if(pageRequest.offset >= speakers.size()) return new PageImpl<SpeakerListItem>([], pageRequest, speakers.size())
            speakers = speakers.subList(pageRequest.offset, Math.min(pageRequest.offset + pageRequest.pageSize, speakers.size()))
        }

        def results = speakers.collect { speaker ->
            new SpeakerListItem(
                    uuid: speaker.businessId,
                    firstName: speaker.firstName,
                    lastName: speaker.lastName,
                    lang: speaker.lang,
                    avatarUrl: speaker.avatarUrl,
                    company: speaker.company
            )
        }

        log.info("Filtering done. Returning ${results.size()} speakers.")

        return new PageImpl<SpeakerListItem>(results, pageRequest, totalSpeakers)
    }

    SpeakerDetail findSpeakerByUuid(String uuid) {
        Speaker speaker = speakerRepository.findByBusinessId(uuid)
        if(speaker == null) return null
        return new SpeakerDetail(
                uuid: speaker.businessId,
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
                            speakerUuids: talk.speakers.collect {spkr -> spkr.businessId},
                            speakerNames: talk.speakers.collect {spkr -> spkr.fullName},
                            trackTitle: talk.track?.title,
                            durationInSeconds: talk.youtubeVideoDurationInSeconds
                    )
                }.sort{it.title}
        )
    }
}
