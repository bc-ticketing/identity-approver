# identity-approver
service for approving identity claims and store those on the Ethereum blockchain

This is a Sample Implementation of our Identity Approover

It lets you register a Phone Number or an Email Adress as your Identity proof. It then sends a secret to the registerd Identity. 

This secret must then be signed using the Ether Address the Identtiy should be linked to.

When sending this signed secret to the Identity Approver, it will approve your Identity by including your Ether Adress in the Identity Smart Contract. Only the Ethe Address is stored on chain. All the other Data is kept in the Backend.

To Send Emails, you need acces to a Mailserver. The credential have to be specified in the application.yml file

To Send SMS, the Services of Twillio are used. Therefore you need to create an Account with Twillio. Add your Tokens to the application.yml file.

As Database, you can use any Database, just specify it in the application.yml file

Last but not least, you need a on the Identity registerd ETH Adress to store the Identity proofs on chain.

For this you need to provide the path and password of your wallet file in the application.yml file


The Required propperties are:
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

twilioAccountSid: *Twilio Account Sid*
twilioAuthToken: *Twilio Auth Token*
twiliophoneNumber: *Twilio Phone Number*
IdentityContractAddress: *'Address of Identity Contract'*
BlockchainPath: *HTTP Addres used to connect to Eth Net*
BlockchainWalletPath: *Path to Wallet File*
BlockchainWalletPSWD: *Wallet Password*
server:
    port: *Port you want to Serv*
```

