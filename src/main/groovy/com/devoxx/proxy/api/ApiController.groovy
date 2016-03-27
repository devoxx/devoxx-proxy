package com.devoxx.proxy.api

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by sarbogast on 27/03/2016.
 */
@Controller
class ApiController {
    @RequestMapping("/api/")
    String index() {
        return "api/index"
    }
}
