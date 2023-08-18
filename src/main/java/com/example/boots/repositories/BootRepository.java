package com.example.boots.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.boots.entities.Boot;
import com.example.boots.enums.BootType;

public interface BootRepository extends CrudRepository<Boot, Integer> {
    List<Boot> findBySize(Float size);
    List<Boot> findByMaterial(String material);
    List<Boot> findByType(BootType type);
    List<Boot> findByQuantityGreaterThan(Integer quantity);
    List<Boot> findByMaterialAndTypeAndSizeAndQuantityGreaterThan(String material, BootType type, Float size, Integer quantity);
    List<Boot> findByMaterialAndTypeAndSize(String material, BootType type, Float size);
    List<Boot> findByMaterialAndTypeAndQuantityGreaterThan(String material, BootType type, Integer quantity);
    List<Boot> findByMaterialAndType(String material, BootType type);
    List<Boot> findByTypeAndSizeAndQuantityGreaterThan(BootType type, Float size, Integer quantity);
    List<Boot> findByTypeAndSize(BootType type, Float size);
    List<Boot> findByTypeAndQuantityGreaterThan(BootType type, Integer quantity);
    List<Boot> findBySizeAndQuantityGreaterThan(Float size, Integer quantity);
}
