package DSA.CourseSchedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class CourseScheduleIII {
    public int scheduleCourse(int[][] courses) {
        List<Course> li = new ArrayList<>();
        for (int i = 0; i < courses.length; i++) {
            li.add(new Course(courses[i][0], courses[i][1]));
        }

        Collections.sort(li, (a, b) -> {
            return a.endTime - b.endTime;
        });
        // Max-Heap to store the durations of the courses taken
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a,b) -> b-a);

        int totalDuration = 0;
        for (Course c : li) {
            totalDuration += c.duration;
            maxHeap.add(c.duration);
            // If total time exceeds the current endTime, we need to swap
            if (totalDuration > c.endTime) {
            // Remove the course with the longest Duration which taken
                totalDuration -= maxHeap.poll();
            }
        }
        return maxHeap.size();
    }
}

class Course {
    int duration;
    int endTime;

    Course(int duration, int endTime) {
        this.duration = duration;
        this.endTime = endTime;
    }
}
