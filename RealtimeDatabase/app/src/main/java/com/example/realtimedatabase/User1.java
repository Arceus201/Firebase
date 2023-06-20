package com.example.realtimedatabase;

import java.util.HashMap;
import java.util.Map;

public class User1 {
    private int id;
    private String name;

    public User1() {
    }

    public User1(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User1{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Map<String,Object> toMap(){
        HashMap<String,Object> result =  new HashMap<>();
        result.put("name",name );
        return result;
    }

}
