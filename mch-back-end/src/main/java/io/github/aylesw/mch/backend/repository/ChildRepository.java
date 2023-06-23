package io.github.aylesw.mch.backend.repository;

import io.github.aylesw.mch.backend.model.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {
    @Query("SELECT c FROM Child c WHERE c.fullName LIKE %?1%")
    public List<Child> findByKeyword(String keyword);

    @Query("SELECT c FROM Child c JOIN FETCH c.parent u WHERE u.id = ?1")
    public List<Child> findByParentId(Long parentId);
}
