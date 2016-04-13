[![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg?style=flat-square)](http://developer.android.com/index.html)
[![Version](https://img.shields.io/badge/version-1.2.0-brightgreen.svg?style=flat-square)](https://github.com/payleven/mPOS-SDK-Android/releases/tag/1.2.0)
[![API](https://img.shields.io/badge/API-14%2B-orange.svg?style=flat-square)](http://developer.android.com/about/versions/android-4.0.html)
[![Berlin](https://img.shields.io/badge/Made%20in-Berlin-red.svg?style=flat-square)](https://payleven.de/)

# payleven Mobile API

This project provides an Android API that allows app developers to process payments through the payleven application. The Mobile API provides the calls to open the payleven app from your own application and process the payments.

### Prerequisites

##### Step 1 - Create a merchant account
To process a payment you need to login in the payleven app with your payleven account. You can create an account by registering on our [website](http://payleven.com). Make sure to register for the country you wish.

##### Step 2 - Create a developer account
To grant your application access to payleven’s Mobile API you need to [register](https://service.payleven.com/uk/developer) and receive an API key. Please keep in mind that you should use your final app specifications (e.g bundle-ID for iOS or package name for Android) during the registration since these will be used in combination with your API key to identify your app.

##### Step 3 - Install the payleven app
Althought the payment is initiated in your app; the actual transaction takes place within the payleven app. For this reason, the payleven app must be installed on the mobile device you wish to use for accepting card payments.

##### Step 4 - Purchase card reader
To accept card payments, you need to purchase a card reader. This is possible on our [website](http://payleven.com) during the registration or in your payleven account after registration. For testing purposes a card reader is not necessarily needed as the transaction flow can be tested using another payment methods: e.g. cash.

### Installation
Include the Mobile API [file](https://github.com/payleven/Mobile-API-Android/blob/master/Example/app/libs/payleven-android-sdk-2.0.2.jar) to your project:
###### Gradle
1 - Create a 'libs' folder in the top level of the module directory.

2 - Copy the mobile API JAR file to the libs folder.

3 - In the module's build.gradle file add the following:
```
dependencies {
    compile files('libs/payleven-android-sdk-2.0.2.jar')
}
```
4 - Sync project with Gradle Files.
  
###### Maven
1 - Create a 'libs' folder in the top level of the module directory.

2 - Copy the mobile API JAR file to the libs folder.

3 - Right click on the Jar file and then select Build Path > Add to Build Path.

### Getting started
#### Setup your app
Use the API key received from payleven to setup your app. Before doing payments you need to configure the API. In the following example replace "yourApiKey".
```
PaylevenApi.configure(yourApiKey);
```

#### Start Payment
Below you see an example payment call to open the payleven app with an amount, a custom order ID, a description and a product picture.
```
TransactionRequestBuilder builder = new TransactionRequestBuilder(amount, currency);
                    .setDescription(description)
                    .setBitmap(image);
                    
TransactionRequest request = builder.createTransactionRequest()
                            
PaylevenApi.initiatePayment(MainActivity.this, orderId, request);
```

#### Open transaction history
To open the transaction history you have to call the openSalesHistory method. Below you see an example of the transaction history call to open the payleven App at the transaction history view.
```
PaylevenApi.openSalesHistory(MainActivity.this);
```

#### Open transaction details
To open the details view of a transaction you have to call the openTransactionDetails method and set the order ID of such transaction. Below you see an example of this call:
```
PaylevenApi.openTransactionDetails(MainActivity.this, orderId);
```

#### Refund transaction
It is also possible to refund a transaction. Calling the method openTransactionDetailsForRefund and providing an order ID will open a transaction details view with the option to refund the payment. Below you see an example of this call:
```
PaylevenApi.openTransactionDetailsForRefund(MainActivity.this, orderId);
```

      
#### Documentation
[API Reference](http://payleven.github.io/Mobile-API-Android/1.2.0/javadoc)
      
