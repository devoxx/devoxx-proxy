package com.devoxx.proxy.service

import com.devoxx.proxy.api.dto.SpeakerListItem
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

    List<SpeakerListItem> getSpeakers() {
        return speakerRepository.findAllByOrderByLastNameAsc().findAll {speaker -> speaker.talks?.size() > 0 && speaker.talks.find {talk->talk.youtubeVideoId != null}}.collect { speaker ->
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
}
