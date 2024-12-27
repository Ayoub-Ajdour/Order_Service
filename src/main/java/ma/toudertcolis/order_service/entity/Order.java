package ma.toudertcolis.order_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.toudertcolis.order_service.dto.Hub;
import ma.toudertcolis.order_service.dto.Livreur;
import ma.toudertcolis.order_service.dto.Person;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "\"order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tracking_number")
    private String trackingNumber;

    @Transient
    private Person customer;
    @Column(name = "customer_id")
    private Long customerId;

    public Long getId() {
        return id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public Person getCustomer() {
        return customer;
    }

    public Long getCustomerId() {
        return customerId;
    }


    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public Status getStatus() {
        return status;
    }

    public Hub getHub() {
        return hub;
    }

    public void setHub(Hub hub) {
        this.hub = hub;
    }

    public String getPickupHubId() {
        return pickupHubId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public void setCustomer(Person customer) {
        this.customer = customer;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }



    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPickupHubId(String pickupHubId) {
        this.pickupHubId = pickupHubId;
    }





    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "pickup_hub_id")
    private String pickupHubId;
    @Transient
    private Hub hub;

    public enum Status {
        PENDING,
        IN_PROGRESS,
        COMPLETED
    }

    private String natureDeProduit;
    private String commentaire;

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getNatureDeProduit() {
        return natureDeProduit;
    }

    public void setNatureDeProduit(String natureDeProduit) {
        this.natureDeProduit = natureDeProduit;
    }

    private Double prix;

}

