package com.example.adelson_pc.a4shopping;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by facom on 23/10/17.
 */

public class ShopList {

    private String id;
    private String name;
    private String date;
    private String admin;
    private Map<String, User> members;
    private Map<String, Item> items;


    public ShopList() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public Map getMembers() {
        return members;
    }

    public void setMembers(Map<String, User> members) {
        this.members = members;
    }

    public void setMember(String key, User value) {
        if (members == null)
            members = new HashMap<String, User>();

        this.members.put(key, value);
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public void setItems(Map<String, Item> items) {
        this.items = items;
    }

    public void setItem(String key, Item item) {
        if (items == null)
            items = new HashMap<String, Item>();

        this.items.put(key, item);
    }

    @Override
    public String toString() {
        return name;
    }
}
