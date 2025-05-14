package com.example.proiect;

public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private String birthdate;
    private String city;
    private String gender;
    private String interestedIn;
    private String bio;
    private String profileImageUrl;
    private double latitude;
    private double longitude;
    private int likesCount;

    // Constructor gol
    public User() {}

    // Constructor complet
    public User(String name, String email, String password, String birthdate, String city,
                String gender, String interestedIn, String bio, String profileImageUrl,
                double latitude, double longitude) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthdate = birthdate;
        this.city = city;
        this.gender = gender;
        this.interestedIn = interestedIn;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.likesCount = 0;
    }

    // Getters si Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getInterestedIn() { return interestedIn; }
    public void setInterestedIn(String interestedIn) { this.interestedIn = interestedIn; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }
}
