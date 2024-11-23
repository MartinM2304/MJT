package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.rmi.server.RemoteObjectInvocationHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DefaultUserProfile implements UserProfile {

    private String username;
    private Set<Interest> interestSet;
    Set<UserProfile> friendSet;
    private static Map<UserProfile, UserProfile> parent = new HashMap<>();


    public DefaultUserProfile(String username) {
        this.username = username;
        interestSet = new HashSet<>(7);
        friendSet = new HashSet<>();
    }
    @Override
    public int hashCode(){
        return Objects.hashCode(username);
    }

    @Override
    public boolean equals(Object obj){
        if(this==obj){
            return true;
        }
        if (obj == null || !(obj instanceof DefaultUserProfile)) {
            return false;
        }
        DefaultUserProfile other=(DefaultUserProfile) obj;
        return Objects.equals(username,other.username);
    }

    /**
     * Returns the username of the user.
     * Each user is guaranteed to have a unique username.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns an unmodifiable view of the user's interests.
     *
     * @return an unmodifiable view of the user's interests
     */
    public Collection<Interest> getInterests() {
        return Collections.unmodifiableSet(interestSet);
    }

    /**
     * Adds an interest to the user's profile.
     *
     * @param interest the interest to be added
     * @return true if the interest is newly added, false if the interest is already present
     * @throws IllegalArgumentException if the interest is null
     */
    public boolean addInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("Interest is null");
        }
        return interestSet.add(interest);
    }

    /**
     * Removes an interest from the user's profile.
     *
     * @param interest the interest to be removed
     * @return true if the interest is removed, false if the interest is not present
     * @throws IllegalArgumentException if the interest is null
     */
    public boolean removeInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("Interest is null");
        }
        return interestSet.remove(interest);
    }

    /**
     * Return unmodifiable view of the user's friends.
     *
     * @return an unmodifiable view of the user's friends
     */
    public Collection<UserProfile> getFriends() {
        return Collections.unmodifiableSet(friendSet);
    }

    public static UserProfile find(UserProfile user) {
        if (parent.get(user) == null||parent.get(user)==user) {
            return user;
        }

        return find(parent.get(user));
    }

    private static void union(UserProfile left, UserProfile right) {
        UserProfile leftParent = find(left);
        UserProfile rightParent = find(right);

        if(leftParent!=rightParent){
            parent.put(leftParent,rightParent);
        }
//        if (leftParent != rightParent || rightParent == null) {
//            parent.put(rightParent, leftParent);//leftParent becomes parent of right parent
//        }
//        if (leftParent == null) {
//            parent.put(leftParent, rightParent);
//        }
    }

    /**
     * Adds a user to the user's friends.
     *
     * @param userProfile the user to be added as a friend
     * @return true if the user is added, false if the user is already a friend
     * @throws IllegalArgumentException if the user is trying to add themselves as a friend,
     *                                  or if the user is null
     */
    public boolean addFriend(UserProfile userProfile) {
        if (userProfile == null|| userProfile==this) {
            throw new IllegalArgumentException("user profile is null");
        }

        //System.out.println(userProfile.getUsername());
        //union(this, userProfile);

        ((DefaultUserProfile)userProfile).friendSet.add(this);
        return friendSet.add(userProfile);
    }



    /**
     * Removes a user from the user's friends.
     *
     * @param userProfile the user to be removed
     * @return true if the user is removed, false if the user is not a friend
     * @throws IllegalArgumentException if the user is null
     */
    public boolean unfriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("user Profile is null");
        }
        if(!friendSet.contains(userProfile)){
            return false;
        }
        ((DefaultUserProfile)userProfile).friendSet.remove(this);
        return friendSet.remove(userProfile);
    }

    /**
     * Checks if a user is already a friend.
     *
     * @param userProfile the user to be checked
     * @return true if the user is a friend, false if the user is not a friend
     * @throws IllegalArgumentException if the user is null
     */
    public boolean isFriend(UserProfile userProfile) {
        if (userProfile == null) {
            //System.out.println("inside if");
            throw new IllegalArgumentException("user Profile is null");
        }

        //System.out.println("isFriend return");
        return friendSet.contains(userProfile);
    }

    public static void debug(){
        System.out.println("debug Start");
        for(UserProfile user:parent.keySet()){
            System.out.print(user.getUsername());
            System.out.print(" - ");
            UserProfile found=find(user);
            //if(found)
            System.out.println(find(user).getUsername());
        }
        System.out.println("debug END");
    }

}
