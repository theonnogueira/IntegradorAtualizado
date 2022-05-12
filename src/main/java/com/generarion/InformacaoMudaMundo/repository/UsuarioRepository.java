package com.generarion.InformacaoMudaMundo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generarion.InformacaoMudaMundo.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	public Optional<Usuario> findByNome(String nome);

}
