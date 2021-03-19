package com.dsm.kkoribyeol.controller

import com.dsm.kkoribyeol.controller.request.TargetModificationRequest
import com.dsm.kkoribyeol.controller.request.TargetRegistrationAllRequest
import com.dsm.kkoribyeol.controller.request.TargetUnregisterRequest
import com.dsm.kkoribyeol.service.TargetModificationService
import com.dsm.kkoribyeol.service.TargetRegistrationService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Size

@RestController
@RequestMapping("/target")
@Validated
class TargetController(
    private val registrationService: TargetRegistrationService,
    private val modificationService: TargetModificationService,
) {

    @PostMapping
    fun registerTarget(
        @Size(min = 9, max = 28, message = "<9~28>")
        @RequestHeader("projectCode")
        projectCode: String,
        @Valid
        @RequestBody
        request: TargetRegistrationAllRequest,
    ) = registrationService.registerTarget(
        projectCode = projectCode,
        targets = request.targets
    )

    @PatchMapping("/{targetToken}")
    fun modifyTarget(
        @Size(min = 9, max = 28, message = "<9~28>")
        @RequestHeader("projectCode")
        projectCode: String,
        @Size(min = 1, max = 255, message = "<1~255>")
        @PathVariable("targetToken")
        targetToken: String,
        @Valid
        @RequestBody
        request: TargetModificationRequest,
    ) = modificationService.modifyTarget(
        projectCode = projectCode,
        targetToken = targetToken,
        newNickname = request.nickname,
        newName = request.name,
    )

    @DeleteMapping
    fun unregisterTarget(
        @Size(min = 9, max = 28, message = "<9~28>")
        @RequestHeader("projectCode")
        projectCode: String,
        @Valid
        @RequestBody
        request: TargetUnregisterRequest,
    ) = registrationService.unregisterTarget(
        projectCode = projectCode,
        targets = request.tokens,
    )

    @GetMapping
    fun searchTarget() {

    }

    @GetMapping("/{targetToken}")
    fun searchTargetDetail(
        @Size(min = 1, max = 255, message = "<1~255>")
        @PathVariable("targetToken")
        targetId: String,
    ) {

    }
}