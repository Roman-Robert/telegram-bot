# telegram-bot

# Telegram bot for an English language school.

This bot allows all users of the telegram application to subscribe to the distribution of materials (photos, documents) that will be useful when learning English.

Sending messages can be done by the developer and the administrator of the school by sending a message to the chat bot. After that, the bot will send this message to all subscribed users.
When subscribing, the chat collects the following information about the userEntity:

	- Name
	- Surname
	- Username (in the telegram application)
	- Date and time of subscription

To stop receiving messages from the school through the chatbot, there is an unsubscribe function that completely removes the userEntity and his data from the database.


Feature:
1. Code refactoring
2. English level test
3. Add logging in a file