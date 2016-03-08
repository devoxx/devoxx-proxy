package com.devoxx.proxy.api.dto

/**
 * Created by sarbogast on 05/03/2016.
 */
class TalkDetail {
    String talkId
    String talkType
    String title
    String summary
    String summaryAsHtml
    String trackTitle
    String lang
    Double averageRating
    Integer numberOfRatings
    String youtubeVideoId
    String thumbnailUrl
    String conferenceLabel
    List<SpeakerListItem> speakers
    Long durationInSeconds
}
