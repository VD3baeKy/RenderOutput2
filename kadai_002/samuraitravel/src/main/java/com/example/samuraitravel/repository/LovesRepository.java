package com.example.samuraitravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Loves;
import com.example.samuraitravel.entity.User;

public interface LovesRepository extends JpaRepository<Loves, Integer> {
	public Page<Loves> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

}
