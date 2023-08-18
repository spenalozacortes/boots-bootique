package com.example.boots.controllers;

import java.lang.Iterable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.boots.entities.Boot;
import com.example.boots.enums.BootType;
import com.example.boots.exceptions.QueryNotSupportedException;

import com.example.boots.repositories.BootRepository;

@RestController
@RequestMapping("/api/v1/boots")
public class BootController {
    private final BootRepository bootRepository;

    public BootController(final BootRepository bootRepository) {
        this.bootRepository = bootRepository;
    }

    @GetMapping("/")
    public Iterable<Boot> getAllBoots() {
        return this.bootRepository.findAll();
    }

    @GetMapping("/types")
    public List<BootType> getBootTypes() {
        return Arrays.asList(BootType.values());
    }

    @PostMapping("/")
    public Boot addBoot(@RequestBody Boot boot) {
        Boot newBoot = this.bootRepository.save(boot);
        return newBoot;
    }

    @DeleteMapping("/{id}")
    public Boot deleteBoot(@PathVariable("id") Integer id) {
        Optional<Boot> bootToDeleteOptional = this.bootRepository.findById(id);
        if (!bootToDeleteOptional.isPresent()) {
            return null;
        }
        Boot bootToDelete = bootToDeleteOptional.get();
        this.bootRepository.delete(bootToDelete);
        return bootToDelete;
    }

    @PutMapping("/{id}/quantity/increment")
    public Boot incrementQuantity(@PathVariable("id") Integer id) {
        Optional<Boot> bootToUpdateOptional = this.bootRepository.findById(id);
        if (!bootToUpdateOptional.isPresent()) {
            return null;
        }
        Boot bootToUpdate = bootToUpdateOptional.get();
        bootToUpdate.setQuantity(bootToUpdate.getQuantity() + 1);
        return this.bootRepository.save(bootToUpdate);
    }

    @PutMapping("/{id}/quantity/decrement")
    public Boot decrementQuantity(@PathVariable("id") Integer id) {
        Optional<Boot> bootToUpdateOptional = this.bootRepository.findById(id);
        if (!bootToUpdateOptional.isPresent()) {
            return null;
        }
        Boot bootToUpdate = bootToUpdateOptional.get();
        bootToUpdate.setQuantity(bootToUpdate.getQuantity() - 1);
        return this.bootRepository.save(bootToUpdate);
    }

    @GetMapping("/search")
    public List<Boot> searchBoots(@RequestParam(required = false) String material,
                                  @RequestParam(required = false) BootType type, @RequestParam(required = false) Float size,
                                  @RequestParam(required = false, name = "quantity") Integer minQuantity) throws QueryNotSupportedException {
        if (Objects.nonNull(material)) {
            if (Objects.nonNull(type) && Objects.nonNull(size) && Objects.nonNull(minQuantity)) {
                return this.bootRepository.findByMaterialAndTypeAndSizeAndQuantityGreaterThan(material, type, size, minQuantity);
            } else if (Objects.nonNull(type) && Objects.nonNull(size)) {
                return this.bootRepository.findByMaterialAndTypeAndSize(material, type, size);
            } else if (Objects.nonNull(type) && Objects.nonNull(minQuantity)) {
                return this.bootRepository.findByMaterialAndTypeAndQuantityGreaterThan(material, type, minQuantity);
            } else if (Objects.nonNull(type)) {
                return this.bootRepository.findByMaterialAndType(material, type);
            } else {
                return this.bootRepository.findByMaterial(material);
            }
        } else if (Objects.nonNull(type)) {
            if (Objects.nonNull(size) && Objects.nonNull(minQuantity)) {
                return this.bootRepository.findByTypeAndSizeAndQuantityGreaterThan(type, size, minQuantity);
            } else if (Objects.nonNull(size)) {
                return this.bootRepository.findByTypeAndSize(type, size);
            } else if (Objects.nonNull(minQuantity)) {
                return this.bootRepository.findByTypeAndQuantityGreaterThan(type, minQuantity);
            } else {
                return this.bootRepository.findByType(type);
            }
        } else if (Objects.nonNull(size)) {
            if (Objects.nonNull(minQuantity)) {
                return this.bootRepository.findBySizeAndQuantityGreaterThan(size, minQuantity);
            } else {
                return this.bootRepository.findBySize(size);
            }
        } else if (Objects.nonNull(minQuantity)) {
            return this.bootRepository.findByQuantityGreaterThan(minQuantity);
        } else {
            throw new QueryNotSupportedException("This query is not supported! Try a different combination of search parameters.");
        }
    }
}
