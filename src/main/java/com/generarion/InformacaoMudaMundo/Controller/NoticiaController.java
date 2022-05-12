package com.generarion.InformacaoMudaMundo.Controller;

import java.util.List;
import java.util.Optional;

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

import com.generarion.InformacaoMudaMundo.model.Noticias;
import com.generarion.InformacaoMudaMundo.repository.NoticiasRepository;
import com.generarion.InformacaoMudaMundo.repository.TemasRepository;

@RestController
@RequestMapping("/Noticias")
@CrossOrigin("*")
public class NoticiaController {

	@Autowired
	private NoticiasRepository noticiasRE;

	@Autowired
	private TemasRepository temaRe;

	@GetMapping
	public ResponseEntity<List<Noticias>> GetAll() {

		return ResponseEntity.ok(noticiasRE.findAll());

	}

	@GetMapping("/{id}") // localhost8080/Noticias/{id}
	public ResponseEntity<Noticias> getById(@PathVariable Long id) {
		return noticiasRE.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/Titulo/{titulo}") // localhost8080/Noticias/Titulo/{titulo da noticia}
	public ResponseEntity<List<Noticias>> getByTitulo(@PathVariable String titulo) {
		return ResponseEntity.ok(noticiasRE.findAllByTituloContainingIgnoreCase(titulo));
	}

	@PostMapping
	public ResponseEntity<Noticias> post(@Valid @RequestBody Noticias noticias) {
		if (temaRe.existsById(noticias.getTema().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(noticiasRE.save(noticias));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

	}

	@PutMapping
	public ResponseEntity<Noticias> putNoticias(@Valid @RequestBody Noticias noticias) {
		if (noticiasRE.existsById(noticias.getId())) {
			if (temaRe.existsById(noticias.getTema().getId()))
				return ResponseEntity.status(HttpStatus.OK).body(noticiasRE.save(noticias));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		Optional<Noticias> noticias = noticiasRE.findById(id);

		if (noticias.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		noticiasRE.deleteById(id);
	}

}
