package com.devoxx.proxy.domain

import org.hibernate.search.annotations.Analyzer
import org.hibernate.search.annotations.ContainedIn
import org.hibernate.search.annotations.Field

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

/**
 * Created by sarbogast on 13/01/2016.
 */
@Entity
class Track {
    @Id
    @GeneratedValue
    long id

    String trackId
    @Field(analyzer=@Analyzer(definition="ngram")) String title

    @ContainedIn
    @OneToMany(mappedBy = "track")
    Set<Talk> talks = new HashSet<>()

    String toString() { title }
}
