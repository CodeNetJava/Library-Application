package com.sau.library.service;

public class PaymentService {

    public String payDue(String userId){
        //call check due endpoint of order service
        //then pay due
        //send event to the order-service update the due amount
        //send event to the notification-service due paid and send email
    }

    public String payBill(String amount,String userId ){

    }
}
