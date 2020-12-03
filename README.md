# identity-approver

This is a Sample Implementation of our Identity Approver
## Purpose

It lets you register a Phone Number or an Email Address as your Identity proof. It then sends a secret to the registerd Identity. 
This secret must then be signed using the Ether Address the Identity should be linked to.

When sending this signed secret to the Identity Approver, it will approve your Identity by including your Ether Adress in the Identity Smart Contract. Only the Ethe Address is stored on chain. All the other Data is kept in the Backend.

### Sending Mails

To Send Emails, you need access to a Mailserver. The credential have to be specified in the application.yml file

### Sending SMS

To Send SMS, the Services of [Twillio](https://www.twilio.com/sms) are used. Therefore you need to create an Account with Twillio. Add your Tokens to the application.yml file.

### Validating Person with ID Card and Selfie

To Validate the MRZ of the ID Card, Teseract is used. Pleas specify the path of the Tesseract Model.
To check, if the Selfie and the picture on the ID is from the same Person, AWS Rekognition is used.
Provide the AWS User credentials in the Application.yml file.

### Database

As Database, you can use any Database, just specify it in the application.yml file

### Ether Wallet

Last but not least, you need a on the Identity Contract registered ETH Address to store the Identity proofs on chain.

For this you need to provide the path and password of your wallet file in the application.yml file


