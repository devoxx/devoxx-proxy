package com.devoxx.proxy.service

import com.devoxx.proxy.api.dto.SpeakerListItem
import com.devoxx.proxy.api.dto.TalkDetail
import com.devoxx.proxy.api.dto.TalkListItem
import com.devoxx.proxy.domain.Talk
import com.devoxx.proxy.repository.TalkRepository
import com.devoxx.proxy.repository.TalkSearch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * Created by sarbogast on 16/01/2016.
 */
@Service
class TalkService {
    @Autowired
    TalkRepository talkRepository

    @Autowired
    TalkSearch talkSearch

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
                thumbnailUrl: talk.thumbnailUrl?.toString()?.replace('hqdefault', 'maxresdefault'),
                conferenceLabel: talk.conference?.label,
                speakers: talk.speakers.collect { speaker ->
                    new SpeakerListItem(
                            uuid: speaker.businessId,
                            firstName: speaker.firstName,
                            lastName: speaker.lastName,
                            lang: speaker.lang,
                            avatarUrl: speaker.avatarUrl,
                            company: speaker.company
                    )
                }.sort{it.lastName}
        )
    }

    Page<TalkListItem> findAllTalksWithVideo(boolean withVideo, Pageable pageRequest = null) {
        Page<Talk> talks;
        if(withVideo) {
            talks = talkRepository.findAllByYoutubeVideoIdNotNull(pageRequest)
        } else {
            talks = talkRepository.findAll(pageRequest?:new PageRequest(0, Integer.MAX_VALUE))
        }

        return new PageImpl<TalkListItem>(talks.content.collect { talk ->
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
        }, pageRequest, talks.totalElements)
    }

    Page<TalkListItem> findTalksByTrack(String trackId, boolean withVideo, Pageable pageRequest) {
        Page<Talk> talks
        if(withVideo) {
            talks = talkRepository.findAllByTrackTrackIdAndYoutubeVideoIdNotNull(trackId, pageRequest)
        } else {
            talks = talkRepository.findAllByTrackTrackId(trackId, pageRequest)
        }

        return new PageImpl<TalkListItem>(talks.content.collect { talk ->
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
                    thumbnailUrl: talk.thumbnailUrl?.toString()?.replace('hqdefault', 'maxresdefault'),
                    conferenceEventCode: talk.conference?.eventCode,
                    speakerUuids: talk.speakers.collect {speaker -> speaker.businessId},
                    speakerNames: talk.speakers.collect {speaker -> speaker.fullName},
                    trackTitle: talk.track?.title
            )
        }, pageRequest, talks.totalElements)
    }

    List<TalkListItem> getTopTalks(boolean withVideo, int count) {
        List<Talk> talks
        if(withVideo) {
            talks = talkRepository.findAllByYoutubeVideoIdNotNullAndAverageRatingNotNullAndNumberOfRatingsGreaterThanEqualOrderByAverageRatingDesc(30, new PageRequest(0, count))
        } else {
            talks = talkRepository.findAllByAverageRatingNotNullAndNumberOfRatingsGreaterThanEqualOrderByAverageRatingDesc(30, new PageRequest(0, count))
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
                    thumbnailUrl: talk.thumbnailUrl?.toString()?.replace('hqdefault', 'maxresdefault'),
                    conferenceEventCode: talk.conference?.eventCode,
                    speakerUuids: talk.speakers.collect {speaker -> speaker.businessId},
                    speakerNames: talk.speakers.collect {speaker -> speaker.fullName},
                    trackTitle: talk.track?.title
            )
        }
    }

    List<TalkListItem> searchTalks(String text, boolean withVideo) {
        List<Talk> talks = talkSearch.search(text)
        if(withVideo) {
            talks = talks.findAll{talk->talk.youtubeVideoId != null}
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
                    speakerUuids: talk.speakers.collect {speaker -> speaker.businessId},
                    speakerNames: talk.speakers.collect {speaker -> speaker.fullName},
                    trackTitle: talk.track?.title
            )
        }
    }
}
