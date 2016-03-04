package com.devoxx.proxy.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.UniqueConstraint
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
    @Column(columnDefinition = "TEXT") String bio
    String company
    String firstName
    String lastName
    String lang

    URL avatarUrl
    String blog
    String twitter

    @ManyToMany
    Set<Talk> talks = new HashSet<>()

    String toString() { fullName }

    String getFullName() { "$firstName $lastName"}
}
