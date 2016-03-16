package com.devoxx.proxy.repository

import com.devoxx.proxy.domain.Talk
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * Created by sarbogast on 15/01/2016.
 */
@Repository
interface TalkRepository extends CrudRepository<Talk, Long>{
    Talk findByTalkId(String talkId)
    List<Talk> findAllByYoutubeVideoId(String youtubeVideoId)
    List<Talk> findAllByYoutubeVideoIdNotNull()
    List<Talk> findAllByYoutubeVideoIdNotNullAndAverageRatingNotNullAndNumberOfRatingsGreaterThanEqualOrderByAverageRatingDesc(int numberOfRatings, Pageable pageable)
    List<Talk> findAllByAverageRatingNotNullAndNumberOfRatingsGreaterThanEqualOrderByAverageRatingDesc(int numberOfRatings, Pageable pageable)
}