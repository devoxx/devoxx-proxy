package com.devoxx.proxy.domain

import org.apache.lucene.analysis.core.LowerCaseFilterFactory
import org.apache.lucene.analysis.ngram.NGramFilterFactory
import org.apache.lucene.analysis.standard.StandardTokenizerFactory
import org.hibernate.search.annotations.Analyzer
import org.hibernate.search.annotations.AnalyzerDef
import org.hibernate.search.annotations.AnalyzerDefs
import org.hibernate.search.annotations.Boost
import org.hibernate.search.annotations.Field
import org.hibernate.search.annotations.Indexed
import org.hibernate.search.annotations.IndexedEmbedded
import org.hibernate.search.annotations.Parameter
import org.hibernate.search.annotations.TokenFilterDef
import org.hibernate.search.annotations.TokenizerDef

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
@AnalyzerDefs([
    @AnalyzerDef(name = "ngram",
            tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
            filters = [
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = NGramFilterFactory.class,
                        params = [
                            @Parameter(name = "minGramSize",value = "3"),
                            @Parameter(name = "maxGramSize",value = "3")
                        ])
            ])
])
class Talk {
    @Id
    @GeneratedValue
    long id

    String talkId
    @Field(boost = @Boost(2.0f), analyzer=@Analyzer(definition="ngram")) String title
    String talkType
    @Field(analyzer=@Analyzer(definition="ngram")) @Column(columnDefinition = "TEXT") String summary
    @Column(columnDefinition = "TEXT") String summaryAsHtml
    String lang

    Double averageRating
    Integer numberOfRatings

    URL thumbnailUrl
    String youtubeVideoId
    Long youtubeVideoDurationInSeconds
    Boolean youtubeVideoManuallyFixed = false

    @IndexedEmbedded @ManyToOne Track track
    @IndexedEmbedded @ManyToOne Conference conference
    @IndexedEmbedded @ManyToMany(mappedBy = "talks") Set<Speaker> speakers = new HashSet<>()

    String toString() { title }
}
