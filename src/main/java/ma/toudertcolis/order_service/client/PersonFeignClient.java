package ma.toudertcolis.order_service.client;

import ma.toudertcolis.order_service.dto.Livreur;
import ma.toudertcolis.order_service.dto.Person;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "flask-service", url = "http://flask-service:8081")
public interface PersonFeignClient {
    @GetMapping("/users/{id}")
    Person getUserById(@PathVariable("id") Long personID);
    @GetMapping("/users")
    List<Person> getUsers();

    @PutMapping("/users/{id}")
    Livreur updateUser(@PathVariable("id") Long id, @RequestBody Livreur person);
}
