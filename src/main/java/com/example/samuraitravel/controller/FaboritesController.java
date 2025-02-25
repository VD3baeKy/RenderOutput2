package com.example.samuraitravel.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FaboriteHouseDTO;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.LovesRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReservationService;



@Controller
public class FaboritesController {

    private LovesRepository lovesRepository = null;   
    private HouseRepository houseRepository = null;
    private ReservationService reservationService = null;
    private FaboriteHouseDTO faboriteHouseDTO =null;
      //public ReservationController(ReservationRepository reservationRepository, HouseRepository houseRepository, ReservationService reservationService) { 

    @Autowired
    public FaboritesController(LovesRepository lovesRepository, HouseRepository houseRepository, ReservationService reservationService) { 
        this.lovesRepository = lovesRepository; 
        this.houseRepository = houseRepository;
        this.reservationService = reservationService;
    } 



    
    @GetMapping("/faborites")
    public String index(
    		@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, 
    		@PageableDefault(page = 0, size = 5, sort = "id", direction = Direction.ASC) Pageable pageable, 
    		Model model
    		) {

        User user = userDetailsImpl.getUser();
        
        //Page<Loves> fabosPage = lovesRepository.findByUseridOrderByCreatedAtDesc(user.getId(), pageable);

        Page<FaboriteHouseDTO> fabosPage = lovesRepository.getFabos(user.getId(),pageable);

        model.addAttribute("fabosPage", fabosPage);
        
        System.out.println("   ***** fabosPage :  " + fabosPage.getContent().size());
        System.out.println("   ***** fabosPage =  " + fabosPage.getContent().toString());

        return "faborites/index";

    }
    
     
    /*
     @GetMapping("/faborites")
     public String index(
             @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, 
             @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, 
             Model model) {

         User user = userDetailsImpl.getUser();
         Page<Loves> fabosPage = lovesRepository.findByUseridOrderByCreatedAtDesc(user.getId(), pageable);
         
         List<FaboriteHouseDTO> faboriteHouseDTOs = new ArrayList<>();
         for (Loves loves : fabosPage.getContent()) {
             House house = houseRepository.findById(loves.getHouseid()).orElse(null);
             FaboriteHouseDTO dto = new FaboriteHouseDTO(loves, house);
             faboriteHouseDTOs.add(dto);
         }

         model.addAttribute("fabosPage", faboriteHouseDTOs);
         
         return "faborites/index";
     }
     */

}
