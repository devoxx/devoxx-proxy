package com.devoxx.proxy.domain

import org.hibernate.search.annotations.Analyzer
import org.hibernate.search.annotations.ContainedIn
import org.hibernate.search.annotations.Field

import javax.persistence.ElementCollection
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
    @Field(analyzer=@Analyzer(definition="ngram")) @NotNull String label
    String localisation
    @ElementCollection Set<String> locale = new HashSet<>()

    @ContainedIn
    @OneToMany(mappedBy = "conference")
    List<Talk> talks = new ArrayList<>()

    String toString() { label }
}
