package com.example.samuraitravel.controller;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.samuraitravel.entity.Loves;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.LovesRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReservationService;
import com.example.samuraitravel.service.StripeService;



@Controller

public class LovesController {

    private final LovesRepository lovesRepository;   

    private final HouseRepository houseRepository;

    private final ReservationService reservationService; 

     private final StripeService stripeService; 

    

     //public ReservationController(ReservationRepository reservationRepository, HouseRepository houseRepository, ReservationService reservationService) { 

     public LovesController(LovesRepository lovesRepository, HouseRepository houseRepository, ReservationService reservationService, StripeService stripeService) { 

        this.lovesRepository = lovesRepository; 

        this.houseRepository = houseRepository;

        this.reservationService = reservationService;

         this.stripeService = stripeService;

    }    



    @GetMapping("/loves")

    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {

        User user = userDetailsImpl.getUser();

        Page<Loves> lovesnPage = lovesRepository.findByUserOrderByCreatedAtDesc(user, pageable);

        model.addAttribute("lovesnPage", lovesnPage);         

        

        return "loves/index";

    }

    

}