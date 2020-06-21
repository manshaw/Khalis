# Summary 
This app provides Milk Delivery System. Users can order dairy products using this app and the order will be delivered at their doorstep at user specified time. Users can order from a list of products and can see indiviual and total price before ordering. Users can also see a history of thier previous orders. The main website for this app is here [Khaalis](https://khaalis.web.app/).

## Userflow
Userflow is simple and can be divided into following steps,
1. User login via Firebase Phone Authentication in which OTP is sent via SMS.
1. After login, dashboard appers which has 2 options 'Previous Orders' and 'Current Orders'
1. In Current Orders Page, user can add order by selecting items with desired quantity from the list. Then user is asked about order type. Currently the app supports 2 order type 'Schedule' and 'One-Time'. Then user enters his address and time of delivery and finally places his order.
1. In Previous Order Page, user can see his ongoing and past orders as well. 

## Features
This app is developed by keeping in mind the requirments of Milk Delivery System,
* OTP based Phone Authentication from Firebase 
* Orders history
* Invoice generation
* Order Types
* Multi-Language support

## Firebase
We are using following firebase features,
* Authentication
* Database
* Crashlytics

### Authentication
User enters his phone number with country code and soon he receives OTP (One Time Password) via message which is Auto Verified and the user is redirected to App Dashboard.

### Database
Database has simple structure in which root node is user's phone number and it has 2 child nodes 'Current Orders' and 'Previous Orders'.
Each order node has further child nodes having details about the order. Simple structure is given below,

```
UID
 |__CURRENT ORDERS
    |__NAME
    |__ADDRESS
    |__PRODUCT_1
    |__PRODUCT_2.....
    |__ORDER TYPE
    |__STATUS
    |__PAYMENT
    |__TIMESTAMP
    |__TOTAL
 |__PREVIOUS ORDERS
    |__NAME
    |__ADDRESS
    |__PRODUCT_1
    |__PRODUCT_2.....
    |__ORDER TYPE
    |__STATUS
    |__PAYMENT
    |__TIMESTAMP
    |__TOTAL
```




