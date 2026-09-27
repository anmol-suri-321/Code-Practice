package Practice.LLD.LLDQuestions.AICoding.scheduler.repository;

import Practice.LLD.LLDQuestions.AICoding.scheduler.model.Job;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class JobRepository {

    private final ConcurrentHashMap<String, Job> store = new ConcurrentHashMap<>();

    public void addJob(Job job) {
        store.put(job.jobId(), job);
    }

    public void updateJob(Job job) {
        store.put(job.jobId(), job);
    }

    public void removeJob(String jobId) {
        store.remove(jobId);
    }

    public Optional<Job> getJob(String jobId) {
        return Optional.ofNullable(store.get(jobId));
    }

    public Collection<Job> getAllJobs() {
        return Collections.unmodifiableCollection(store.values());
    }
}
