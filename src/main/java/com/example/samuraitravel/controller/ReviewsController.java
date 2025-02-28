package com.example.samuraitravel.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewHouseDTO;
import com.example.samuraitravel.repository.ReviewsRepository;
import com.example.samuraitravel.repository.UserRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewsService;
import com.example.samuraitravel.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
@RequestMapping("/reviews")
public class ReviewsController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewsController.class);

    private final HouseRepository houseRepository;
    private final UserRepository userRepository;
    private final ReviewsRepository reviewsRepository;
    
    @Autowired
    private ReviewsService reviewService;

    @Autowired
    private UserService userService;
    
    @Autowired
    public ReviewsController(
        HouseRepository houseRepository, 
        UserRepository userRepository,
        ReviewsRepository reviewsRepository
    ) {
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
        this.reviewsRepository = reviewsRepository;
    }
      
    @GetMapping
    public String index(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "area", required = false) String area,
            @RequestParam(name = "price", required = false) Integer price,  
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "reviewhouseId", required = false) Integer reviewhouseId,
            @RequestParam(name = "uid", required = false) Integer uid,
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Direction.ASC) Pageable pageable,
            Model model
    ){
    	System.out.println("   [REVIEW-INDEX] A reviewhouseId= " + reviewhouseId);
    	if(reviewhouseId==null) {
    		reviewhouseId=0;
    	}
    	System.out.println("   [REVIEW-INDEX] B reviewhouseId= " + reviewhouseId);

        UserDetailsImpl userDetails=null;
   	
    	try {
    		userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                userDetails = (UserDetailsImpl) authentication.getPrincipal();
            } else {
                // 未認証の場合の処理
            }
        } catch (ClassCastException e) {
            // UserDetailsImplへのキャストに失敗した場合の処理
        }
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) {
            userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }


            
        Page<ReviewHouseDTO> reviewPage;        
        
        
        if (keyword != null && !keyword.isEmpty()) {
            if ("priceAsc".equals(order)) {
            	System.out.println("   [REVIEW-INDEX] @@@@@ 1");
                reviewPage = reviewsRepository.findByHousesNameLikeOrHousesAddressLikeOrderByHousesPriceAsc(keyword, pageable);
            } else {
            	System.out.println("   [REVIEW-INDEX] @@@@@ 2");
                reviewPage = reviewsRepository.findByHousesNameLikeOrHousesAddressLikeOrderByReviewCreatedAtDesc(keyword, pageable);
            }            
        } else if (area != null && !area.isEmpty()) {            
            if ("priceAsc".equals(order)) {
            	System.out.println("   [REVIEW-INDEX] @@@@@ 3");
                reviewPage = reviewsRepository.findByHousesAddressLikeOrderByHousesPriceAsc(area, pageable);
            } else {
            	System.out.println("   [REVIEW-INDEX] @@@@@ 4");
                reviewPage = reviewsRepository.findByHousesAddressLikeOrderByReviewCreatedAtDesc(area, pageable);
            }            
        } else if (price != null) {
            if ("priceAsc".equals(order)) {
            	System.out.println("   [REVIEW-INDEX] @@@@@ 5");
                reviewPage = reviewsRepository.findByHousesPriceLessThanEqualOrderByHousesPriceAsc(price, pageable);
            } else {
            	System.out.println("   [REVIEW-INDEX] @@@@@ 6");
                reviewPage = reviewsRepository.findByHousesPriceLessThanEqualOrderByReviewCreatedAtDesc(price, pageable);
            }            
    	} else if (reviewhouseId>0) {
    		if ("priceAsc".equals(order)) {
    			System.out.println("   [REVIEW-INDEX] @@@@@ 7");
    			reviewPage = reviewsRepository.findByHousesIdOrderByHousesPriceAsc(reviewhouseId, pageable);
    		} else {	
    			System.out.println("   [REVIEW-INDEX] @@@@@ 8");
    			reviewPage = reviewsRepository.findByHousesIdOrderByReviewCreatedAtDesc(reviewhouseId, pageable);
    		}
    	} else if (uid==1 && userDetails != null) {
        	UserDetailsImpl userDetailsCopy = userDetails;
            System.out.println(userDetails.getUserId()); // 現在ログインしているユーザーのuserId
    		if ("priceAsc".equals(order)) {
    			System.out.println("   [REVIEW-INDEX] @@@@@ 8.4");
    			reviewPage = reviewsRepository.findByUserIdOrderByHousesPriceAsc(userDetailsCopy.getUserId(), pageable);
            }else {	
    			System.out.println("   [REVIEW-INDEX] @@@@@ 8.6");
    			reviewPage = reviewsRepository.findByUserIdOrderByReviewCreatedAtDesc(userDetailsCopy.getUserId(), pageable);
    		}
        }else {
            if ("priceAsc".equals(order)) {
            	System.out.println("   [REVIEW-INDEX] @@@@@ 9");
                reviewPage = reviewsRepository.findAllByOrderByHousesPriceAsc(pageable);
            } else {
            	System.out.println("   [REVIEW-INDEX] @@@@@ 10");
                reviewPage = reviewsRepository.findAllByOrderByReviewCreatedAtDesc(pageable);   
            }            
        }
        
        logger.debug("reviewPage= {}", reviewPage);
        logger.debug("keyword= {}", keyword);
        logger.debug("area= {}", area);
        logger.debug("price= {}", price);
        logger.debug("order= {}", order);
        logger.debug("pageable= {}", pageable);
        
        List<ReviewHouseDTO> reviewList = reviewPage.getContent();
        model.addAttribute("reviewList", reviewList);
        
        ObjectMapper objectMapper = new ObjectMapper();
        String reviewListJson = "[]";
        try {
            reviewListJson = objectMapper.writeValueAsString(reviewList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        model.addAttribute("reviewListJson", reviewListJson);
        
        model.addAttribute("reviewPage", reviewPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("area", area);
        model.addAttribute("price", price); 
        model.addAttribute("order", order);
        model.addAttribute("reviewhouseId", reviewhouseId);
        
        UserDetailsImpl userDetails=null;
        Boolean loginwas = false;
    	try {
    		userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                userDetails = (UserDetailsImpl) authentication.getPrincipal();
                loginwas = true;
            } else {
                // 未認証の場合の処理
            }
        } catch (ClassCastException e) {
            // UserDetailsImplへのキャストに失敗した場合の処理
        }
    	
    	
    	boolean hasWrittenReview = false; // スコープの外で宣言
        if (userDetails != null) {
        	UserDetailsImpl userDetailsCopy = userDetails;
            System.out.println("   [REVIEW-INDEX] 現在ログインしているユーザーのuserId= " + userDetails.getUserId()); // 現在ログインしているユーザーのuserId
            System.out.println("   [REVIEW-INDEX] 現在ログインしているユーザーのハッシュ化されたpassword= " + userDetails.getPassword()); // 現在ログインしているユーザーのハッシュ化されたpassword
            System.out.println("   [REVIEW-INDEX] 現在ログインしているユーザーのmailアドレス= " + userDetails.getUsername()); // 現在ログインしているユーザーのmailアドレス
            // 現在のユーザーのレビューを取得
            hasWrittenReview = reviewList.stream().anyMatch(r -> r.getReview().getUserid().equals(userDetailsCopy.getUserId())); // ユーザーのレビューがあるか確認
            model.addAttribute("currentUserId", userDetails.getUserId());
            model.addAttribute("hasWrittenReview", hasWrittenReview);
            System.out.println("   [REVIEW-INDEX] currentUserId= " + userDetails.getUserId());
            System.out.println("   [REVIEW-INDEX] hasWrittenReview= " + hasWrittenReview);
        } else {
        	System.out.println("   [REVIEW-INDEX] #########################################");
            System.out.println("   [REVIEW-INDEX] ############    NOT LOGIN    ############");
            System.out.println("   [REVIEW-INDEX] #########################################");
            System.out.println("   [REVIEW-INDEX] currentUserId= " + -1);
            System.out.println("   [REVIEW-INDEX] hasWrittenReview= " + false);
            model.addAttribute("currentUserId", -1);
            model.addAttribute("hasWrittenReview", false);
        }
        model.addAttribute("loginwas", loginwas);
        System.out.println("   [REVIEW-INDEX] loginwas= " + loginwas);
        
        boolean isAuthenticatedData = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
        	isAuthenticatedData = true;
        }
        System.out.println("   [REVIEW-INDEX] isAuthenticatedData= " + isAuthenticatedData);
        model.addAttribute("isAuthenticatedData", isAuthenticatedData);
        
        return "reviews/index";
    }
    

}
