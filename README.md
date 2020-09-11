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

### Database

As Database, you can use any Database, just specify it in the application.yml file

### Ether Wallet

Last but not least, you need a on the Identity Contract registered ETH Address to store the Identity proofs on chain.

For this you need to provide the path and password of your wallet file in the application.yml file


The Required properties are:
```
spring:
    datasource:
        driver-class-name: 
        url: *Databse-Url*
        username: *Username*
        password: *Password*
    jpa:
        show-sql: true
        hibernate:
            ddl-auto: update
            properties:
                hibernate:
                    dialect: org.hibernate.dialect.MySQL5Dialect
    mail:
        host: *host*
        port: *port*
        username: *mail username*
        password: *mail password*
        properties:
            mail:
                smtp:
                    auth: true
                    starttls:
                        enable: true
                        required: true
  servlet:
    multipart:
      max-file-size: 20MB
      max-request-size: 20MB
aws:
  similarityThreshold: *wanted Similarity*
  access-key: *AWS KEY*
  secret-key: *AWS Secret*
twilioAccountSid: *Twilio Account Sid*
twilioAuthToken: *Twilio Auth Token*
twiliophoneNumber: *Twilio Phone Number*
IdentityContractAddress: *'Address of Identity Contract'*
BlockchainPath: *HTTP Addres used to connect to Eth Net*
BlockchainPrivatKey: *Private Key of Approver Account*
TesseractDataPath: *Path to Teseract OCR Model for Image Processing*
server:
    port: *Port you want to Serv*
```

