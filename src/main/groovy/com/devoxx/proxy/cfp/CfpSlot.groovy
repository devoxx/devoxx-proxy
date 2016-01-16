package com.devoxx.proxy.cfp

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by sarbogast on 15/01/2016.
 */
class CfpSlot {
    String roomId
    boolean notAllocated
    long fromTimeMillis
    @JsonProperty("break") CfpBreak cfpBreak
    String roomSetup
    CfpTalk talk
    String fromTime
    long toTimeMillis
    String toTime
    int roomCapacity
    String roomName
    String slotId
    String day
}
