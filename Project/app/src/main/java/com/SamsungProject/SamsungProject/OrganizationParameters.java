package com.SamsungProject.SamsungProject;

import java.util.ArrayList;
import java.util.Arrays;

public class OrganizationParameters {
    private String name;
    private String type;
    private String description;
    private String website;
    private String players;
    private String ex_players;

    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public String getDescription() {
        return description;
    }
    public String getWebsite() {
        return website;
    }
    public String getPlayers() {
        return players;
    }

    public String getEx_players() {
        return ex_players;
    }

    OrganizationParameters(String name, String description, String type, String website){
        this.name = name;
        this.type = type;
        this.description = description;
        this.website = website;
    }
    OrganizationParameters(String name, String description,
                           String players, String type, String website, String ex_players){
        this.name = name;
        this.type = type;
        this.description = description;
        this.website = website;
        this.players = players;
        this.ex_players = ex_players;
    }
}
