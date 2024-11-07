package com.br.web.v1.service;

import com.br.web.v1.model.Animals;
import com.br.web.v1.model.ImageAnimalModel;
import com.br.web.v1.model.dto.AnimalsDTO;
import com.br.web.v1.repository.AnimalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class AnimalsService {

    @Autowired
    private AnimalsRepository animalsRepository;

    @Autowired
    private ModelMapper modelMapper;

    private List<AnimalsDTO> cachedAnimalList;
    private UUID cacheVersion = UUID.randomUUID();

    private List<AnimalsDTO> savedAnimalList = new ArrayList<>();

    public Page<AnimalsDTO> getByCriteria(String name, Integer page, Integer pageSize, String sort, String order) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sort);
        return this.modelMapper.map(this.animalsRepository.getByCriteria(name, pageable), new TypeToken<Page<Animals>>() {}.getType());
    }

    public AnimalsDTO getById(Long id) throws Exception {
        Animals animal = this.animalsRepository.getById(id);
        return this.modelMapper.map(animal, AnimalsDTO.class);
    }

    public List<AnimalsDTO> getAll() {
        if (cachedAnimalList == null) {
            loadCache();
        }
        return cachedAnimalList;
    }

    private void loadCache() {
        List<Animals> animalsList = animalsRepository.findAll();
        Type listType = new TypeToken<List<AnimalsDTO>>() {}.getType();
        cachedAnimalList = modelMapper.map(animalsList, listType);
    }

    public void addAnimal(Animals animal) {
        animalsRepository.save(animal);
        invalidateCache();
    }

    public void removeAnimal(Long id) {
        animalsRepository.deleteById(id);
        invalidateCache();
    }

    private void invalidateCache() {
        cacheVersion = UUID.randomUUID();
        cachedAnimalList = null;
    }

}
