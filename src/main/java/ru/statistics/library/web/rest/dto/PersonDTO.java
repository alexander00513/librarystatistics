package ru.statistics.library.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Person entity.
 */
public class PersonDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 128)
    private String firstName;

    @NotNull
    @Size(max = 128)
    private String lastName;

    private String email;

    private String phoneNumber;


    private Set<LibraryDTO> libraries = new HashSet<>();

    private Set<EventDTO> events = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<LibraryDTO> getLibraries() {
        return libraries;
    }

    public void setLibraries(Set<LibraryDTO> libraries) {
        this.libraries = libraries;
    }

    public Set<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(Set<EventDTO> events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersonDTO personDTO = (PersonDTO) o;

        if ( ! Objects.equals(id, personDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PersonDTO{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", email='" + email + "'" +
            ", phoneNumber='" + phoneNumber + "'" +
            '}';
    }
}
