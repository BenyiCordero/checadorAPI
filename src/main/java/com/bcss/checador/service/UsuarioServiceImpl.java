package com.bcss.checador.service;

import com.bcss.checador.domain.Usuario;
import com.bcss.checador.exception.EmailRepetidoException;
import com.bcss.checador.exception.UsuarioNoEncontradoException;
import com.bcss.checador.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario saveIfNotExist(Usuario usuario) {
        if(findByEmail(usuario.getEmail()).isPresent()) throw new EmailRepetidoException();
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if(usuario.isPresent()) return usuario;
        else throw new UsuarioNoEncontradoException();
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if(usuario.isPresent()) return usuario;
        else throw new UsuarioNoEncontradoException();
    }

}
