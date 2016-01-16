package com.devoxx.proxy.repository

import com.devoxx.proxy.domain.Conference
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * Created by sarbogast on 13/01/2016.
 */
@Repository
interface ConferenceRepository extends CrudRepository<Conference, Long> {
    Conference findByEventCode(String eventCode)
}
