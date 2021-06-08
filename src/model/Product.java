package model;

public class Product {

    int id;
    String name;
    double purchasedPrice;
    double soldPrice;
    String expirationDate;
    String category;

    public Product(int id, String name, double purchasedPrice, double soldPrice, String expirationDate, String category) {
        this.id = id;
        this.name = name;
        this.purchasedPrice = purchasedPrice;
        this.soldPrice = soldPrice;
        this.expirationDate = expirationDate;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPurchasedPrice() {
        return purchasedPrice;
    }

    public void setPurchasedPrice(double purchasedPrice) {
        this.purchasedPrice = purchasedPrice;
    }

    public double getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(double soldPrice) {
        this.soldPrice = soldPrice;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
