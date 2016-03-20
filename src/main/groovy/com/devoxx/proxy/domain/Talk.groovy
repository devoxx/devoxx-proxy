package com.devoxx.proxy.domain

import org.hibernate.search.annotations.Analyze
import org.hibernate.search.annotations.Boost
import org.hibernate.search.annotations.Field
import org.hibernate.search.annotations.Index
import org.hibernate.search.annotations.Indexed
import org.hibernate.search.annotations.IndexedEmbedded
import org.hibernate.search.annotations.Store
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
@Indexed
@Entity
class Talk {
    @Id
    @GeneratedValue
    long id

    String talkId
    @Field(boost = @Boost(2.0f)) String title
    String talkType
    @Field @Column(columnDefinition = "TEXT") String summary
    @Column(columnDefinition = "TEXT") String summaryAsHtml
    String lang

    Double averageRating
    Integer numberOfRatings

    URL thumbnailUrl
    String youtubeVideoId
    Long youtubeVideoDurationInSeconds

    @IndexedEmbedded @ManyToOne Track track
    @IndexedEmbedded @ManyToOne Conference conference
    @IndexedEmbedded @ManyToMany(mappedBy = "talks") Set<Speaker> speakers = new HashSet<>()

    String toString() { title }
}
