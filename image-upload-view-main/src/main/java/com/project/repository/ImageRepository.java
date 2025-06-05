package com.project.repository;

import com.project.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM image_table WHERE id = ?1", nativeQuery = true)
    int deleteImageById(Long id);



}
