package com.example.samuraitravel.repository;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonDeserialize
public class ReviewHouseDTO {
    private Review review;
    private House house;
    private User user;
    
    public ReviewHouseDTO(Review review, House house, User user) {
        this.review = review;
        this.house = house;
        this.user = user;
    }

    // ゲッターとセッタ
    @Override
    public String toString() {
    	System.out.println("   ***** ReviewHouseDTO = review:  " + review);
    	System.out.println("   ***** ReviewHouseDTO = house:  " + house);
    	System.out.println("   ***** ReviewHouseDTO = user:  " + user);
        return "ReviewHouseDTO{" +
                "review=" + review +
                ", house=" + house +
                ", user=" + user +
                '}';
    }
    
    public Review getReview() {
        return review;
    }

    public House getHouse() {
        return house;
    }
    
    public User getUser() {
        return user;
    }

}
