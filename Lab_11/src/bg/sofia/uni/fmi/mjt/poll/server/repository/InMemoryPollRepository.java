package bg.sofia.uni.fmi.mjt.poll.server.repository;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InMemoryPollRepository implements PollRepository {
    //can use concurrent hashmap
    private final Map<Integer, Poll> polls = new HashMap<>();
    private int nextId = 1;

    @Override
    public synchronized int addPoll(Poll poll) {
        int id = nextId++;
        polls.put(id, poll);
        return id;
    }

    @Override
    public synchronized Poll getPoll(int pollId) {
        return polls.get(pollId);
    }

    @Override
    public synchronized Map<Integer, Poll> getAllPolls() {
        return Collections.unmodifiableMap(polls);
    }

    @Override
    public synchronized void clearAllPolls() {
        polls.clear();
    }
}
