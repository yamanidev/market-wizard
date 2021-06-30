package model;

public class Supplier {

    private int id;
    private String supplierName;
    private String phoneNumber;
    private String address;
    private String registry;
    private String nis;
    private String nif;

    public Supplier(int id, String supplierName,
                    String phoneNumber, String address,
                    String registry, String nis,
                    String nif) {
        this.id = id;
        this.supplierName = supplierName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.registry = registry;
        this.nis = nis;
        this.nif = nif;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public Supplier(int id, String supplierName, String phoneNumber, String address, String nis, String nif) {
        this.id = id;
        this.supplierName = supplierName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.nis = nis;
        this.nif = nif;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }
}
