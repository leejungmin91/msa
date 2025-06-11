package com.store.product.repository;

import com.store.member.entity.MemberEntity;
import com.store.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    boolean existsByName(String name);

    @Query("SELECT p FROM ProductEntity p WHERE p.useYn = 'Y' AND (:displayYn IS NULL OR p.displayYn = :displayYn)")
    Page<ProductEntity> findAllByUseYnAndDisplayYn(Pageable pageable,  @Param("displayYn") String displayYn);

    @Query("""
       SELECT p
         FROM ProductEntity p
        WHERE p.id = :id
          AND p.useYn = 'Y'
          AND (:displayYn IS NULL OR p.displayYn = :displayYn)
    """)
    Optional<ProductEntity> findByIdWithOptionalDisplayYn(
            @Param("id") Long id,
            @Param("displayYn") String displayYn
    );

    @Query("""
       SELECT p
         FROM ProductEntity p
        WHERE p.id IN :ids
          AND p.useYn = 'Y'
          AND (:displayYn IS NULL OR p.displayYn = :displayYn)
    """)
    List<ProductEntity> findAllByIdInWithOptionalDisplayYn(
            @Param("ids") List<Long> ids,
            @Param("displayYn") String displayYn
    );

    @Query("SELECT DISTINCT p FROM ProductEntity p LEFT JOIN FETCH p.images WHERE p.useYn = 'Y' AND p.displayYn = 'Y'")
    List<ProductEntity> findAllWithImages();

    @Query("SELECT p FROM ProductEntity p LEFT JOIN FETCH p.images WHERE p.id = :id AND p.useYn = 'Y' AND p.displayYn = 'Y'")
    Optional<ProductEntity> findByIdWithImages(@Param("id") Long id);

    @Query("SELECT p FROM ProductEntity p LEFT JOIN FETCH p.images WHERE p.name LIKE %:name% AND p.useYn = 'Y' AND p.displayYn = 'Y'")
    Optional<ProductEntity> findByNameContainingWithImages(@Param("name") String name);

    /*@Modifying
    @Query("UPDATE ProductEntity p SET p.stockQuantity = p.stockQuantity - :quantity WHERE p.id = :productId AND p.stockQuantity >= :quantity")
    int decreaseStock(@Param("productId") Long productId, @Param("quantity") int quantity);*/

}
