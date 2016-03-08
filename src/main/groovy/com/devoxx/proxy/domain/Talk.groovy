package com.devoxx.proxy.domain

import org.springframework.boot.autoconfigure.SpringBootApplication

import javax.annotation.Nullable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne

/**
 * Created by sarbogast on 13/01/2016.
 */
@Entity
class Talk {
    @Id
    @GeneratedValue
    long id

    String talkId
    String title
    String talkType
    @Column(columnDefinition = "TEXT") String summary
    @Column(columnDefinition = "TEXT") String summaryAsHtml
    String lang

    Double averageRating
    Integer numberOfRatings

    URL thumbnailUrl
    String youtubeVideoId
    Long youtubeVideoDurationInSeconds

    @ManyToOne Track track
    @ManyToOne Conference conference
    @ManyToMany(mappedBy = "talks") Set<Speaker> speakers = new HashSet<>()

    String toString() { title }
}
