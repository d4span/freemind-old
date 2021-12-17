package freemind.controller.actions.generated.instance

import java.util.*

/* CalendarMarkings...*/   class CalendarMarkings : XmlAction() {
    /* constants from enums*/
    fun addCalendarMarking(calendarMarking: CalendarMarking?) {
        calendarMarkingList.add(calendarMarking)
    }

    fun addAtCalendarMarking(position: Int, calendarMarking: CalendarMarking?) {
        calendarMarkingList.add(position, calendarMarking)
    }

    fun getCalendarMarking(index: Int): CalendarMarking {
        return calendarMarkingList[index] as CalendarMarking
    }

    fun removeFromCalendarMarkingElementAt(index: Int) {
        calendarMarkingList.removeAt(index)
    }

    fun sizeCalendarMarkingList(): Int {
        return calendarMarkingList.size
    }

    fun clearCalendarMarkingList() {
        calendarMarkingList.clear()
    }

    val listCalendarMarkingList: List<*>
        get() = Collections.unmodifiableList(calendarMarkingList)
    protected var calendarMarkingList: MutableList<CalendarMarking?> = mutableListOf()
} /* CalendarMarkings*/