package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SocialFeedPost implements Post {

    private static long id = 0;
    private UserProfile author;
    private String content;
    private LocalDateTime timePublished;
    private Map<UserProfile, ReactionType> reactions;
    private String uniqueId;


    public SocialFeedPost(UserProfile author, String content) {
        if(content==null || author==null){
            throw  new IllegalArgumentException("SocialFeedPost is null");
        }
        this.author = author;
        this.content = content;
        timePublished = LocalDateTime.now();
        this.reactions = new HashMap<>();
        uniqueId = String.valueOf(id++);
    }

    /**
     * Returns the unique id of the post.
     * Each post is guaranteed to have a unique id.
     *
     * @return the unique id of the post
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * Returns the author of the post.
     *
     * @return the author of the post
     */
    public UserProfile getAuthor() {
        return author;
    }

    /**
     * Returns the date and time when the post was published.
     * A post is published once it is created.
     *
     * @return the date and time when the post was published
     */
    public LocalDateTime getPublishedOn() {
        return timePublished;
    }

    /**
     * Returns the content of the post.
     *
     * @return the content of the post
     */
    public String getContent() {
        return content;
    }

    /**
     * Adds a reaction to the post.
     * If the profile has already reacted to the post, the reaction is updated to the latest one.
     * An author of a post can react to their own post.
     *
     * @param userProfile  the profile that adds the reaction
     * @param reactionType the type of the reaction
     * @return true if the reaction is added, false if the reaction is updated
     * @throws IllegalArgumentException if the profile is null
     * @throws IllegalArgumentException if the reactionType is null
     */
    public boolean addReaction(UserProfile userProfile, ReactionType reactionType) {
        if (userProfile == null) {
            throw new IllegalArgumentException("userProfile is null");
        }
        if (reactionType == null) {
            throw new IllegalArgumentException("reactionType is null");
        }

        if (reactions.containsKey(userProfile)) {
            reactions.put(userProfile, reactionType);
            return false;
        }
        reactions.put(userProfile, reactionType);
        return true;
    }

    /**
     * Removes a reaction from the post.
     *
     * @param userProfile the profile that removes the reaction
     * @return true if the reaction is removed, false if the reaction is not present
     * @throws IllegalArgumentException if the profile is null
     */
    public boolean removeReaction(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("userProfile is null");
        }
        if (!reactions.containsKey(userProfile)) {
            return false;
        }
        reactions.remove(userProfile);
        return true;
    }

    /**
     * Returns all reactions to the post.
     * The returned map is unmodifiable.
     *
     * @return an unmodifiable view of all reactions to the post
     */
    public Map<ReactionType, Set<UserProfile>> getAllReactions() {
        Map<ReactionType, Set<UserProfile>> result = new HashMap<>();
        Set<UserProfile> users = reactions.keySet();

        for (UserProfile user : users) {
            ReactionType reaction = reactions.get(user);

            result.computeIfAbsent(reaction, k -> new HashSet<>()).add(user);
            //result.get(reaction).add(user);//TODO make it not to throw null exception
        }

        return Collections.unmodifiableMap(result);
    }

    /**
     * Returns the count of a specific reaction type to the post.
     *
     * @param reactionType the type of the reaction
     * @return the count of reactions of the specified type
     * @throws IllegalArgumentException if the reactionType is null
     */
    public int getReactionCount(ReactionType reactionType) {
        if(reactionType==null){
            throw new IllegalArgumentException("reactionType is null");
        }
        return Collections.frequency(reactions.values(), reactionType);
    }

    /**
     * Returns the total count of all reactions to the post.
     *
     * @return the total count of all reactions to the post
     */
    public int totalReactionsCount() {
        int sum = 0;

        for (ReactionType reaction : ReactionType.values()) {
            sum += getReactionCount(reaction);
        }

        return sum;
    }
}
