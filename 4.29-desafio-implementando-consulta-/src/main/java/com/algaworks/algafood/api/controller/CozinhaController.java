package com.algaworks.algafood.api.controller;


import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Controller
//@ResponseBody // indica que a resposta do metodo do controlador devem ser retornandas no response da requisicão
@RestController // tem controlador e o responseBody
@RequestMapping(value = "/cozinhas") //, produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinha;

    @GetMapping //(produces = MediaType.APPLICATION_JSON_VALUE) //{MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<Cozinha> listar(){
        return cozinhaRepository.listar();
    }

    @GetMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
        Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);

        if(cozinha != null){
            return ResponseEntity.ok(cozinha);
        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cozinha adicionar(@RequestBody Cozinha cozinha){
        return cadastroCozinha.salvar(cozinha);
    }

    @PutMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId,
                                             @RequestBody Cozinha cozinha){
        Cozinha cozinhaAtual = cozinhaRepository.buscar(cozinhaId);

        if(cozinhaAtual != null) {
//        cozinhaAtual.setNome(cozinha.getNome());
            BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");

            cozinhaAtual = cadastroCozinha.salvar(cozinhaAtual);
            return ResponseEntity.ok(cozinhaAtual);
        }

        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> remover(@PathVariable Long cozinhaId){
        try {
            cadastroCozinha.excluir(cozinhaId);
            return ResponseEntity.noContent().build();

        } catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();

        } catch(EntidadeEmUsoException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // @ResponseStatus(HttpStatus.CREATED) difinir uma resposta do codigo http
//    @GetMapping("/{cozinhaId}")
//    public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId){
//        Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);
//      return ResponseEntity.status(HttpStatus.OK).body(cozinha);
//      return ResponseEntity.ok(cozinha);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.LOCATION, "http://localhost:8080/cozinhas");
//        return ResponseEntity
//                .status(HttpStatus.FOUND)
//                .headers(headers)
//                .build();
//    }
}
