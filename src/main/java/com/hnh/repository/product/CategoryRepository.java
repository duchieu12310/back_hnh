package com.hnh.repository.product;

import com.hnh.entity.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    List<Category> findByParentCategoryIsNull();

    Optional<Category> findBySlug(String slug);

    /**
     * Lấy tất cả ID danh mục (bao gồm danh mục cha và toàn bộ danh mục con đệ quy)
     * sử dụng WITH RECURSIVE để tránh N+1 query và lazy-loading issue.
     */
    @Query(value = """
            WITH RECURSIVE category_tree AS (
                SELECT id FROM category WHERE id = :rootId
                UNION ALL
                SELECT c.id FROM category c
                INNER JOIN category_tree ct ON c.parent_id = ct.id
            )
            SELECT id FROM category_tree
            """, nativeQuery = true)
    List<Long> findAllDescendantIds(@Param("rootId") Long rootId);

}

