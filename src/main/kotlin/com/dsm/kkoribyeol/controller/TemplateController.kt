package com.dsm.kkoribyeol.controller

import com.dsm.kkoribyeol.controller.request.TemplateRequest
import com.dsm.kkoribyeol.controller.response.TemplateCreationResponse
import com.dsm.kkoribyeol.controller.response.TemplateSearchAllResponse
import com.dsm.kkoribyeol.controller.response.TemplateSearchAllResponse.TemplateSearchResponse
import com.dsm.kkoribyeol.controller.response.TemplateSearchDetailResponse
import com.dsm.kkoribyeol.exception.TemplateSearchException
import com.dsm.kkoribyeol.service.TemplateCreationService
import com.dsm.kkoribyeol.service.TemplateDeletionService
import com.dsm.kkoribyeol.service.TemplateModificationService
import com.dsm.kkoribyeol.service.TemplateSearchService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@RestController
@RequestMapping("/template")
@Validated
class TemplateController(
    private val creationService: TemplateCreationService,
    private val modificationService: TemplateModificationService,
    private val deletionService: TemplateDeletionService,
    private val searchService: TemplateSearchService,
) {

    @PostMapping
    fun createTemplate(
        @Valid
        @RequestBody
        request: TemplateRequest,

    ) = TemplateCreationResponse(
        creationNumber = creationService.createTemplate(
            templateTitle = request.title,
            templateBody = request.body,
        )
    )

    @PatchMapping("/{templateId}")
    fun modifyTemplate(
        @NotNull(message = "<NULL>")
        @Positive(message = "<양수가 아님>")
        @PathVariable("templateId")
        templateId: Long,

        @RequestBody @Valid
        request: TemplateRequest,

    ) = modificationService.modifyTemplate(
        templateId = templateId,
        templateTitle = request.title,
        templateBody = request.body,
    )

    @DeleteMapping("/{templateId}")
    fun deleteTemplate(
        @NotNull(message = "<NULL>") @Positive(message = "<양수가 아님>")
        @PathVariable("templateId")
        templateId: Long,
    ) = deletionService.deleteTemplate(
        templateId = templateId,
    )

    @GetMapping
    fun searchTemplate() =
        TemplateSearchAllResponse(
            templates = searchService.searchAllTemplate()
                .map {
                    TemplateSearchResponse(
                        templateId = it.id ?: throw TemplateSearchException(),
                        templateTitle = it.title,
                        templateBody = it.body,
                    )
                }
        )

    @GetMapping("/{templateId}")
    fun searchTemplateDetail(
        @NotNull(message = "<NULL>") @Positive(message = "<양수가 아님>")
        @PathVariable("templateId")
        templateId: Long,
    ): TemplateSearchDetailResponse {
        val findTemplate = searchService.searchTemplate(templateId)
        return TemplateSearchDetailResponse(
            templateId = findTemplate.id ?: throw TemplateSearchException(),
            templateTitle = findTemplate.title,
            templateBody = findTemplate.body,
            templateCreatedAt = findTemplate.createdAt,
            templateUpdatedAt = findTemplate.updatedAt,
        )
    }
}