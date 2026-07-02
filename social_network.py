from typing import Dict, List, Set


class SocialNetwork:
    def __init__(self) -> None:
        self.friends: Dict[str, Set[str]] = {}
        self.bios: Dict[str, str] = {}
        self.chat_messages: Dict[str, List[str]] = {}

    def add_user(self, user: str, bio: str) -> None:
        if user not in self.friends:
            self.friends[user] = set()
            self.bios[user] = bio
            self.chat_messages[user] = []
            print(f"✅ User {user} added with bio: {bio}")
        else:
            print("⚠️ User already exists!")

    def update_bio(self, user: str, new_bio: str) -> None:
        if user in self.bios:
            self.bios[user] = new_bio
            print(f"✅ Bio updated for {user}")
        else:
            print("⚠️ User does not exist!")

    def add_friend(self, user1: str, user2: str) -> None:
        if user1 in self.friends and user2 in self.friends:
            self.friends[user1].add(user2)
            self.friends[user2].add(user1)
            print(f"✅ {user1} and {user2} are now friends!")
        else:
            print("⚠️ One or both users do not exist.")

    def remove_friend(self, user1: str, user2: str) -> None:
        if user1 in self.friends and user2 in self.friends[user1]:
            self.friends[user1].remove(user2)
            self.friends[user2].remove(user1)
            print(f"✅ {user1} and {user2} are no longer friends.")
        else:
            print("⚠️ They are not friends or user does not exist.")

    def check_if_friends(self, user1: str, user2: str) -> None:
        if user1 in self.friends and user2 in self.friends[user1]:
            print(f"✅ {user1} and {user2} are friends.")
        else:
            print("❌ They are not friends.")

    def find_mutual_friends(self, user1: str, user2: str) -> None:
        if user1 in self.friends and user2 in self.friends:
            mutual = self.friends[user1].intersection(self.friends[user2])
            print(f"🤝 Mutual friends: {mutual}")
        else:
            print("⚠️ One or both users do not exist.")

    def get_friend_suggestions(self, user: str) -> None:
        if user not in self.friends:
            print("⚠️ User does not exist.")
            return

        suggestions: Set[str] = set()
        for friend in self.friends[user]:
            for friend_of_friend in self.friends.get(friend, set()):
                if friend_of_friend != user and friend_of_friend not in self.friends[user]:
                    suggestions.add(friend_of_friend)

        print(f"✨ Friend suggestions: {suggestions}")

    def send_message(self, sender: str, receiver: str, message: str) -> None:
        if sender in self.chat_messages and receiver in self.chat_messages:
            self.chat_messages[receiver].append(f"{sender}: {message}")
            print(f"📩 Message sent from {sender} to {receiver}")
        else:
            print("⚠️ One or both users do not exist.")

    def view_messages(self, user: str) -> None:
        if user in self.chat_messages:
            print(f"📨 Messages for {user}: {self.chat_messages[user]}")
        else:
            print("⚠️ User does not exist.")

    def display_network(self) -> None:
        print("\n🌍 Social Network:")
        for user, friends in self.friends.items():
            bio = self.bios.get(user, "")
            print(f"👤 {user} ({bio}) -> {friends}")


def main() -> None:
    social_network = SocialNetwork()

    while True:
        print("\nSocial Network Menu:")
        print("1. Add User")
        print("2. Update Bio")
        print("3. Add Friend")
        print("4. Remove Friend")
        print("5. Check if Friends")
        print("6. Find Mutual Friends")
        print("7. Get Friend Suggestions")
        print("8. Send Chat Message")
        print("9. View Messages")
        print("10. Display Social Network")
        print("11. Exit")

        choice = input("Enter your choice: ").strip()

        if choice == "1":
            user = input("Enter username: ").strip()
            bio = input("Enter bio: ").strip()
            social_network.add_user(user, bio)
        elif choice == "2":
            user = input("Enter username: ").strip()
            new_bio = input("Enter new bio: ").strip()
            social_network.update_bio(user, new_bio)
        elif choice == "3":
            user1 = input("Enter first user: ").strip()
            user2 = input("Enter second user: ").strip()
            social_network.add_friend(user1, user2)
        elif choice == "4":
            user1 = input("Enter first user: ").strip()
            user2 = input("Enter second user: ").strip()
            social_network.remove_friend(user1, user2)
        elif choice == "5":
            user1 = input("Enter first user: ").strip()
            user2 = input("Enter second user: ").strip()
            social_network.check_if_friends(user1, user2)
        elif choice == "6":
            user1 = input("Enter first user: ").strip()
            user2 = input("Enter second user: ").strip()
            social_network.find_mutual_friends(user1, user2)
        elif choice == "7":
            user = input("Enter username: ").strip()
            social_network.get_friend_suggestions(user)
        elif choice == "8":
            sender = input("Enter sender: ").strip()
            receiver = input("Enter receiver: ").strip()
            message = input("Enter message: ").strip()
            social_network.send_message(sender, receiver, message)
        elif choice == "9":
            user = input("Enter username: ").strip()
            social_network.view_messages(user)
        elif choice == "10":
            social_network.display_network()
        elif choice == "11":
            print("👋 Exiting...")
            break
        else:
            print("❌ Invalid choice! Try again.")


if __name__ == "__main__":
    main()
