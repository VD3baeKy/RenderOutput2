package com.example.samuraitravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.samuraitravel.entity.Loves;

public interface LovesRepository extends JpaRepository<Loves, Integer> {
	//public Page<Loves> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
	public Page<Loves> findByUseridAndHouseidOrderByCreatedAtDesc(Integer user, Integer house, Pageable pageable);
	public Page<Loves> findByUseridOrderByCreatedAtDesc(Integer user, Pageable pageable);
	
	//@Query("SELECT new com.example.samuraitravel.repository.FaboriteHouseDTO(l, h) FROM Loves l JOIN House h ON l.id = h.id WHERE l.id = ?1 ORDER BY l.createdAt DESC")
	//@Query("SELECT l.id, l.userid, l.createdAt, h.id, h.name, h.imageName, h.description, h.price, h.capacity, h.postalCode, h.address, h.phoneNumber, h.createdAt, h.updatedAt FROM Loves l JOIN House h ON l.houseid = h.id WHERE l.userid = ?1 ORDER BY l.createdAt DESC")
	@Query("SELECT new com.example.samuraitravel.repository.FaboriteHouseDTO(l, h) FROM Loves l JOIN House h ON l.houseid = h.id WHERE l.userid = ?1 ORDER BY l.createdAt DESC")
	public Page<FaboriteHouseDTO> getFabos(Integer keyword, Pageable pageable);
}

/*
SELECT 
loves.id AS love_id,
loves.houseid,
loves.userid,
loves.created_at AS love_created_at,
houses.id AS house_id,
houses.name AS house_name,
houses.image_name,
houses.description,
houses.price,
houses.capacity,
houses.postal_code,
houses.address,
houses.phone_number,
houses.created_at AS house_created_at,
houses.updated_at
FROM 
loves
JOIN 
houses ON loves.houseid = houses.id
WHERE
loves.userid = 1;
*/
