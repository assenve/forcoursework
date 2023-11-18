package work.bean;

import java.util.Objects;

public class Order {
    private String picture;
    private String product;

    private String id;
    private float price;
    private int quantity;
    private float income;

    @Override
    public String toString() {
        return "Order{" +
                "picture='" + picture + '\'' +
                ", product='" + product + '\'' +
                ", id='" + id + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", income=" + income +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Float.compare(order.price, price) == 0 && quantity == order.quantity && Float.compare(order.income, income) == 0 && Objects.equals(picture, order.picture) && Objects.equals(product, order.product) && Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(picture, product, id, price, quantity, income);
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public Order(String picture, String product, String id, float price, int quantity, float income) {
        this.picture = picture;
        this.product = product;
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.income = income;
    }

    public Order() {
    }
}
