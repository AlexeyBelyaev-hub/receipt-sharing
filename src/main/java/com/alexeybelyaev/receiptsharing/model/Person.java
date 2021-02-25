package com.alexeybelyaev.receiptsharing.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.UUID;

@Data
public class Person {
    private final UUID uuid;
    @NotBlank
    private String name;
    @Email
    private String email;

    private String phoneNumber;


    public Person(@JsonProperty("id") UUID uuid,
                  @JsonProperty("name") String name,
                  @JsonProperty("email") String email,
                  @JsonProperty("phoneNumber") String phoneNumber) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.email = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return uuid.equals(person.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
