package com.pr.parser.repository;

import com.pr.parser.entity.ProductSpecificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSpecificationRepository extends JpaRepository<ProductSpecificationEntity, Long> {
}
