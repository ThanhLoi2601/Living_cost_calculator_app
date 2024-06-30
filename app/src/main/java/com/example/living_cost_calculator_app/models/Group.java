package com.example.living_cost_calculator_app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Group implements Serializable {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("creator")
    private String creator;

    @SerializedName("users")
    private List<String> users;

    public Group(String id, String name, String creator, List<String> users) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.users = users;
    }

    public Group(String name, String creator, List<String> users) {
        this.name = name;
        this.creator = creator;
        this.users = users;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public static class group_user implements Serializable{
        @SerializedName("groupId")
        private String groupId;

        @SerializedName("userIds")
        private List<String> userIds;

        public group_user(String groupId, List<String> userIds) {
            this.groupId = groupId;
            this.userIds = userIds;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public List<String> getUserIds() {
            return userIds;
        }

        public void setUserIds(List<String> userIds) {
            this.userIds = userIds;
        }
    }

}
