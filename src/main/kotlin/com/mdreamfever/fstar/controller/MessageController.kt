package com.mdreamfever.fstar.controller

import com.mdreamfever.fstar.model.FResult
import com.mdreamfever.fstar.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v2/message")
class MessageController {


    @Autowired
    private lateinit var messageRepository: MessageRepository

    @GetMapping("/latest")
    fun getLatestMessage(@RequestParam buildNumber: Int): FResult {
        val message = messageRepository.findTopByMaxVisibleBuildNumberGreaterThanEqualOrderByPublishTimeDesc(buildNumber)
        return FResult.success(data = message)
    }

    @GetMapping
    fun getAllMessage(): FResult {
        return FResult.success(data = messageRepository.findAll(Sort.by("publishTime").descending()))
    }
}