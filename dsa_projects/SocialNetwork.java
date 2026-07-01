

public class SocialNetwork 
    private Map<String, Set<String>> friends;
    private Map<String, String> bios;

    
        this.friends = new HashMap<>();
        this.bios = new HashMap<>();
        this.chatMessages = new HashMap<>();
    }

    // Add a new user
    public void addUser(String user, String bio) {
        if (!friends.containsKey(user)) {
            friends.put(user, new HashSet<>()); 
            bios.put(user, bio);
            chatMessages.put(user, new ArrayList<>());
            System.out.println("✅ User " + user + " added with bio: " + bio); 
            System.out.println("⚠️ User already exists!"); // Prevent duplicate users
        }
    }

    // Update bio
    public void updateBio(String user, String newBio) { //jdhvuehv
        if (bios.containsKey(user)) {
            bios.put(user, newBio);
            System.out.println("✅ Bio updated for " + user);
        } else {
            System.out.println("⚠️ User does not exist!");
        }
    }

    // Add a friend connection
    public void addFriend(String user1, String user2) {
        if (friends.containsKey(user1) && friends.containsKey(user2)) {
            friends.get(user1).add(user2);
            friends.get(user2).add(user1);
            System.out.println("✅ " + user1 + " and " + user2 + " are now friends!");
        } else {
            System.out.println("⚠️ One or both users do not exist.");
        }
    }

    // Remove a friend
    public void removeFriend(String user1, String user2) {
        if (friends.containsKey(user1) && friends.get(user1).contains(user2)) {
            friends.get(user1).remove(user2);
            friends.get(user2).remove(user1);
            System.out.println("✅ " + user1 + " and " + user2 + " are no longer friends.");
        } else {
            System.out.println("⚠️ They are not friends or user does not exist.");
        }
    }

    // Check if two users are friends
    public void checkIfFriends(String user1, String user2) {
        if (friends.containsKey(user1) && friends.get(user1).contains(user2)) {
            System.out.println("✅ " + user1 + " and " + user2 + " are friends.");
        } else {
            System.out.println("❌ They are not friends.");
        }
    }

    // Find mutual friends
    public void findMutualFriends(String user1, String user2) {
        if (friends.containsKey(user1) && friends.containsKey(user2)) {
            Set<String> mutual = new HashSet<>(friends.get(user1));
            mutual.retainAll(friends.get(user2));
            System.out.println("🤝 Mutual friends: " + mutual);
        } else {
            System.out.println("⚠️ One or both users do not exist.");
        }
    }

    // Suggest a friend (first-degree friends of friends)
    public void getFriendSuggestions(String user) {
        if (!friends.containsKey(user)) {
            System.out.println("⚠️ User does not exist.");
            return;
        }
        Set<String> suggestions = new HashSet<>();
        for (String friend : friends.get(user)) {
            for (String friendOfFriend : friends.get(friend)) {
                if (!friendOfFriend.equals(user) && !friends.get(user).contains(friendOfFriend)) {
                    suggestions.add(friendOfFriend);
                }
            }
        }
        System.out.println("✨ Friend suggestions: " + suggestions);
    }

    // Send a chat message
    public void sendMessage(String sender, String receiver, String message) {
        if (friends.containsKey(sender) && friends.containsKey(receiver)) {
            chatMessages.get(receiver).add(sender + ": " + message);
            System.out.println("📩 Message sent from " + sender + " to " + receiver);
        } else {
            System.out.println("⚠️ One or both users do not exist.");
        }
    }

    // View chat messages
    public void viewMessages(String user) {
        if (chatMessages.containsKey(user)) {
            System.out.println("📨 Messages for " + user + ": " + chatMessages.get(user));
        } else {
            System.out.println("⚠️ User does not exist.");
        }
    }

    // Display all users and their friends
    public void displayNetwork() {
        System.out.println("\n🌍 Social Network:");
        for (String user : friends.keySet()) {
            System.out.println("👤 " + user + " (" + bios.get(user) + ") -> " + friends.get(user));
        }
    }
}
