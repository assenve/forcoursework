package work.bean;

import java.util.Objects;

public class Product {
    private String name;
    private String price;
    private String picture;


    private String details;
    private String quantity;
    private String store;
    private int id;

    private int order;

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", picture='" + picture + '\'' +
                ", details='" + details + '\'' +
                ", quantity='" + quantity + '\'' +
                ", store='" + store + '\'' +
                ", id=" + id +
                ", order=" + order +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && order == product.order && Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(picture, product.picture) && Objects.equals(details, product.details) && Objects.equals(quantity, product.quantity) && Objects.equals(store, product.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, picture, details, quantity, store, id, order);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Product(String name, String price, String picture, String details, String quantity, String store, int id, int order) {
        this.name = name;
        this.price = price;
        this.picture = picture;
        this.details = details;
        this.quantity = quantity;
        this.store = store;
        this.id = id;
        this.order = order;
    }

    public Product() {
    }
}
