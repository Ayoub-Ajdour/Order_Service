package ma.toudertcolis.order_service.controller;

import ma.toudertcolis.order_service.dto.Delivery.Delivery;
import ma.toudertcolis.order_service.entity.Order;
import ma.toudertcolis.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Optional<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public ResponseEntity<Delivery> createOrder(@RequestBody Order order) {
        try {
            Delivery delivery = orderService.createOrder(order);
            return ResponseEntity.ok(delivery); // Return the created delivery
        } catch (Exception e) {
            e.printStackTrace(); // or log the error
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create order", e);
        }
    }


    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
