package com.devoxx.proxy.repository

import com.devoxx.proxy.domain.Track
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * Created by sarbogast on 15/01/2016.
 */
@Repository
interface TrackRepository extends CrudRepository<Track, Long> {
    Track findByTrackId(String trackId)
}