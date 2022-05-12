package com.generarion.InformacaoMudaMundo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.generarion.InformacaoMudaMundo.model.Temas;

@Repository
public interface TemasRepository extends JpaRepository<Temas, Long> {
	public List<Temas> findAllByDescricaoContainingIgnoreCase(String descricao);
}
