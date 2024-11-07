package com.br.web.v1.controller;

import com.br.web.v1.model.dto.AnimalsDTO;
import com.br.web.v1.service.AnimalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animals")
public class AnimalsController {

    @Autowired
    private AnimalsService animalsService;

    @GetMapping
    public ResponseEntity<AnimalsDTO> getById(@RequestParam Long id) {
        try {
            return new ResponseEntity<>(animalsService.getById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/getByCriteria")
    public ResponseEntity<Page<AnimalsDTO>> getByCriteria(@RequestParam String name, @RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam String sort, @RequestParam String order){
        return new ResponseEntity<>(this.animalsService.getByCriteria(name, page, pageSize, sort, order), HttpStatus.OK);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<AnimalsDTO>> getAllAnimals(){
        return new ResponseEntity<>(this.animalsService.getAll(), HttpStatus.OK);
    }

}
