package work.bean;

import java.util.Objects;

public class Merchant {
    private String account;
    private String password;
    private String email;
    private String store;
    int id;

    @Override
    public String toString() {
        return "Merchant{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", store='" + store + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Merchant merchant = (Merchant) o;
        return id == merchant.id && Objects.equals(account, merchant.account) && Objects.equals(password, merchant.password) && Objects.equals(email, merchant.email) && Objects.equals(store, merchant.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, password, email, store, id);
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

    public Merchant(String account, String password, String email, String store, int id) {
        this.account = account;
        this.password = password;
        this.email = email;
        this.store = store;
        this.id = id;
    }

    public Merchant() {
    }
}


