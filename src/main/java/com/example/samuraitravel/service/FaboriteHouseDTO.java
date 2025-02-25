package com.example.samuraitravel.repository;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Loves;


public class FaboriteHouseDTO {
    private Loves loves;
    private House house;
    
    //@Autowired
    public FaboriteHouseDTO(Loves loves, House house) {
        this.loves = loves;
        this.house = house;
    }

    // ゲッターとセッタ
    @Override
    public String toString() {
    	System.out.println("   ***** FaboriteHouseDTO = loves:  " + loves);
    	System.out.println("   ***** FaboriteHouseDTO = house:  " + house);
        return "ReviewHouseDTO{" +
                "loves=" + loves +
                ", house=" + house +
                '}';
    }
    
    public Loves getLoves() {
        return loves;
    }

    public House getHouse() {
        return house;
    }
    

}
