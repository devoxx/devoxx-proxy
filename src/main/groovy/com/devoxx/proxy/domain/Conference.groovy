package com.devoxx.proxy.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.validation.constraints.NotNull

/**
 * Created by sarbogast on 13/01/2016.
 */
@Entity
class Conference {
    @Id
    @GeneratedValue
    long id

    @NotNull String eventCode
    @NotNull String label
    String localisation
    String[] locale

    @OneToMany(mappedBy = "conference")
    List<Talk> talks = new ArrayList<>()

    String toString() { label }
}
