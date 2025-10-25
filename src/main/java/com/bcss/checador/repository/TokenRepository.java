package com.bcss.checador.repository;

import com.bcss.checador.domain.Token;
import com.bcss.checador.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    Optional<Token> findByToken(String token);
    List<Token> findByUser(Usuario usuario);
}