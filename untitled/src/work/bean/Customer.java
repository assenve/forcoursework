package work.bean;

import java.util.Objects;

public class Customer {
    private String account;
    private String password;
    private String email;

    private int id;

    @Override
    public String toString() {
        return "Customer{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id && Objects.equals(account, customer.account) && Objects.equals(password, customer.password) && Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, password, email, id);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer(String account, String password, String email, int id) {
        this.account = account;
        this.password = password;
        this.email = email;
        this.id = id;
    }

    public Customer() {
    }
}
