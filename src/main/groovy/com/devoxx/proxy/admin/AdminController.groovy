package com.devoxx.proxy.admin

import com.devoxx.proxy.service.CfpUpdateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

/**
 * Created by sarbogast on 20/03/2016.
 */
@Controller
@RequestMapping("/admin")
class AdminController {
    @Autowired
    CfpUpdateService updateService

    @RequestMapping("/")
    String index() {
        return "index"
    }

    @ResponseBody
    @RequestMapping("/update")
    String update() {
        updateService.updateData()
        return "updating..."
    }

    @ResponseBody
    @RequestMapping("/reindex")
    String reindex() {
        updateService.reindex()
        return "reindexing..."
    }
}
