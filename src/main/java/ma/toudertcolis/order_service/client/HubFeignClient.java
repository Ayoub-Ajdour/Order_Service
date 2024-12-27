package ma.toudertcolis.order_service.client;

import ma.toudertcolis.order_service.dto.Hub;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-management-service", url = "http://hub-management-service:8083")
public interface HubFeignClient {
    @GetMapping("/hubs/{id}")
    Hub getHubById(@PathVariable("id") String hub);
}
