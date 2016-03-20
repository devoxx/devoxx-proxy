package com.devoxx.proxy.domain

import org.hibernate.search.annotations.ContainedIn
import org.hibernate.search.annotations.Field

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.validation.constraints.NotNull

/**
 * Created by sarbogast on 13/01/2016.
 */
@Entity
class Speaker {
    @Id
    @GeneratedValue
    long id

    @NotNull String uuid
    @Column(columnDefinition = "TEXT") String bioAsHtml
    @Field @Column(columnDefinition = "TEXT") String bio
    @Field String company
    @Field String firstName
    @Field String lastName
    String lang

    URL avatarUrl
    String blog
    String twitter

    @ContainedIn
    @ManyToMany
    Set<Talk> talks = new HashSet<>()

    String toString() { fullName }

    String getFullName() { "$firstName $lastName"}
}
