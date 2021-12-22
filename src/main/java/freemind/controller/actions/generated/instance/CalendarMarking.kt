package freemind.controller.actions.generated.instance

/* CalendarMarking...*/
class CalendarMarking {
    var name: String? = null
    var color: String? = null
    var startDate: Long = 0
    var endDate: Long = 0
    var repeatType: String? = null
    var repeatEachNOccurence = 0
    var firstOccurence = 0

    companion object {
        /* constants from enums*/
        const val NEVER = "never"
        const val YEARLY = "yearly"
        const val YEARLY_EVERY_NTH_DAY = "yearly_every_nth_day"
        const val YEARLY_EVERY_NTH_WEEK = "yearly_every_nth_week"
        const val YEARLY_EVERY_NTH_MONTH = "yearly_every_nth_month"
        const val MONTHLY = "monthly"
        const val MONTHLY_EVERY_NTH_DAY = "monthly_every_nth_day"
        const val MONTHLY_EVERY_NTH_WEEK = "monthly_every_nth_week"
        const val WEEKLY = "weekly"
        const val WEEKLY_EVERY_NTH_DAY = "weekly_every_nth_day"
        const val DAILY = "daily"
    }
} /* CalendarMarking*/
