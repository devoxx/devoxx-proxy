package com.devoxx.proxy.api.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Created by sarbogast on 05/03/2016.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String resourceId) {
        super("Could not find resource with identifier " + resourceId)
    }
}
