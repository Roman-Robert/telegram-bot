# Telegram bot for an English language school.

This bot allows all users of the telegram application to subscribe to the distribution of materials (photos, documents) that will be useful when learning English.

Sending messages can be done by the developer and the administrator of the school by sending a message to the chat bot. After that, the bot will send this message to all subscribed users.
When subscribing, the bot collects the following information about the User:

	- ChatId
	- Username
	- Firstname
    - Lastname

And sets values:
    
    - Date and time of subscription
    - English level = 0
    - Quantity of free lessons = 0
    - Activity marker = "YES"

To stop receiving messages from the school through the bot, there is an unsubscribe function that marks the User as inactive.


Feature:
1. Code refactoring
2. Logic with levels after English level test pass
3. Logging in a file on a server
4. Unit and integration tests coverage