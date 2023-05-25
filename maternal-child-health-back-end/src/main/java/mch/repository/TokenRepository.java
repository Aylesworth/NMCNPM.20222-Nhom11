package mch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mch.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
	
//	@Query("SELECT * FROM token t JOIN FETCH t.user u WHERE u.email = ?1 AND t.active = 1")
//	public Optional<Token> findAllValidTokensByUser(String username);
	
	public Optional<Token> findByValue(String value);
}
