package ma.toudertcolis.order_service.dto.Delivery;

import lombok.Data;
import ma.toudertcolis.order_service.dto.Livreur;
import ma.toudertcolis.order_service.dto.Person;

import java.time.LocalDateTime;

@Data
public class Delivery {
    private String _id;
    private String trackingNumber;

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    private LocalDateTime pickupTime;
    private LocalDateTime deliveryTime;
    private String status;
    private Long deliveryPersonId;
    private Livreur deliveryPerson;
    private Long customerId;
    private Person customer;
    private String uid;
    private Double prix;

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public Livreur getDeliveryPerson() {
        return deliveryPerson;
    }

    public void setDeliveryPerson(Livreur deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setCustomer(Person customer) {
        this.customer = customer;
    }

    public Person getCustomer() {
        return customer;
    }
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }





    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getDeliveryPersonId() {
        return deliveryPersonId;
    }

    public void setDeliveryPersonId(Long deliveryPersonId) {
        this.deliveryPersonId = deliveryPersonId;
    }
    public enum Status {
        IN_TRANSIT,
        DELIVERED
    }

}
