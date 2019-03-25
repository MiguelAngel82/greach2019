package es.salenda.springboot.java.example;

import java.util.HashMap;
import java.util.Map;

public class BintrayConfiguration {

    public static final String PREFIX = "bintray";
    public static final String BINTRAY_API_URL = "https://bintray.com";

    private String apiversion;

    private String organization;

    private String repository;

    private String username;

    private String token;

    public String getApiversion() {
        return apiversion;
    }

    public void setApiversion(String apiversion) {
        this.apiversion = apiversion;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("apiversion", getApiversion());
        m.put("organization", getOrganization());
        m.put("repository", getRepository());
        m.put("username", getUsername());
        m.put("token", getToken());
        return m;
    }
}
