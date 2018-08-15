# mt-demo

Demo app for money transfer. Built with akka-http

To run it execute `sbt/run` 

### POST /create
    {
        "name": "Jane Doe",
        "amount": 200
    }
    
Response
    
    {
        "id": "4a0b9978-a004-11e8-98d0-529269fb1459"
        "name": "Jane Doe",
        "amount": 200
    }
     

### POST /transfer
 
    {
        "from": "4a0b9978-a004-11e8-98d0-529269fb1459",
        "to": "4a0b9978-a004-11e8-98d0-529269fb1460",
        "amount": 200
    }
    
Response

    {
        "from": "4a0b9978-a004-11e8-98d0-529269fb1459",
        "to": "4a0b9978-a004-11e8-98d0-529269fb1460",
        "amount": 200
    }
    
Possible errors

    {
        "message": "Amount can't be negative"
    }   
    
    {
        "message": "Transaction can't be negative or more than account balance"
    } 

 
 