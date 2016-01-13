package com.devoxx.proxy.domain

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
    String title

    @OneToMany(mappedBy = "track")
    Set<Talk> talks = new HashSet<>()

    String toString() { title }
}
