package com.example.samuraitravel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Review;

public interface ReviewsRepository extends JpaRepository<Review, Integer> {

	/*
    @Modifying
    @Transactional
    @Query("DELETE FROM Review r WHERE r.id = :reviewId")
    void deleteById(Integer id);
    */
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Review r WHERE r.id = :reviewId")
    void deleteById(@Param("reviewId") Integer id);

    // 宿IDから検索して、価格の安い順でソート
    @Query("SELECT new com.example.samuraitravel.repository.ReviewHouseDTO(r, h, u) FROM Review r JOIN House h ON r.houseId = h.id JOIN User u ON r.userId = u.id WHERE h.id = ?1 ORDER BY h.price ASC")
    public Page<ReviewHouseDTO> findByHousesIdOrderByHousesPriceAsc(Integer keyword, Pageable pageable);

    // 宿IDから検索して、レビューの新しい順でソート
    @Query("SELECT new com.example.samuraitravel.repository.ReviewHouseDTO(r, h, u) FROM Review r JOIN House h ON r.houseId = h.id JOIN User u ON r.userId = u.id WHERE h.id = ?1 ORDER BY r.updatedAt DESC")
    public Page<ReviewHouseDTO> findByHousesIdOrderByReviewCreatedAtDesc(Integer keyword, Pageable pageable);
    
    // 宿名と宿の住所から検索して、価格の安い順でソート
    //@Query("SELECT new com.example.samuraitravel.repository.ReviewHouseDTO(r, h) FROM Review r JOIN House h ON r.houseId = h.id WHERE h.name LIKE %?1% OR h.address LIKE %?1% ORDER BY h.price ASC")
    @Query("SELECT new com.example.samuraitravel.repository.ReviewHouseDTO(r, h, u) FROM Review r JOIN House h ON r.houseId = h.id JOIN User u ON r.userId = u.id WHERE h.name LIKE %?1% OR h.address LIKE %?1% ORDER BY h.price ASC")
    public Page<ReviewHouseDTO> findByHousesNameLikeOrHousesAddressLikeOrderByHousesPriceAsc(String keyword, Pageable pageable);

    // 宿名と宿の住所から検索して、レビューの新しい順でソート
    @Query("SELECT new com.example.samuraitravel.repository.ReviewHouseDTO(r, h, u) FROM Review r JOIN House h ON r.houseId = h.id JOIN User u ON r.userId = u.id WHERE h.name LIKE %?1% OR h.address LIKE %?1% ORDER BY r.updatedAt DESC")
    public Page<ReviewHouseDTO> findByHousesNameLikeOrHousesAddressLikeOrderByReviewCreatedAtDesc(String keyword, Pageable pageable);

    // 宿の住所から検索して、価格の安い順でソート
    @Query("SELECT new com.example.samuraitravel.repository.ReviewHouseDTO(r, h, u) FROM Review r JOIN House h ON r.houseId = h.id JOIN User u ON r.userId = u.id WHERE h.address LIKE %?1% ORDER BY h.price ASC")
    public Page<ReviewHouseDTO> findByHousesAddressLikeOrderByHousesPriceAsc(String keyword, Pageable pageable);

    // 宿の住所から検索して、レビューの新しい順でソート
    @Query("SELECT new com.example.samuraitravel.repository.ReviewHouseDTO(r, h, u) FROM Review r JOIN House h ON r.houseId = h.id JOIN User u ON r.userId = u.id WHERE h.address LIKE %?1% ORDER BY r.updatedAt DESC")
    public Page<ReviewHouseDTO> findByHousesAddressLikeOrderByReviewCreatedAtDesc(String keyword, Pageable pageable);

    // 宿の電話番号から検索して、価格の安い順でソート
    @Query("SELECT new com.example.samuraitravel.repository.ReviewHouseDTO(r, h, u) FROM Review r JOIN House h ON r.houseId = h.id JOIN User u ON r.userId = u.id WHERE h.phoneNumber LIKE %?1% OR h.name LIKE %?2% OR h.address LIKE %?2% ORDER BY h.price ASC")
    public Page<ReviewHouseDTO> findByPhoneNumberLikeOrderByHousesPriceAsc(String phoneStr, String keyword, Pageable pageable);

    // 宿の電話番号から検索して、レビューの新しい順でソート
    @Query("SELECT new com.example.samuraitravel.repository.ReviewHouseDTO(r, h, u) FROM Review r JOIN House h ON r.houseId = h.id JOIN User u ON r.userId = u.id WHERE h.phoneNumber LIKE %?1% OR h.name LIKE %?2% OR h.address LIKE %?2% ORDER BY r.updatedAt DESC")
    public Page<ReviewHouseDTO> findByPhoneNumberLikeOrderByReviewCreatedAtDesc(String phoneStr, String keyword, Pageable pageable);

    // 宿の価格（〇円以下）で検索して、価格の安い順でソート
    @Query("SELECT new com.example.samuraitravel.repository.ReviewHouseDTO(r, h, u) FROM Review r JOIN House h ON r.houseId = h.id JOIN User u ON r.userId = u.id WHERE h.price <= ?1 ORDER BY h.price ASC")
    public Page<ReviewHouseDTO> findByHousesPriceLessThanEqualOrderByHousesPriceAsc(Integer keyword1, Pageable pageable);

    // 宿の価格（〇円以下）で検索して、レビューの新しい順でソート
    @Query("SELECT new com.example.samuraitravel.repository.ReviewHouseDTO(r, h, u) FROM Review r JOIN House h ON r.houseId = h.id JOIN User u ON r.userId = u.id WHERE h.price <= ?1 ORDER BY r.updatedAt DESC")
    public Page<ReviewHouseDTO> findByHousesPriceLessThanEqualOrderByReviewCreatedAtDesc(Integer keyword1, Pageable pageable);

    // ユーザーIDで検索して、価格の安い順でソート
    @Query("SELECT new com.example.samuraitravel.repository.ReviewHouseDTO(r, h, u) FROM Review r JOIN House h ON r.houseId = h.id JOIN User u ON r.userId = u.id WHERE r.userId = ?1 ORDER BY h.price ASC")
    public Page<ReviewHouseDTO> findByUserIdOrderByHousesPriceAsc(Integer keyword1, Pageable pageable);

    // ユーザーIDで検索して、レビューの新しい順でソート
    @Query("SELECT new com.example.samuraitravel.repository.ReviewHouseDTO(r, h, u) FROM Review r JOIN House h ON r.houseId = h.id JOIN User u ON r.userId = u.id WHERE r.userId = ?1 ORDER BY r.updatedAt DESC")
    public Page<ReviewHouseDTO> findByUserIdOrderByReviewCreatedAtDesc(Integer keyword1, Pageable pageable);

    // 全てのレビューを、価格の安い順でソート
    @Query("SELECT new com.example.samuraitravel.repository.ReviewHouseDTO(r, h, u) FROM Review r JOIN House h ON r.houseId = h.id JOIN User u ON r.userId = u.id ORDER BY h.price ASC")
    public Page<ReviewHouseDTO> findAllByOrderByHousesPriceAsc(Pageable pageable);

    // 全てのレビューを、レビューの新しい順でソート
    @Query("SELECT new com.example.samuraitravel.repository.ReviewHouseDTO(r, h, u) FROM Review r JOIN House h ON r.houseId = h.id JOIN User u ON r.userId = u.id ORDER BY r.updatedAt DESC")
    public Page<ReviewHouseDTO> findAllByOrderByReviewCreatedAtDesc(Pageable pageable);

    public List<Review> findTop10ByOrderByCreatedAtDesc();
    //public List<Review> findByHouseIdOrderByCreatedAtDesc(Integer houseId);
    //public Page<Review> findByHouseIdOrderByCreatedAtDesc(Integer houseId,Pageable pageable);
    public List<Review> findByHouseIdOrderByCreatedAtDesc(Integer houseId);
}
