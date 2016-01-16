package com.devoxx.proxy.service

import com.devoxx.proxy.voting.Vote
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * Created by sarbogast on 16/01/2016.
 */
@Service
class VotingService {
    private RestTemplate template = new RestTemplate()

    Vote getVote(String url) {
        return template.getForObject(url, Vote.class)
    }
}
