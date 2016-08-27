package com.devoxx.proxy.repository

import com.devoxx.proxy.domain.Speaker
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * Created by sarbogast on 15/01/2016.
 */
@Repository
interface SpeakerRepository extends CrudRepository<Speaker, Long>{

    Speaker findByTwitter(String twitter)

    Speaker findByBlog(String blog)

    Speaker findByUuid(String uuid)

    Speaker findByBusinessId(String businessId)

    List<Speaker> findAllByOrderByLastNameAsc()

    @Query('select s from Speaker s inner join s.talks t where t.youtubeVideoId is not null order by s.lastName asc')
    List<Speaker> findAllWithTalksByLastNameAsc()
}