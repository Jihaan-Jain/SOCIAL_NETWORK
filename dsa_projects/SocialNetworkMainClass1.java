package dsa_projects;

import java.util.Scanner;

public class SocialNetworkMainClass1 {
    public static void main(String[] args) {
        SocialNetwork socialNetwork = new SocialNetwork();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nSocial Network Menu:");
            System.out.println("1. Add User");
            System.out.println("2. Update Bio");
            System.out.println("3. Add Friend");
            System.out.println("4. Remove Friend");
            System.out.println("5. Check if Friends");
            System.out.println("6. Find Mutual Friends");
            System.out.println("7. Get Friend Suggestions");
            System.out.println("8. Send Chat Message");
            System.out.println("9. View Messages");
            System.out.println("10. Display Social Network");
            System.out.println("11. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String user = scanner.nextLine();
                    System.out.print("Enter bio: ");
                    String bio = scanner.nextLine();
                    socialNetwork.addUser(user, bio);
                    break;

                case 2:
                    System.out.print("Enter username: ");
                    String updateUser = scanner.nextLine();
                    System.out.print("Enter new bio: ");
                    String newBio = scanner.nextLine();
                    socialNetwork.updateBio(updateUser, newBio);
                    break;

                case 3:
                    System.out.print("Enter first user: ");
                    String user1 = scanner.nextLine();
                    System.out.print("Enter second user: ");
                    String user2 = scanner.nextLine();
                    socialNetwork.addFriend(user1, user2);
                    break;

                case 4:
                    System.out.print("Enter first user: ");
                    user1 = scanner.nextLine();
                    System.out.print("Enter second user: ");
                    user2 = scanner.nextLine();
                    socialNetwork.removeFriend(user1, user2);
                    break;

                case 5:
                    System.out.print("Enter first user: ");
                    user1 = scanner.nextLine();
                    System.out.print("Enter second user: ");
                    user2 = scanner.nextLine();
                    socialNetwork.checkIfFriends(user1, user2);
                    break;

                case 6:
                    System.out.print("Enter first user: ");
                    user1 = scanner.nextLine();
                    System.out.print("Enter second user: ");
                    user2 = scanner.nextLine();
                    socialNetwork.findMutualFriends(user1, user2);
                    break;

                case 7:
                    System.out.print("Enter username: ");
                    user = scanner.nextLine();
                    socialNetwork.getFriendSuggestions(user);
                    break;

                case 8:
                    System.out.print("Enter sender: ");
                    String sender = scanner.nextLine();
                    System.out.print("Enter receiver: ");
                    String receiver = scanner.nextLine();
                    System.out.print("Enter message: ");
                    String message = scanner.nextLine();
                    socialNetwork.sendMessage(sender, receiver, message);
                    break;

                case 9:
                    System.out.print("Enter username: ");
                    user = scanner.nextLine();
                    socialNetwork.viewMessages(user);
                    break;

                case 10:
                    socialNetwork.displayNetwork();
                    break;

                case 11:
                    System.out.println("👋 Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("❌ Invalid choice! Try again.");
            }
        }
    }
}
