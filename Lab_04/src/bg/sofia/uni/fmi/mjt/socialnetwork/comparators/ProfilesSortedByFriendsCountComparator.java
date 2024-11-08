package bg.sofia.uni.fmi.mjt.socialnetwork.comparators;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.Comparator;

public class ProfilesSortedByFriendsCountComparator implements Comparator<UserProfile> {
    @Override
    public int compare(UserProfile first, UserProfile second){

        return (Integer.compare(first.getFriends().size(),second.getFriends().size()));
    }
}
