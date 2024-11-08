package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.comparators.ProfilesSortedByFriendsCountComparator;
import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.DefaultUserProfile;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.io.Console;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SocialNetworkImpl implements SocialNetwork {

    private Set<UserProfile> users = new HashSet<>();
    private Set<Post> posts = new HashSet<>();

    /**
     * Registers a user in the social network.
     *
     * @param userProfile the user profile to register
     * @throws IllegalArgumentException  if the user profile is null
     * @throws UserRegistrationException if the user profile is already registered
     */
    public void registerUser(UserProfile userProfile) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile is null");
        }

        if (users.contains(userProfile)) {
            throw new UserRegistrationException("User is already registered");
        }

        users.add(userProfile);
    }

    /**
     * Returns all the registered users in the social network.
     *
     * @return unmodifiable set of all registered users (empty one if there are none).
     */
    public Set<UserProfile> getAllUsers() {
        return Collections.unmodifiableSet(users);
    }

    /**
     * Posts a new post in the social network.
     *
     * @param userProfile the user profile that posts the content
     * @param content     the content of the post
     * @return the created post
     * @throws UserRegistrationException if the user profile is not registered
     * @throws IllegalArgumentException  if the user profile is null
     * @throws IllegalArgumentException  if the content is null or empty
     */
    public Post post(UserProfile userProfile, String content) throws UserRegistrationException {
        if (userProfile == null || content == null||content.isBlank()) {
            throw new IllegalArgumentException("null passed");
        }
        if (!users.contains(userProfile)) {
            throw new UserRegistrationException("User is not registered");
        }
        if(content.isBlank()){
            System.out.println("empty contents");
        }
        Post post = new SocialFeedPost(userProfile, content);
        posts.add(post);
        return post;
    }

    /**
     * Returns all posts in the social network.
     *
     * @return unmodifiable collection of all posts (empty one if there are none).
     */
    public Collection<Post> getPosts() {
        return Collections.unmodifiableSet(posts);
    }

    public Set<UserProfile> getReachedUsers(UserProfile toBeReached) {
        Set<UserProfile> result=new HashSet<>();
        UserProfile parent= DefaultUserProfile.find(toBeReached);

        for(UserProfile user: users){
            UserProfile userParent=DefaultUserProfile.find(user);
            System.out.println(userParent.getUsername());
            if(userParent==parent){
                result.add(user);
            }
        }

        return result;
    }

    public Set<UserProfile> getReachedUsers(Collection<Interest> interestSet) {
        Set<UserProfile> result = new HashSet<>();

        for (UserProfile user : users) {
            Set<Interest> intersection = new HashSet<>(user.getInterests());
            intersection.retainAll(interestSet);

            if (!intersection.isEmpty()){
                result.add(user);
            }
        }

        return result;
    }

    /**
     * Returns a collection of unique user profiles that can see the specified post in their feed. A
     * user can view a post if both of the following conditions are met:
     * <ol>
     *     <li>The user has at least one common interest with the author of the post.</li>
     *     <li>The user has the author of the post in their network of friends.</li>
     * </ol>
     * <p>
     * Two users are considered to be in the same network of friends if they are directly connected
     * (i.e., they are friends) or if there exists a chain of friends connecting them.
     * </p>
     *
     * @param post The post for which visibility is being determined
     * @return A set of user profiles that meet the visibility criteria (empty one if there are none).
     * @throws IllegalArgumentException if the post is <code>null</code>.
     */
    public Set<UserProfile> getReachedUsers(Post post) {
        if(post==null){
            throw new IllegalArgumentException("post is null");
        }
        Set<UserProfile> usersWithSameInterest = getReachedUsers(post.getAuthor().getInterests());
        Set<UserProfile> friends=getReachedUsers(post.getAuthor());

        System.out.printf("Users with same interest: %d\n",usersWithSameInterest.size());
        System.out.printf("Users union: %d\n",friends.size());

        usersWithSameInterest.retainAll(friends);
        usersWithSameInterest.remove(post.getAuthor());

        return usersWithSameInterest;

    }

    /**
     * Returns a set of all mutual friends between the two users.
     *
     * @param userProfile1 the first user profile
     * @param userProfile2 the second user profile
     * @return a set of all mutual friends between the two users or an empty set if there are no
     * mutual friends
     * @throws UserRegistrationException if any of the user profiles is not registered
     * @throws IllegalArgumentException  if any of the user profiles is null
     */
    public Set<UserProfile> getMutualFriends(UserProfile userProfile1, UserProfile userProfile2)
            throws UserRegistrationException {

        if(userProfile1==null || userProfile2==null){
            throw  new IllegalArgumentException("users are null");
        }
        if((!users.contains(userProfile1)) ||(!users.contains(userProfile2))){
            throw  new UserRegistrationException("user is not registered");
        }
        Set<UserProfile> intersecion=new HashSet<>(userProfile1.getFriends());
        intersecion.retainAll(userProfile2.getFriends());

        return intersecion;
    }

    /**
     * Returns a sorted set of all user profiles ordered by the number of friends they have in
     * descending order.
     *
     * @return a sorted set of all user profiles ordered by the number of friends they have in
     * descending order
     */
    public SortedSet<UserProfile> getAllProfilesSortedByFriendsCount() {
        SortedSet<UserProfile> result= new TreeSet<>(new ProfilesSortedByFriendsCountComparator());
        result.addAll(users);

        return result;
    }
}
