package io.flaterlab.meshexam.feature.meshroom.ui.event

import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.domain.exam.model.ExamEvent
import io.flaterlab.meshexam.domain.exam.model.ExamEventModel
import io.flaterlab.meshexam.feature.meshroom.R
import io.flaterlab.meshexam.feature.meshroom.dvo.EventDvo
import javax.inject.Inject

internal class ExamEventModelToDvoMapper @Inject constructor(

) : Mapper<ExamEventModel, EventDvo> {

    override fun invoke(input: ExamEventModel): EventDvo {
        return EventDvo(
            title = resolveTitle(input.event),
            owner = input.userFullName,
            timeInMillis = input.time.time,
            isActive = false,
        )
    }

    private fun resolveTitle(event: ExamEvent): Text = when (event) {
        ExamEvent.SCREEN_HID -> Text.from(R.string.monitor_event_screenHid)
        ExamEvent.SCREEN_VISIBLE -> Text.from(R.string.monitor_event_screenOpened)
        else -> throw IllegalArgumentException("No event type with int: ${event.typeInt}")
    }
}