package io.flaterlab.meshexam.domain.exam.model

@JvmInline
value class ExamEvent private constructor(
    val typeInt: Int,
) {

    companion object {

        val SCREEN_HID get() = ExamEvent(1)
        val SCREEN_VISIBLE get() = ExamEvent(2)
    }
}