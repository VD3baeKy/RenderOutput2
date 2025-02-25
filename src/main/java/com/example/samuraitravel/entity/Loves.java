package com.example.samuraitravel.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;

@Getter
@Entity
@Table(name = "loves")
@Data
public class Loves {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "house_id")
    private Integer houseid;
    
    @Column(name = "user_id")
    private Integer userid;
    
    /*
    @ManyToOne
    @JoinColumn(name = "houseid")
    private House house; 
    
    @ManyToOneA
    @JoinColumn(name = "userid")
    private User user;
    */
    
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

}
