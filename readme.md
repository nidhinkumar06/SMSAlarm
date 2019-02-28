## READING SMS AND STARTING ALARM BASED ON STRING MATCHING


#### ABOUT

The smsalarm application will read the messages from the inbox and display them in a list box.
If the user clicks a message it will check whether the message has matched with the string Alarm.
If the string gets matched then the alarm will be set after one minute from the current time

#### TECHNOLOGIES USED

- FRONT END:  ANDROID (JAVA & XML)

- The process used to fetch messages from inbox as well as scheduling an alarm is the Broadcast receiver

#### WHAT IS BROADCAST RECEIVER?

A broadcast receiver is an Android component which allows you to send or receive Android system or application events. All the registered application are notified by the Android runtime once the event happens.

For example, applications can register for various system events like boot complete or battery low, and Android system sends broadcast when a specific event occurs. Any application can also create its own custom broadcasts.

![alt text](https://smsalarm.page.link/rniX)

#### SMS Receiver

![alt text](https://firebasestorage.googleapis.com/v0/b/reactflashcards-45cbc.appspot.com/o/smsalarm%2Fbroadcast.png?alt=media&token=75ceedfa-02cc-4301-9092-e3c8ba3766b6)

#### Alarm Receiver

![alt text](https://firebasestorage.googleapis.com/v0/b/reactflashcards-45cbc.appspot.com/o/smsalarm%2Falarmreceiver.png?alt=media&token=8b0dc4a6-5a35-45f6-b95d-6391a91d73c3)

#### Playing Audio

![alt text](https://firebasestorage.googleapis.com/v0/b/reactflashcards-45cbc.appspot.com/o/smsalarm%2Fplaysound.png?alt=media&token=b98db76c-aae4-42d2-8dfc-63d75dc16032)

#### Screen Shots

![alt text](https://firebasestorage.googleapis.com/v0/b/reactflashcards-45cbc.appspot.com/o/smsalarm%2Fphoto_2019-02-27_17-23-54.jpg?alt=media&token=763ff96e-2e22-46cb-95dc-efa8dbf040cf)

![alt text](https://firebasestorage.googleapis.com/v0/b/reactflashcards-45cbc.appspot.com/o/smsalarm%2Fphoto_2019-02-27_17-24-02.jpg?alt=media&token=d1a3fa5a-f538-4b2f-b3ae-652e04ea0949)


### LEARNINGS:


1. Since the Broadcast receiver has been deprecated from 2018, it is not possible to show the live changes (Once an SMS is received it will not be updated immediately in the list).

2. By default, the priority for the incoming messages is given to the default messaging app once the message is displayed in the messaging app after that it will be shown in the app which we have created.

3. There is a new option called SMS Retrieval API which will read the message automatically from the inbox and display the message in the app.

4. SMS Retrieval API does not require permissions like READ_SMS or RECEIVE_SMS whereas the old one requires permission.

#### LINKS:

https://developers.google.com/identity/sms-retriever/request

#### APK LINK

https://smsalarm.page.link/Wcr6
