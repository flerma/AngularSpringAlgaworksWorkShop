package com.algaworks.comercial.controller;

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

import com.algaworks.comercial.model.Oportunidade;
import com.algaworks.comercial.repository.OportunidadeRepository;

// GET http://localhost:8080/oportunidades

@CrossOrigin
@RestController
@RequestMapping("/oportunidades")
public class OportunidadeController {

	@Autowired
	private OportunidadeRepository oportunidadeRepository;
	
	@GetMapping
	public List<Oportunidade> listar() {
		return oportunidadeRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Oportunidade> buscar(@PathVariable Long id) {
		Optional<Oportunidade> oportunidade = oportunidadeRepository.findById(id);
		
		if (!oportunidade.isPresent()) {
			return ResponseEntity.notFound().build();
		}
 		return ResponseEntity.ok(oportunidade.get());
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Oportunidade adicionar(@Valid @RequestBody Oportunidade oportunidade) {
		
		Optional<Oportunidade> oportunidadeExistente = oportunidadeRepository
				.findByDescricaoAndNomeProspecto(oportunidade.getDescricao(), 
						oportunidade.getNomeProspecto());
		
		if (oportunidadeExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
					"Já existe uma oportunidade para este prospecto com a mesma descrição");
		}
		return oportunidadeRepository.save(oportunidade);
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public Oportunidade atualizar(@Valid @RequestBody Oportunidade oportunidade) {
		Optional<Oportunidade> oportunidadeExistente = oportunidadeRepository.findById(oportunidade.getId());
		
		if (!oportunidadeExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
					"Não foi possível atualizar esta oportunidade. Oportunidade não encontrada.");
		}
		
		return oportunidadeRepository.save(oportunidade);
		
	}
	
	@DeleteMapping("/{id}")
	public void excluir(@PathVariable Long id) {
		
		Optional<Oportunidade> oportunidadeExistente = oportunidadeRepository.findById(id);
		
		if (!oportunidadeExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
					"Não foi possível excluir esta oportunidade. Oportunidade não encontrada.");
		}
		
		oportunidadeRepository.deleteById(id);
	}
	
	
	
}
