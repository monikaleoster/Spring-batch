package io.spring.chunk;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Line implements Serializable {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Line() {
    }

    public Line(String name, String age, String dob, String status) {
        this.name = name;
        this.dob = dob;
        this.age = age;
        this.status = status;
    }

    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Line{" +
                "name='" + name + '\'' +
                ", dob='" + dob + '\'' +
                ", age='" + age + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    private String name;
    private String dob;
    private String age;
    private String status;





}