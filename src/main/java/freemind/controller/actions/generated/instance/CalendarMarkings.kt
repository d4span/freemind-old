package freemind.controller.actions.generated.instance

import java.util.Collections

/* CalendarMarkings...*/ class CalendarMarkings : XmlAction() {
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

    val listCalendarMarkingList: List<CalendarMarking?>
        get() = Collections.unmodifiableList(calendarMarkingList)
    protected var calendarMarkingList: ArrayList<CalendarMarking?> = ArrayList()
} /* CalendarMarkings*/
