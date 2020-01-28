package pakzarzameen.com.pk.khalis.Utils;

import java.io.Serializable;

public class TimeContract implements Serializable {

    private double MilkQuantity;
    private double YogurtQuantity;


    public TimeContract(){

    }

    public double getYogurtQuantity() {
        return YogurtQuantity;
    }

    public void setYogurtQuantity(double yogurtQuantity) {
        YogurtQuantity = yogurtQuantity;
    }

    public double getMilkQuantity() {
        return MilkQuantity;
    }

    public void setMilkQuantity(double milkQuantity) {
        MilkQuantity = milkQuantity;
    }
}
