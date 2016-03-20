package com.devoxx.proxy.service

import org.hibernate.search.jpa.FullTextEntityManager
import org.hibernate.search.jpa.Search
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * Created by sarbogast on 20/03/2016.
 */
@Component
class SearchIndexBuilder implements ApplicationListener{
    @PersistenceContext
    private EntityManager entityManager

    void onApplicationEvent(ApplicationEvent event) {
        try {
            FullTextEntityManager fullTextEntityManager =
                    Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            System.out.println(
                    "An error occurred trying to build the search index: " +
                            e.toString());
        }
    }
}
