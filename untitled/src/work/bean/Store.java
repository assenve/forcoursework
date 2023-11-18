package work.bean;

import java.util.Objects;

public class Store {
    private String name;
    private String owner;
    private String details;
    private String contact;

    int id;

    @Override
    public String toString() {
        return "Store{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", details='" + details + '\'' +
                ", contact='" + contact + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return id == store.id && Objects.equals(name, store.name) && Objects.equals(owner, store.owner) && Objects.equals(details, store.details) && Objects.equals(contact, store.contact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner, details, contact, id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Store(String name, String owner, String details, String contact, int id) {
        this.name = name;
        this.owner = owner;
        this.details = details;
        this.contact = contact;
        this.id = id;
    }

    public Store() {
    }
}
