package com.bcss.checador.service;

import com.bcss.checador.domain.Usuario;

import java.util.Optional;

public interface UsuarioService {
    Usuario saveIfNotExist(Usuario usuario);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(Integer id);
}
