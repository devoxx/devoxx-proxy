package com.devoxx.proxy.service

import com.devoxx.proxy.cfp.*
import com.devoxx.proxy.domain.Conference
import com.devoxx.proxy.domain.Speaker
import com.devoxx.proxy.domain.Talk
import com.devoxx.proxy.domain.Track
import com.devoxx.proxy.repository.ConferenceRepository
import com.devoxx.proxy.repository.SpeakerRepository
import com.devoxx.proxy.repository.TalkRepository
import com.devoxx.proxy.repository.TrackRepository
import com.devoxx.proxy.voting.Vote
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestClientException

/**
 * Created by sarbogast on 13/01/2016.
 */
@Service
@ConfigurationProperties(prefix = "devoxx")
@Transactional
class CfpUpdateService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired ConferenceRepository conferenceRepository
    @Autowired TalkRepository talkRepository
    @Autowired TrackRepository trackRepository
    @Autowired SpeakerRepository speakerRepository

    @Autowired YoutubeService youtubeService
    @Autowired CfpClientService devoxx
    @Autowired VotingService votes

    List<Map<String,String>> cfpApis

    void updateData() {
        log.info("Updating data...")

        try {
            cfpApis.each { api ->
                updateEvent(api)
            }
        } catch (Exception exc) {
            exc.printStackTrace()
        }

        log.info("Data updated!")
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    def updateEvent(Map<String, String> api){
        try {
            CfpLinks apiConferences = devoxx.getConferences(api.url)
            apiConferences.links.each { link ->
                updateConference(link, api.youtubeChannelId)
            }
        } catch (RestClientException exc) {
            log.error("Error when trying to load URL ${api.url}: ${exc.getMessage()}")
        }
    }

    private void updateConference(CfpLink link, String youtubeChannelId) {
        log.info("Processing ${link.title}...")
        try {
            CfpConference apiConference = devoxx.getConference(link.href)
            Conference conference = conferenceRepository.findByEventCode(apiConference.eventCode)
            if (conference == null) {
                conference = new Conference(eventCode: apiConference.eventCode)
            }
            //Hibernate.initialize(conference.talks)
            conference.label = apiConference.label
            conference.localisation = apiConference.localisation
            String[] locale = []
            apiConference.locale.each {
                locale += it
            }
            conference.locale = new HashSet<>(locale.toList())
            conferenceRepository.save(conference)

            def schedulesEndpoint = apiConference.links.find { it.rel.endsWith("/schedules") }?.href
            try {
                CfpLinks apiSchedules = devoxx.getSchedules(schedulesEndpoint)
                def scheduleLinks = apiSchedules?.links?.findAll { it.rel?.endsWith("/schedule") }
                scheduleLinks?.each { scheduleLink ->
                    updateSchedule(scheduleLink, conference, youtubeChannelId)
                }
            } catch (RestClientException exc) {
                log.error("Error while accessing ${schedulesEndpoint}: ${exc.getMessage()}")
            }
        } catch (RestClientException exc) {
            log.error("Error when trying to load URL ${link.href}: ${exc.getMessage()}")
        }
    }

    private void updateSchedule(CfpLink scheduleLink, Conference conference, String youtubeChannelId) {
        log.info("Processing schedule ${scheduleLink.title}...")
        try {
            CfpSchedule apiSchedule = devoxx.getSchedule(scheduleLink.href)
            def apiTalks = apiSchedule.slots?.findAll { it.talk != null }
            apiTalks.each { apiSlot ->
                def apiTalk = apiSlot.talk
                def talkId = "${conference.eventCode}_${apiTalk.id}"
                try {
                    def talk = talkRepository.findByTalkId(talkId)
                    if (talk == null) {
                        talk = new Talk(talkId: talkId)
                    }
                    talk.title = apiTalk.title
                    talk.summary = apiTalk.summary
                    talk.summaryAsHtml = apiTalk.summaryAsHtml
                    talk.lang = apiTalk.lang
                    talk.talkType = apiTalk.talkType

                    def talkToRemove = conference.talks.find { it.talkId == talkId.toString() }
                    if (talkToRemove) conference.talks.remove(talkToRemove)

                    conference.talks.add(talk)
                    talk.conference = conference
                    talkRepository.save(talk)

                    updateTrack(apiTalk, talk)
                    updateSpeakers(apiTalk, talk)
                    updateYoutube(talk, youtubeChannelId)
                    updateVotes(apiTalk, talk)

                    talkRepository.save(talk)
                } catch (NullPointerException exc) {
                    log.warn(exc.message)
                }
            }
        } catch (RestClientException exc) {
            log.error("Error while accessing ${scheduleLink.href}: ${exc.message}")
        }
    }

    private void updateVotes(CfpTalk apiTalk, Talk talk) {
        def url = "https://api-voting.devoxx.com/${talk.conference.eventCode}/talk/${apiTalk.id}"
        try {
            Vote vote = votes.getVote(url.toString())
            if (vote.avg) talk.averageRating = new Double(vote.avg as double)
            if (vote.count) talk.numberOfRatings = new Integer(vote.count as int)
        } catch (Exception exc) {
            log.warn("Error while accessing url ${url}: ${exc.getMessage()}")
        }
    }

    private void updateYoutube(Talk talk, String youtubeChannelId) {
        def speakers = talk.speakers.collect { it.firstName + ' ' + it.lastName }.join("/")
        def youtubeVideo = youtubeService.getYoutubeUrl("${talk.title} by $speakers", talk.talkType, youtubeChannelId)
        if (youtubeVideo) {
            talk.thumbnailUrl = new URL(youtubeVideo.thumbnailUrl)
            talk.youtubeVideoId = youtubeVideo.videoId
        }
    }

    private void updateTrack(CfpTalk apiTalk, Talk talk) {
        def talkToRemove
        if (apiTalk.trackId) {
            def track = trackRepository.findByTrackId(apiTalk.trackId)
            if (track == null) {
                track = new Track(trackId: apiTalk.trackId)
            }
            track.title = apiTalk.track
            trackRepository.save(track)

            talkToRemove = track.talks.find { it.talkId == talk.talkId }
            if (talkToRemove) track.talks.remove(talkToRemove)
            track.talks.add(talk)
            talk.track = track
            trackRepository.save(track)
        }
    }

    private def updateSpeakers(CfpTalk apiTalk, Talk talk) {
        return apiTalk.speakers?.each { apiSpeakerLink ->
            try {
                CfpSpeaker apiSpeaker = devoxx.getSpeaker(apiSpeakerLink.link.href)
                def speaker
                if (apiSpeaker.twitter) {
                    def twitter = apiSpeaker.twitter?.replace('@', '')
                    speaker = speakerRepository.findByTwitter(twitter)
                    if (speaker == null) {
                        speaker = new Speaker(twitter: twitter)
                    }
                } else if (apiSpeaker.blog) {
                    speaker = speakerRepository.findByBlog(apiSpeaker.blog)
                    if (speaker == null) {
                        speaker = new Speaker(blog: apiSpeaker.blog)
                    }
                } else {
                    speaker = speakerRepository.findByUuid(apiSpeaker.uuid)
                    if (speaker == null) {
                        speaker = new Speaker(uuid: apiSpeaker.uuid)
                    }
                }

                speaker.uuid = apiSpeaker.uuid
                speaker.firstName = apiSpeaker.firstName
                speaker.lastName = apiSpeaker.lastName
                speaker.bio = apiSpeaker.bio
                speaker.bioAsHtml = apiSpeaker.bioAsHtml
                speaker.company = apiSpeaker.company
                speaker.blog = apiSpeaker.blog
                speaker.avatarUrl = apiSpeaker.avatarURL ? new URL(apiSpeaker.avatarURL) : null
                speaker.lang = apiSpeaker.lang
                speaker.twitter = apiSpeaker.twitter?.replace('@', '')
                speakerRepository.save(speaker)

                def speakerToRemove = talk.speakers.find { it.uuid == speaker.uuid }
                if (speakerToRemove) {
                    talk.speakers.remove(speaker)
                }
                def talkToRemove = speaker.talks.find { it.talkId == talk.talkId }
                if (talkToRemove) {
                    speaker.talks.remove(talkToRemove)
                }
                talk.speakers.add(speaker)
                speaker.talks.add(talk)
            } catch (RestClientException exc) {
                log.error("Error while accessing ${apiSpeakerLink.link.href}: ${exc.getMessage()}")
            }
        }
    }
}
