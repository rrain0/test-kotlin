package test.`date-time`

import kotlinx.datetime.*
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import com.rrain.util.print.println
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days


fun main() {
  println("kotlinx-datetime")
  println()
  
  // Instant from epoch milliseconds
  run {
    // Clock is a utility interface used to obtain the current Instant.
    // Instant represents a timestamp on the UTC-SLS (UTC with Smoothed Leap Seconds) time scale.
    val instant = Clock.System.now()
    
    val instantFromEpochMillis = Instant.fromEpochMilliseconds(1722475458286)
    val epochMillisFromInstant = instantFromEpochMillis.toEpochMilliseconds()
  }
  
  // TimeZone
  run {
    val instant = Instant.fromEpochSeconds(1722427200)
    println("instant.toString()", instant.toString()) // "2024-07-31T12:00:00Z"
    
    // System time zone is UTC+8 (Irkutsk)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    println("localDateTime.toString()", localDateTime.toString()) // "2024-07-31T20:00"
    
    val localDateTimeUtc = instant.toLocalDateTime(TimeZone.UTC)
    println("localDateTimeUtc.toString()", localDateTimeUtc.toString()) // "2024-07-31T12:00"
    
    val localDateTimeUtc8 = instant.toLocalDateTime(TimeZone.of("UTC+8"))
    println("localDateTimeUtc8.toString()", localDateTimeUtc8.toString()) // "2024-07-31T20:00"
    
    val localDateTimeAsiaIrkutsk = instant.toLocalDateTime(TimeZone.of("Asia/Irkutsk"))
    println("localDateTimeAsiaIrkutsk.toString()", localDateTimeAsiaIrkutsk.toString()) // "2024-07-31T20:00"
    
    println()
  }
  
  // LocalDateTime
  run {
    val localDateTime = LocalDateTime(
      year = 2024,
      month = Month.JULY,
      //monthNumber = 7, // another variant
      dayOfMonth = 31,
      hour = 11,
      minute = 10,
      second = 0,
      nanosecond = 0,
    )
    println("localDateTime.toString()", localDateTime.toString())
    
    val instantFromLocalDateTime = localDateTime.toInstant(TimeZone.of("Asia/Irkutsk"))
    
    val localDateTimeFromInstant = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+8"))
    println("localDateTimeFromInstant.toString()", localDateTimeFromInstant.toString())
    
    println()
  }
  
  // LocalDate
  run {
    val localDate = LocalDate(year = 2024, month = Month.JULY, dayOfMonth = 31)
    println("localDate.toString()", localDate.toString())
    
    val localDateFromInstant = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+8")).date
    println("localDateFromInstant.toString()", localDateFromInstant.toString())
    
    println()
  }
  
  // LocalTime
  run {
    val localTime = LocalTime(hour = 10, minute = 30, second = 15, nanosecond = 50)
    println("localTime.toString()", localTime.toString())
    
    val localTimeFromInstant = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+8")).time
    println("localTimeFromInstant.toString()", localTimeFromInstant.toString())
    
    println()
  }
  
  // parse
  run {
    // ВНИМАНИЕ!!! - в LocalDateTime например нельзя запихать строку-instant с таймзоной
    
    // Z is UTC time zone
    val instant = Instant.parse("2024-07-31T22:19:44.475Z")
    
    val localDateTime = LocalDateTime.parse("2024-07-31T22:19")
    
    val localDate = LocalDate.parse("2024-07-31")
    
    val localTime = LocalTime.parse("10:19:22.111")
  }
  
  // LocalDate.Format
  run {
    val dateFormat = LocalDate.Format {
      dayOfMonth() // need 2 digits
      char('.')
      monthNumber(Padding.NONE) // need 1 or 2 digits
      char('.')
      year() // need 4+ digits, there can be +/- before number
    }
    val localDate = LocalDate.parse("08.5.2024", format = dateFormat)
    println("parse 8.5.2024", localDate.toString())
    println()
  }
  
  // DateTimeComponents.Format
  // Using DateTimeComponents, we can extract any date and time components as needed
  run {
    val monthDay = DateTimeComponents.Format {
      dayOfMonth()
      char('-')
      monthName(MonthNames.ENGLISH_FULL)
    }.parse("31-July")
    
    // parse 31-July y: null, m: 7, d: 31
    println("parse 31-July", "y: ${monthDay.year}, m: ${monthDay.monthNumber}, d: ${monthDay.dayOfMonth}")
    
    val format = DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET
    //val instant = Instant.parse("2020-08-26T06:53:27.609+00:00", format)
    val instant = Clock.System.now()
    println("instant format", instant.format(format))
    
    println()
  }
  
  // Duration - difference between instants
  run {
    val instant = Instant.parse("2024-07-31T22:00:00.000Z")
    val olderInstant = Instant.parse("2024-03-15T22:00:00.000Z")
    val duration: Duration = instant - olderInstant
    
    println("duration.inWholeDays", duration.inWholeDays) // 138
    println("duration.inWholeNanoseconds", duration.inWholeNanoseconds) // 11923200000000000
    println()
  }
  
  // DateTimePeriod - calendar difference
  run {
    val instant = Instant.parse("2024-07-31T22:00:00.000Z")
    val olderInstant = Instant.parse("2022-03-15T12:05:01.050Z")
    val dateTimePeriod = olderInstant.periodUntil(instant, TimeZone.UTC)
    
    // This allows us to determine that there are
    // 2 years, 4 months, 16 days, 9 hours, 54 minutes, 58 seconds, and 950`000`000 nanoseconds
    // between our two instants.
    println("dateTimePeriod.years", dateTimePeriod.years) // 2
    println("dateTimePeriod.months", dateTimePeriod.months) // 4
    println("dateTimePeriod.days", dateTimePeriod.days) // 16
    println("dateTimePeriod.hours", dateTimePeriod.hours) // 9
    println("dateTimePeriod.minutes", dateTimePeriod.minutes) // 54
    println("dateTimePeriod.seconds", dateTimePeriod.seconds) // 58
    println("dateTimePeriod.nanoseconds", dateTimePeriod.nanoseconds) // 950000000
    println()
  }
  
  // TimeUnit - difference in provided time unit
  run {
    val instant = Instant.parse("2024-07-31T22:00:00.000Z")
    val olderInstant = Instant.parse("2024-03-15T22:00:00.000Z")
    val months = olderInstant.until(instant, DateTimeUnit.MONTH, TimeZone.UTC)
    val months2 = olderInstant.monthsUntil(instant, TimeZone.UTC)
    
    println("months", months) // 4
    println()
  }
  
  // Plus and Minus Functions
  run {
    val instant = Clock.System.now()
    
    val tenDaysFromNow: Instant = instant.plus(10, DateTimeUnit.DAY, TimeZone.UTC)
    val tenDaysBeforeNow: Instant = instant.minus(10, DateTimeUnit.DAY, TimeZone.UTC)
    
    val tenDaysFromNow2: Instant = instant + 10.days
    val tenDaysBeforeNow2: Instant = instant - 10.days
    
    val localDate = LocalDate(year = 2024, month = Month.JULY, dayOfMonth = 31)
    val oneYearFromLocalDate = localDate.plus(1, DateTimeUnit.YEAR)
    val oneYearBeforeLocalDate = localDate.minus(1, DateTimeUnit.YEAR)
    
    // To operate with LocalDateTime, we need to assign TimeZone
    val localDateTime = LocalDateTime(
      year = 2024,
      month = Month.JULY,
      dayOfMonth = 31,
      hour = 11,
      minute = 10,
      second = 0,
      nanosecond = 0,
    )
    val timeZone = TimeZone.of("Brazil/West")
    val localDateTimeInstant = localDateTime.toInstant(timeZone)
    val fiveMinutesAfter = localDateTimeInstant.plus(5, DateTimeUnit.MINUTE)
    val localDateTimeFiveMinutesAfter = fiveMinutesAfter.toLocalDateTime(timeZone)
  }
  
  
}

