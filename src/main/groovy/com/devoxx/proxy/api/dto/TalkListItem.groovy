package com.devoxx.proxy.api.dto

/**
 * Created by sarbogast on 16/01/2016.
 */
class TalkListItem {
    String talkId
    String title
    String summary
    String talkType
    String trackId
    String lang
    Double averageRating
    Integer numberOfRatings
    String youtubeVideoId
    String thumbnailUrl
    String conferenceEventCode
    List<String> speakerUuids
    List<String> speakerNames
    String trackTitle
    Long durationInSeconds
}
