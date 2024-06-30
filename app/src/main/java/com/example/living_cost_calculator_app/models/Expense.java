package com.example.living_cost_calculator_app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Expense implements Serializable {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("cost")
    private int cost;

    @SerializedName("creator")
    private String creator;

    @SerializedName("shared_with")
    private List<User> shared_with;

    @SerializedName("group")
    private String group;

    public Expense(String id, String name, int cost, String creator, List<User> shared_with, String group) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.creator = creator;
        this.shared_with = shared_with;
        this.group = group;
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public List<User> getShared_with() {
        return shared_with;
    }

    public void setShared_with(List<User> shared_with) {
        this.shared_with = shared_with;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
