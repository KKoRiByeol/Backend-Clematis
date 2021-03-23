package com.dsm.clematis.domain.template.service

import com.dsm.clematis.domain.template.domain.Template
import com.dsm.clematis.domain.template.exception.AlreadyExistTemplateException
import com.dsm.clematis.domain.template.repository.TemplateRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class TemplateCreationServiceTest {
    private val templateRepository = mockk<TemplateRepository>()
    private val testService = TemplateCreationService(templateRepository)

    private val savedTemplate = Template("saved title", "saved body")
    private val newTemplate = Template("title", "body")

    @BeforeEach
    fun setup() {
        savedTemplate.id = 1
        newTemplate.id = 2
    }

    @Test
    fun `푸시 템플릿 생성하기`() {
        every { templateRepository.existsByTitleAndBody(savedTemplate.title, savedTemplate.body) } returns true
        every { templateRepository.existsByTitleAndBody(newTemplate.title, newTemplate.body) } returns false
        every { templateRepository.save(any()) } returns newTemplate

        val creationNumber = testService.createTemplate("title", "body")

        assertThat(creationNumber).isEqualTo(2)
        verify(exactly = 1) { templateRepository.existsByTitleAndBody(newTemplate.title, newTemplate.body) }
        verify(exactly = 1) { templateRepository.save(any()) }
    }

    @Test
    fun `푸시 템플릿 생성하기 - throw AlreadyExistTemplateException`() {
        every { templateRepository.existsByTitleAndBody(savedTemplate.title, savedTemplate.body) } returns true
        every { templateRepository.existsByTitleAndBody(newTemplate.title, newTemplate.body) } returns false
        every { templateRepository.save(any()) } returns newTemplate

        assertThrows<AlreadyExistTemplateException> { testService.createTemplate("saved title", "saved body") }

        verify(exactly = 1) { templateRepository.existsByTitleAndBody(savedTemplate.title, savedTemplate.body) }
        verify(exactly = 0) { templateRepository.save(any()) }
    }
}