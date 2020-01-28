package pakzarzameen.com.pk.khalis.Utils;

import java.io.Serializable;

public class TimeContract implements Serializable {

    private double NewOrders;
    private double NewUsers;


    public TimeContract(){

    }

    public double getNewUsers() {
        return NewUsers;
    }

    public void setNewUsers(double newUsers) {
        NewUsers = newUsers;
    }

    public double getNewOrders() {
        return NewOrders;
    }

    public void setNewOrders(double newOrders) {
        NewOrders = newOrders;
    }
}
