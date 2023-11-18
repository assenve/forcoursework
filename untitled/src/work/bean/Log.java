package work.bean;

import java.sql.Timestamp;
import java.util.Objects;

public class Log {
    private String customer;
    private String product;
    private Timestamp time;
    private String active;

    int id;

    @Override
    public String toString() {
        return "Log{" +
                "customer='" + customer + '\'' +
                ", product='" + product + '\'' +
                ", time=" + time +
                ", active='" + active + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
        return id == log.id && Objects.equals(customer, log.customer) && Objects.equals(product, log.product) && Objects.equals(time, log.time) && Objects.equals(active, log.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, product, time, active, id);
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Log(String customer, String product, Timestamp time, String active, int id) {
        this.customer = customer;
        this.product = product;
        this.time = time;
        this.active = active;
        this.id = id;
    }

    public Log() {
    }
}
