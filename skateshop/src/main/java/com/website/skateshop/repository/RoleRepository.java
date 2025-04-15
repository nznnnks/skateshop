package com.website.skateshop.repository;

import com.website.skateshop.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByCharacterTitle(String characterTitle);
    Page<RoleEntity> findAll(Pageable pageable);
}