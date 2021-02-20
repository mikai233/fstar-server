package com.mdreamfever.fstar.controller

import com.mdreamfever.fstar.repository.ChangelogRepository
import com.mdreamfever.fstar.model.Changelog
import com.mdreamfever.fstar.model.FResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.Sort
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import javax.annotation.security.PermitAll

@RestController
@RequestMapping("/v2/changelog")
class ChangelogController {

    @Autowired
    private lateinit var changelogRepository: ChangelogRepository

    @GetMapping
    fun getChangelog(): FResult {
        return FResult.success(data = changelogRepository.findAll(Sort.by("buildNumber").descending()))
    }

    @GetMapping("/{buildNumber}")
    fun getChangelogByBuildNumber(@PathVariable buildNumber: Int): FResult {
        val changelog = Changelog(null, buildNumber, null, null, null)
        val example = Example.of(changelog)
        return FResult.success(data = changelogRepository.findAll(example))
    }

    @GetMapping("/list/{buildNumber}")
    fun getChangelogLessThanBuildNumber(@PathVariable buildNumber: Int): FResult {
        return FResult.success(data = changelogRepository.findAllByBuildNumberLessThanEqual(buildNumber))
    }

    @GetMapping("/current_version")
    fun getCurrentVersion(): FResult {
        val changelog = changelogRepository.findAll(Sort.by("buildNumber").descending())
        return FResult.success(data = changelog.first())
    }
}