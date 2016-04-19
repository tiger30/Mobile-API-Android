[![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg?style=flat-square)](http://developer.android.com/index.html)
[![Version](https://img.shields.io/badge/version-1.2.0-brightgreen.svg?style=flat-square)](https://github.com/payleven/mPOS-SDK-Android/releases/tag/1.2.0)
[![API](https://img.shields.io/badge/API-14%2B-orange.svg?style=flat-square)](http://developer.android.com/about/versions/android-4.0.html)
[![Berlin](https://img.shields.io/badge/Made%20in-Berlin-red.svg?style=flat-square)](https://payleven.de/)

# payleven Point Pay API for Android

The PaylevenAppApi makes possible for app developers to open the payleven application from within their own apps and process payments. Although the payment is initiated on your app, it is the payleven application that takes care of handling the payment process. After a payment is processed, it will open your app and notify if the payment was successful, canceled or failed. 

#### Main Features
- Connects to payleven EMV/PCI certified card reader via bluetooth
- Accept all major card schemes such as Visa, Mastercard or American Express
- Provide immediate information about the payment status 
- Refund card payments
- Supports cash payment method
- Supports all main languages

#### Limitations
- Available only on the markets where [payleven](https://payleven.com/) operates
- Limited control on the UI 

### Prerequisites
* You or your client is operating in one of the countries supported by payleven.
* You are registered as a regular payleven user in a [payleven country](https://payleven.com/).
* You are registered as an integrator on the [developer page](https://service.payleven.com/uk/developer) for an unique API key.
* The iOS or Android payleven app is installed on the mobile device you want to use for accepting card payments.
* A payleven Classic (Chip & PIN) or Plus (NFC) terminal.
* Internet connection and geo location is available in your general use-case

### Table of Contents
* [Installation](#installation)
  * [Gradle](#gradle)
  * [Maven](#maven)
* [Setup your app](#setup-your-app)
* [Start payment](#start-payment)
* [Open transaction history](#open-transaction-history)
* [Open transaction details](#open-transaction-details)
* [Refund transaction](#refund-transaction)
* [Bluetooth Pairing](#bluetooth-pairing)
* [Documentation](#documentation)

### Installation
Include the Mobile API [file](https://github.com/payleven/Mobile-API-Android/blob/master/Example/app/libs/payleven-android-sdk-2.0.2.jar) to your project:
#### Gradle
1 - Create a 'libs' folder in the top level of the module directory.

2 - Copy the mobile API JAR file to the libs folder.

3 - In the module's build.gradle file add the following:
```
dependencies {
    compile files('libs/payleven-android-sdk-2.0.2.jar')
}
```
4 - Sync project with Gradle Files.
  
#### Maven
1 - Create a 'libs' folder in the top level of the module directory.

2 - Copy the mobile API JAR file to the libs folder.

3 - Right click on the Jar file and then select Build Path > Add to Build Path.


### Setup your app
Use the API key received from payleven to setup your app. Before doing payments you need to configure the API. In the following example replace "yourApiKey".
```
PaylevenApi.configure(yourApiKey);
```

### Start Payment
Below you see an example payment call to open the payleven app with an amount, a custom order ID, a description and a product picture. Once a payment is triggered and to ensure a stable flow, these values cannot be changed in the payleven app anymore, however you can cancel the payment and you will jump back to your app.
```
TransactionRequestBuilder builder = new TransactionRequestBuilder(amount, currency);
                    .setDescription(description)
                    .setBitmap(image);
                    
TransactionRequest request = builder.createTransactionRequest()
                            
PaylevenApi.initiatePayment(MainActivity.this, orderId, request);
```

### Open transaction history
To open the transaction history you have to call the openSalesHistory method. Below you see an example of the transaction history call to open the payleven App at the transaction history view.
```
PaylevenApi.openSalesHistory(MainActivity.this);
```

### Open transaction details
To open the details view of a transaction you have to call the openTransactionDetails method and set the order ID of such transaction. Below you see an example of this call:
```
PaylevenApi.openTransactionDetails(MainActivity.this, orderId);
```

### Refund transaction
It is also possible to refund a transaction. Calling the method openTransactionDetailsForRefund and providing an order ID will open a transaction details view with the option to refund the payment. Below you see an example of this call:
```
PaylevenApi.openTransactionDetailsForRefund(MainActivity.this, orderId);
```

### Bluetooth pairing
Before proceeding with the integration and testing, make sure you have paired the card reader.

1. Make sure the device is charged and turned on.
2. Press '0' key on the card reader for 5 sec and make sure the card reader has entered the pairing mode (there will be a corresponding sign on the screen).
3. Go to the payleven app, open the menu, select Account and tap on Default payment method. It will pop up a window where you should select Card.
4. In the same account screen, tap on Manage terminals and follow the app instructions.
      
### Documentation
[API Reference](http://payleven.github.io/Mobile-API-Android/1.2.0/javadoc)
      
