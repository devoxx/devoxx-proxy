package com.devoxx.proxy.repository

import com.devoxx.proxy.domain.Talk
import org.apache.lucene.search.Query
import org.hibernate.search.jpa.FullTextQuery
import org.hibernate.search.jpa.FullTextEntityManager
import org.hibernate.search.jpa.Search
import org.hibernate.search.query.dsl.QueryBuilder
import org.springframework.stereotype.Repository

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

/**
 * Created by sarbogast on 20/03/2016.
 */
@Repository
@Transactional
class TalkSearch {

    @PersistenceContext
    private EntityManager entityManager

    List<Talk> search(String text) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager)
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Talk).get()
        Query query = queryBuilder
                .keyword()
                .onFields("title", "summary", "track.title", "conference.label", "speakers.company", "speakers.firstName", "speakers.lastName", "speakers.bio")
                .matching(text)
                .createQuery()
        FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Talk)
        return jpaQuery.getResultList()
    }
}
