package com.devoxx.proxy.repository

import com.devoxx.proxy.domain.Talk
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

/**
 * Created by sarbogast on 15/01/2016.
 */
@Repository
interface TalkRepository extends PagingAndSortingRepository<Talk, Long> {
    Talk findByTalkId(String talkId)
    Page<Talk> findAllByTrackTrackId(String trackId, Pageable pageRequest)
    Page<Talk> findAllByTrackTrackIdAndYoutubeVideoIdNotNull(String trackId, Pageable pageRequest)
    List<Talk> findAllByYoutubeVideoId(String youtubeVideoId)
    Page<Talk> findAllByYoutubeVideoIdNotNull(Pageable pageRequest)
    List<Talk> findAllByYoutubeVideoIdNotNullAndAverageRatingNotNullAndNumberOfRatingsGreaterThanEqualOrderByAverageRatingDesc(int numberOfRatings, Pageable pageable)
    List<Talk> findAllByAverageRatingNotNullAndNumberOfRatingsGreaterThanEqualOrderByAverageRatingDesc(int numberOfRatings, Pageable pageable)
}