package Demo.AICoding.scheduler.schedule;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;

/**
 * Schedule backed by a cron expression in two supported formats:
 *
 *   5-field UNIX  (minute-level):  minute hour dom month dow
 *   6-field Quartz (second-level): second minute hour dom month dow
 *
 * Field syntax: * | value | range (n-m) | step (*\/n, n-m\/n) | list (a,b,c)
 *
 * Examples:
 *   "0 1 * * *"       — daily at 01:00  (5-field)
 *   "* /30 * * * * *" — every 30 seconds (6-field)
 */
public class CronSchedule implements Schedule {

    private final String expression;
    private final boolean hasSeconds;

    private final Set<Integer> seconds;
    private final Set<Integer> minutes;
    private final Set<Integer> hours;
    private final Set<Integer> daysOfMonth;
    private final Set<Integer> months;
    private final Set<Integer> daysOfWeek;

    public CronSchedule(String expression) {
        this.expression = expression;
        String[] fields = expression.trim().split("\\s+");

        if (fields.length == 6) {
            hasSeconds   = true;
            seconds      = new CronFieldParser(0, 59).parse(fields[0]);
            minutes      = new CronFieldParser(0, 59).parse(fields[1]);
            hours        = new CronFieldParser(0, 23).parse(fields[2]);
            daysOfMonth  = new CronFieldParser(1, 31).parse(fields[3]);
            months       = new CronFieldParser(1, 12).parse(fields[4]);
            daysOfWeek   = new CronFieldParser(0,  6).parse(fields[5]);
        } else if (fields.length == 5) {
            hasSeconds   = false;
            seconds      = null;
            minutes      = new CronFieldParser(0, 59).parse(fields[0]);
            hours        = new CronFieldParser(0, 23).parse(fields[1]);
            daysOfMonth  = new CronFieldParser(1, 31).parse(fields[2]);
            months       = new CronFieldParser(1, 12).parse(fields[3]);
            daysOfWeek   = new CronFieldParser(0,  6).parse(fields[4]);
        } else {
            throw new IllegalArgumentException(
                "Expected 5-field (UNIX) or 6-field (Quartz) cron expression but got: " + expression);
        }
    }

    @Override
    public Optional<Instant> nextExecutionTime(Instant from) {
        return hasSeconds ? nextWithSeconds(from) : nextWithMinutes(from);
    }

    @Override
    public boolean isRecurring() {
        return true;
    }

    public String getExpression() {
        return expression;
    }

    // ---- private helpers ----

    private Optional<Instant> nextWithSeconds(Instant from) {
        ZonedDateTime candidate = from.atZone(ZoneOffset.UTC)
                .plusSeconds(1)
                .truncatedTo(ChronoUnit.SECONDS);

        ZonedDateTime limit = candidate.plusYears(4);

        while (candidate.isBefore(limit)) {
            if (!months.contains(candidate.getMonthValue())) {
                candidate = candidate.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                continue;
            }
            int unixDow = candidate.getDayOfWeek().getValue() % 7;
            if (!daysOfMonth.contains(candidate.getDayOfMonth()) || !daysOfWeek.contains(unixDow)) {
                candidate = candidate.plusDays(1).withHour(0).withMinute(0).withSecond(0);
                continue;
            }
            if (!hours.contains(candidate.getHour())) {
                candidate = candidate.plusHours(1).withMinute(0).withSecond(0);
                continue;
            }
            if (!minutes.contains(candidate.getMinute())) {
                candidate = candidate.plusMinutes(1).withSecond(0);
                continue;
            }
            if (!seconds.contains(candidate.getSecond())) {
                candidate = candidate.plusSeconds(1);
                continue;
            }
            return Optional.of(candidate.toInstant());
        }
        return Optional.empty();
    }

    private Optional<Instant> nextWithMinutes(Instant from) {
        ZonedDateTime candidate = from.atZone(ZoneOffset.UTC)
                .plusMinutes(1)
                .truncatedTo(ChronoUnit.MINUTES);

        ZonedDateTime limit = candidate.plusYears(4);

        while (candidate.isBefore(limit)) {
            if (!months.contains(candidate.getMonthValue())) {
                candidate = candidate.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0);
                continue;
            }
            int unixDow = candidate.getDayOfWeek().getValue() % 7;
            if (!daysOfMonth.contains(candidate.getDayOfMonth()) || !daysOfWeek.contains(unixDow)) {
                candidate = candidate.plusDays(1).withHour(0).withMinute(0);
                continue;
            }
            if (!hours.contains(candidate.getHour())) {
                candidate = candidate.plusHours(1).withMinute(0);
                continue;
            }
            if (!minutes.contains(candidate.getMinute())) {
                candidate = candidate.plusMinutes(1);
                continue;
            }
            return Optional.of(candidate.toInstant());
        }
        return Optional.empty();
    }
}
