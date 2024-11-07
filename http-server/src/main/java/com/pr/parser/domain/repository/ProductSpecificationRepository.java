package com.pr.parser.domain.repository;

import com.pr.parser.domain.entity.ProductSpecificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSpecificationRepository extends JpaRepository<ProductSpecificationEntity, Long> {
}
