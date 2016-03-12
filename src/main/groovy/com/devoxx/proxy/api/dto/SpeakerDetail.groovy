package com.devoxx.proxy.api.dto

/**
 * Created by sarbogast on 12/03/2016.
 */
class SpeakerDetail {
    String uuid
    String firstName
    String lastName
    String bio
    String bioAsHtml
    String company
    String lang
    String avatarUrl
    String blog
    String twitter
    List<TalkListItem> talks
}
