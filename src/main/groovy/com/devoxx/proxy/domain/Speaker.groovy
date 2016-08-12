package com.devoxx.proxy.domain

import org.hibernate.annotations.GenericGenerator
import org.hibernate.search.annotations.Analyzer
import org.hibernate.search.annotations.ContainedIn
import org.hibernate.search.annotations.Field

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.ManyToMany
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.Table
import javax.validation.constraints.NotNull

/**
 * Created by sarbogast on 13/01/2016.
 */
@Entity
@Table(name="speaker", indexes = [
        @Index(columnList = "business_id", name = "business_id_idx")
])
class Speaker {
    @Id
    @GeneratedValue
    long id

    @Column(name="business_id", unique = true)
    String businessId

    @NotNull String uuid
    @Column(columnDefinition = "TEXT") String bioAsHtml
    @Field(analyzer=@Analyzer(definition="ngram")) @Column(columnDefinition = "TEXT") String bio
    @Field(analyzer=@Analyzer(definition="ngram")) String company
    @Field(analyzer=@Analyzer(definition="ngram")) String firstName
    @Field(analyzer=@Analyzer(definition="ngram")) String lastName
    String lang

    URL avatarUrl
    String blog
    String twitter

    @ContainedIn
    @ManyToMany
    Set<Talk> talks = new HashSet<>()

    String toString() { fullName }

    String getFullName() { "$firstName $lastName"}

    @PrePersist
    void onCreate() {
        businessId = UUID.randomUUID().toString()
    }

    @PreUpdate
    void onPersist() {
        if(!businessId) {
            businessId = UUID.randomUUID().toString()
        }
    }
}
