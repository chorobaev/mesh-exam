package io.flaterlab.meshexam.librariy.mesh.client.exception

class ExamNotFoundException(
    val examId: String,
) : Exception("Endpoint not found for exam with id: $examId")