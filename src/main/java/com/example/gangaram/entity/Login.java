package com.example.gangaram.entity;

import java.util.List;

public class Login {
    private String email;
    private List<String> exchanges;
    private List<String> products;
    private String broker;
    private String user_id;
    private String user_name;
    private List<String> order_types;
    private String user_type;
    private boolean poa;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getExchanges() {
        return exchanges;
    }

    public void setExchanges(List<String> exchanges) {
        this.exchanges = exchanges;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public List<String> getOrder_types() {
        return order_types;
    }

    public void setOrder_types(List<String> order_types) {
        this.order_types = order_types;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public boolean isPoa() {
        return poa;
    }

    public void setPoa(boolean poa) {
        this.poa = poa;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExtended_token() {
        return extended_token;
    }

    public void setExtended_token(String extended_token) {
        this.extended_token = extended_token;
    }

    private boolean is_active;
    private String access_token;
    private String extended_token;
}
