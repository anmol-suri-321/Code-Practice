package Practice.LLD.LLDQuestions.AICoding.scheduler.schedule;

import java.util.TreeSet;
import java.util.Set;

/**
 * Parses a single UNIX cron field (e.g. "*", "0-5", "* /15", "1,15,30") into the
 * concrete set of integers it matches. min/max define the allowed range for the field.
 */
class CronFieldParser {

    private final int min;
    private final int max;

    CronFieldParser(int min, int max) {
        this.min = min;
        this.max = max;
    }

    Set<Integer> parse(String field) {
        Set<Integer> values = new TreeSet<>();
        for (String part : field.split(",")) {
            part = part.trim();
            if (part.contains("/")) {
                parseStep(part, values);
            } else if (part.contains("-")) {
                parseRange(part, values);
            } else if ("*".equals(part)) {
                for (int i = min; i <= max; i++) values.add(i);
            } else {
                values.add(Integer.parseInt(part));
            }
        }
        return values;
    }

    private void parseStep(String part, Set<Integer> values) {
        String[] slashParts = part.split("/");
        int step = Integer.parseInt(slashParts[1]);
        int start = min;
        int end = max;
        if (!"*".equals(slashParts[0])) {
            if (slashParts[0].contains("-")) {
                String[] bounds = slashParts[0].split("-");
                start = Integer.parseInt(bounds[0]);
                end = Integer.parseInt(bounds[1]);
            } else {
                start = Integer.parseInt(slashParts[0]);
            }
        }
        for (int i = start; i <= end; i += step) values.add(i);
    }

    private void parseRange(String part, Set<Integer> values) {
        String[] bounds = part.split("-");
        int from = Integer.parseInt(bounds[0]);
        int to = Integer.parseInt(bounds[1]);
        for (int i = from; i <= to; i++) values.add(i);
    }
}
