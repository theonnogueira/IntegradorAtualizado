package com.generarion.InformacaoMudaMundo.Controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generarion.InformacaoMudaMundo.model.Temas;
import com.generarion.InformacaoMudaMundo.repository.TemasRepository;

import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional;

@RestController
@RequestMapping("/Temas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TemaController {

	@Autowired
	private TemasRepository temaRe;

	@GetMapping
	public ResponseEntity<List<Temas>> getAll() {
		return ResponseEntity.ok(temaRe.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Temas> getById(@PathVariable Long id) {
		return temaRe.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/Nome/{descricao}")
	public ResponseEntity<List<Temas>> getByDescricao(@PathVariable String descricao) {
		return ResponseEntity.ok(temaRe.findAllByDescricaoContainingIgnoreCase(descricao));
	}

	@PostMapping
	public ResponseEntity<Temas> post(@Valid @RequestBody Temas temas) {
		return ResponseEntity.status(HttpStatus.CREATED).body(temaRe.save(temas));

	}

	@PutMapping
	public ResponseEntity<Temas> put(@Valid @RequestBody Temas tema) {
		return temaRe.findById(tema.getId())
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(temaRe.save(tema)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		java.util.Optional<Temas> temas = temaRe.findById(id);

		if (temas.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		temaRe.deleteById(id);
	}

}
