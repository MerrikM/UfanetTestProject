package com.pool.poolcrud.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ClientDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String patronymic;

    @NotBlank
    private String number;

    @Email
    private String email;

    @Min(1)
    //private int maxNumberOfVisitsPerDay;

    public ClientDTO(String name, String surname, String patronymic, String number, String email) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.number = number;
        this.email = email;
        //this.maxNumberOfVisitsPerDay = maxNumberOfVisitsPerDay;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public int getMaxNumberOfVisitsPerDay() {
//        return maxNumberOfVisitsPerDay;
//    }
//
//    public void setMaxNumberOfVisitsPerDay(int maxNumberOfVisitsPerDay) {
//        this.maxNumberOfVisitsPerDay = maxNumberOfVisitsPerDay;
//    }
}
