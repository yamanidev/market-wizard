package model;

public class Supplier {

    public String supplier;
    public String phoneNumber;
    public String wilaya;
//    public int id;


    public Supplier(String supplier, String phoneNumber, String wilaya) {
        this.supplier = supplier;
        this.phoneNumber = phoneNumber;
        this.wilaya = wilaya;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWilaya() {
        return wilaya;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }
}
