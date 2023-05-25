package mch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mch.model.Child;

public interface ChildRepository extends JpaRepository<Child, Long> {
	
	@Query("SELECT c FROM Child c JOIN FETCH c.parent p WHERE p.id = ?1")
	public List<Child> findByParent(Long parentId);
}
