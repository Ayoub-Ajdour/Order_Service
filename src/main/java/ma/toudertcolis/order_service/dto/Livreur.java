package ma.toudertcolis.order_service.dto;


import lombok.Builder;
import lombok.Data;

@Builder @Data
public class Livreur {
    private Long id;
    private String name;
    private String cityCode;
    private String role;
    private Double numberCommands;

    public Double getNumberCommands() {
        return numberCommands;
    }

    public void setNumberCommands(Double numberCommands) {
        this.numberCommands = numberCommands;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String phone_number;
    private String location;

}
