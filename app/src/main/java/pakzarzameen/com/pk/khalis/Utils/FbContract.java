package pakzarzameen.com.pk.khalis.Utils;

import java.io.Serializable;
import java.util.List;

public class FbContract implements Serializable {
    private Long TimeStamp;
    private Float BuffaloMilkQuantity;
    private Float CowMilkQuantity;
    private Float YogurtQuantity;
    private Float ButterQuantity;
    private Float GheeQuantity;
    private Float Total;
    private String OrderType;
    private String ScheduleType;
    private String Address;
    private List<String> Days;
    private String status;
    private String Payment;
    private String Name;
    private String ButterDetail;
    private String GheeDetail;

    public FbContract() {

    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }

    public Float getBuffaloMilkQuantity() {
        return BuffaloMilkQuantity;
    }

    public void setBuffaloMilkQuantity(Float milkQuantity) {
        BuffaloMilkQuantity = milkQuantity;
    }

    public Float getTotal() {
        return Total;
    }

    public void setTotal(Float sum) {
        Total = sum;
    }

    public Float getCowMilkQuantity() {
        return CowMilkQuantity;
    }

    public void setCowMilkQuantity(Float milkQuantity) {
        CowMilkQuantity = milkQuantity;
    }

    public Float getYogurtQuantity() {
        return YogurtQuantity;
    }

    public void setYogurtQuantity(Float Quantity) {
        YogurtQuantity = Quantity;
    }

    public Float getButterQuantity() {
        return ButterQuantity;
    }

    public void setButterQuantity(Float Quantity) {
        ButterQuantity = Quantity;
    }

    public Float getGheeQuantity() {
        return GheeQuantity;
    }

    public void setGheeQuantity(Float Quantity) {
        GheeQuantity = Quantity;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment() {
        return Payment;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getButterDetail() {
        return ButterDetail;
    }

    public void setButterDetail(String detail) {
        ButterDetail = detail;
    }

    public String getGheeDetail() {
        return GheeDetail;
    }

    public void setGheeDetail(String detail) {
        GheeDetail = detail;
    }
}