package model;


public class Invoice {

    int id;
    String supplier;
    int productsCount;
    double totalSum;

    public Invoice(int id, String supplier, int productsCount, double totalSum) {
        this.id = id;
        this.supplier = supplier;
        this.productsCount = productsCount;
        this.totalSum = totalSum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public int getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(int productsCount) {
        this.productsCount = productsCount;
    }

    public double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
    }
}

