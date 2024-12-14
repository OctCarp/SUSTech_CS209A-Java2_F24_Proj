package io.github.octcarp.sustech.cs209a.proj.api.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/demo")
class DemoController {

    @RequestMapping("/hello")
    fun hello(): String {
        return "Hello, World!"
    }
}