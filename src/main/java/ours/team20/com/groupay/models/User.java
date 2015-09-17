package ours.team20.com.groupay.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {
    private String id;
    private String name;
    private String URL;
    private ArrayList<String> groups = new ArrayList<String>();
    private ArrayList<Notification> notifications = new ArrayList<Notification>();
    private ArrayList<Payment> payments = new ArrayList<Payment>();
    private ArrayList<Event> events = new ArrayList<Event>();

    public User(String id, String name, String email){
        this.id = id;
        this.name = name;
        this.URL = email;
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

    public String getURL() {
        return URL;
    }

    public void setURL(String email) {
        this.URL = email;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<Payment> payments) {
        this.payments = payments;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", id);
            jsonObject.put("name", name);
            jsonObject.put("URL", URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
