package pakzarzameen.com.pk.khalis.Utils;

import java.io.Serializable;
import java.util.List;

public class FbContract implements Serializable {
    private Long TimeStamp;
    private double MilkQuantity;
    private double YogurtQuantity;
    private String OrderType;
    private String ScheduleType;
    private String Address;
    private List<String> Days;

    public FbContract() {

    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }

    public double getMilkQuantity() {
        return MilkQuantity;
    }

    public void setMilkQuantity(double milkQuantity) {
        MilkQuantity = milkQuantity;
    }

    public double getYogurtQuantity() {
        return YogurtQuantity;
    }

    public void setYogurtQuantity(double yogurtQuantity) {
        YogurtQuantity = yogurtQuantity;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getScheduleType() {
        return ScheduleType;
    }

    public void setScheduleType(String scheduleType) {
        ScheduleType = scheduleType;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public List<String> getDays() {
        return Days;
    }

    public void setDays(List<String> days) {
        Days = days;
    }
}