package com.mdreamfever.fstar.repository

import com.mdreamfever.fstar.model.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, Int> {
    fun findTopByMaxVisibleBuildNumberGreaterThanEqualOrderByPublishTimeDesc(maxVisibleBuildNumber: Int): Message?
}