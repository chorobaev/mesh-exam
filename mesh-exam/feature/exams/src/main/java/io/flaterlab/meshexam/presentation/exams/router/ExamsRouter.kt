package io.flaterlab.meshexam.presentation.exams.router

interface ExamsRouter {

    fun openCreateExam()

    fun openEditExam(examId: String)
}