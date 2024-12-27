package ma.toudertcolis.order_service.service;

import ma.toudertcolis.order_service.client.HubFeignClient;
import ma.toudertcolis.order_service.client.PersonFeignClient;
import ma.toudertcolis.order_service.dto.Delivery.Delivery;
import ma.toudertcolis.order_service.dto.Hub;
import ma.toudertcolis.order_service.dto.Livreur;
import ma.toudertcolis.order_service.dto.Person;
import ma.toudertcolis.order_service.entity.Order;
import ma.toudertcolis.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PersonFeignClient personFeignClient;
    @Autowired
    private HubFeignClient hubFeignClient;

    private final RestTemplate restTemplate;

    @Autowired
    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Delivery createOrder(Order order) {
        String trackingNumber = generateTrackingNumber(hubFeignClient.getHubById(order.getPickupHubId()), personFeignClient.getUserById(order.getCustomerId()));
        order.setTrackingNumber(trackingNumber);
        Order savedOrder = orderRepository.save(order);

        // Fetch customer city code and available delivery persons
        String customerCityCode = getCustomerById(savedOrder.getCustomerId()).block().getCityCode();
        List<Livreur> deliveryPersons = getAllLivreurs().block();
        List<Livreur> availableDeliveryPersons = deliveryPersons.stream()
                .filter(p -> "DELIVERY_PERSON".equals(p.getRole()) && customerCityCode.equals(p.getCityCode()))
                .collect(Collectors.toList());

        Livreur selectedDeliveryPerson = availableDeliveryPersons.stream()
                .min(Comparator.comparingDouble(p -> p.getNumberCommands()))
                .orElseThrow(() -> new RuntimeException("No available delivery person"));

        selectedDeliveryPerson.setNumberCommands(selectedDeliveryPerson.getNumberCommands() + 1);
        personFeignClient.updateUser(selectedDeliveryPerson.getId(), selectedDeliveryPerson);

        // Create Delivery object
        Delivery newDelivery = new Delivery();
        newDelivery.set_id("deliverie_" + savedOrder.getId());
        newDelivery.setTrackingNumber(savedOrder.getTrackingNumber());
        newDelivery.setPickupTime(savedOrder.getOrderDate());
        newDelivery.setDeliveryPersonId(selectedDeliveryPerson.getId());
        newDelivery.setStatus(Delivery.Status.IN_TRANSIT.toString());
        newDelivery.setCustomerId(savedOrder.getCustomerId());
        newDelivery.setCustomer(getCustomerById(savedOrder.getCustomerId()).block());
        newDelivery.setDeliveryPerson(selectedDeliveryPerson);
        // Save delivery in your MongoDB (assuming the repository exists)
//        deliveryRepository.save(newDelivery);

        return newDelivery;
    }


    private String generateTrackingNumber(Hub hub, Person customer) {
        String hubId = hub != null && hub.get_id() != null ? hub.get_id() : "UNKNOWN";
        String cityCode = customer != null && customer.getCityCode() != null ? customer.getCityCode() : "XXX";

        // Generate a random 10-character alphanumeric string
        String randomPart = RandomStringUtils.randomAlphanumeric(10).toUpperCase();

        // Construct the tracking number
        return hubId + "-" + cityCode + randomPart;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Mono<Livreur> getLivreurById(Long id) {
        HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder()
                .url("http://flask-service:8081/graphql")
                .build();

        String httpRequestDocument = """
            query($id: Int!) {
              userById(id: $id) {
                id
                name
                phoneNumber
                location
                cityCode
                role
              }
            }
            """;

        return graphQlClient.document(httpRequestDocument)
                .variable("id", id)
                .retrieve("userById")
                .toEntity(Livreur.class);
    }

    public Mono<List<Livreur>> getAllLivreurs() {
        HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder()
                .url("http://flask-service:8081/graphql")
                .build();

        String httpRequestDocument = """
        query {
          allUsers {
            id
            name
            phoneNumber
            location
            cityCode
            role
            numberCommands
          }
        }
        """;

        return graphQlClient.document(httpRequestDocument)
                .retrieve("allUsers")
                .toEntityList(Livreur.class)
                .onErrorResume(error -> {
                    // Handle any error that occurs during the request
                    System.err.println("Error fetching data: " + error.getMessage());
                    return Mono.just(Collections.emptyList());
                })
                .doOnNext(livreurs -> {
                    // Optionally log or process the fetched data
                    System.out.println("Fetched livreurs: " + livreurs);
                });
    }


    public Mono<Person> getCustomerById(Long id) {
        HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder()
                .url("http://flask-service:8081/graphql")
                .build();

        String httpRequestDocument = """
        query($id: Int!) {
          userById(id: $id) {
            id
            name
            email
            phoneNumber
            address
            cityCode
          }
        }
        """;

        System.out.println("GraphQL query: " + httpRequestDocument);
        System.out.println("Variable id: " + id);

        return graphQlClient.document(httpRequestDocument)
                .variable("id", id)
                .retrieve("userById")
                .toEntity(Person.class)
                .doOnError(e -> System.out.println("😒👀Error: " + e.getMessage())); // Log the error

    }


    public Optional<Order> getOrderById(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setHub(hubFeignClient.getHubById(order.getPickupHubId()));
        order.setCustomer(personFeignClient.getUserById(order.getCustomerId()));
        return Optional.of(order);
    }


    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            order.setHub(hubFeignClient.getHubById(order.getPickupHubId()));
            order.setCustomer(getCustomerById(order.getCustomerId()).block());
        }
        return orders;    }
}
