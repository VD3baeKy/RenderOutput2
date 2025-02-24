package com.example.samuraitravel.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Loves;
import com.example.samuraitravel.entity.User;
//import com.example.samuraitravel.form.ReservationRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.LovesRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service
public class LovesService {
    private final LovesRepository lovesRepository;  
    private final HouseRepository houseRepository;  
    private final UserRepository userRepository;  
    
    public LovesService(LovesRepository lovesRepository, HouseRepository houseRepository, UserRepository userRepository) {
        this.lovesRepository = lovesRepository;  
        this.houseRepository = houseRepository;  
        this.userRepository = userRepository;  
    }    
    
    @Transactional
     //public void create(ReservationRegisterForm reservationRegisterForm) {
     public void create(Map<String, String> lovesObject) {
        Loves loves = new Loves();
        
         Integer houseId = Integer.valueOf(lovesObject.get("houseId"));
         Integer userId = Integer.valueOf(lovesObject.get("userId"));
        
         //House house = houseRepository.getReferenceById(reservationRegisterForm.getHouseId());
         House house = houseRepository.getReferenceById(houseId);       
         //User user = userRepository.getReferenceById(reservationRegisterForm.getUserId());
         User user = userRepository.getReferenceById(userId);
                
         loves.setHouse(house);
         loves.setUser(user);
        
        lovesRepository.save(loves);
    }    
    
   

}