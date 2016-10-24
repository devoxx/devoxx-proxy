package com.devoxx.proxy.service

import org.hibernate.search.jpa.FullTextEntityManager
import org.hibernate.search.jpa.Search
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
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
class SearchIndexBuilder implements ApplicationListener<ApplicationReadyEvent>{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager entityManager

    void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Rebuilding search index...");
        try {
            FullTextEntityManager fullTextEntityManager =
                    Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager
                    .createIndexer()
                    .purgeAllOnStart(true)
                    .optimizeAfterPurge(true)
                    .optimizeOnFinish(true)
                    .startAndWait()
            log.info("Search index rebuilt.")
        } catch (InterruptedException e) {
            log.error(
                    "An error occurred trying to build the search index: " +
                            e.toString());
        }
    }
}
