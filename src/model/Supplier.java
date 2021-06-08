package model;

public class Supplier {

    public int id;
    public String supplierName;
    public String phoneNumber;
    public String wilaya;

    public Supplier(String supplierName, String phoneNumber, String wilaya) {
        this.supplierName = supplierName;
        this.phoneNumber = phoneNumber;
        this.wilaya = wilaya;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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
