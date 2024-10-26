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

    private List<AnimalsDTO> savedAnimalList = new ArrayList<>();

    public Page<AnimalsDTO> getByCriteria(String name, Integer page, Integer pageSize, String sort, String order) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sort);
        return this.modelMapper.map(this.animalsRepository.getByCriteria(name, pageable), new TypeToken<Page<Animals>>() {}.getType());
    }

    public AnimalsDTO getById(Long id) throws Exception {
        Animals animal = this.animalsRepository.getById(id);
        return this.modelMapper.map(animal, AnimalsDTO.class);
    }

    public AnimalsDTO save(AnimalsDTO animalsDTO) {
        Animals animals = new Animals();
        Date registrationDate = new Date();

        if(animalsDTO.getId() != null) {
            animals = this.animalsRepository.getById(animalsDTO.getId());
        }
        if(animalsDTO.getId() == null) {
            animals.setRegistrationDate(registrationDate);
        }
        this.convertDtoToObject(animalsDTO, animals);

        return this.modelMapper.map(this.animalsRepository.save(animals), AnimalsDTO.class);
    }

    public void deleteAnimal(Long id) throws Exception {
        try {
            animalsRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("Erro ao deletar o animal: " + e.getMessage());
        }
    }

    public List<AnimalsDTO> getAll() {

        if (this.savedAnimalList.isEmpty() || this.savedAnimalList.size() != this.getCountAnimals()) {
            List<Animals> animalsList = this.animalsRepository.findAll();
            Type listType = new TypeToken<List<AnimalsDTO>>() {}.getType();
            this.savedAnimalList = this.modelMapper.map(animalsList, listType);
            return this.modelMapper.map(animalsList, listType);
        }
        return this.savedAnimalList;
    }

    private int getCountAnimals() {
        return this.animalsRepository.getCountAnimals();
    }

    public Set<ImageAnimalModel> uplodImage(MultipartFile[] multipartFiles) throws IOException {
        Set<ImageAnimalModel> imageModels = new HashSet<>();

        for(MultipartFile file: multipartFiles) {
            ImageAnimalModel imageModel = new ImageAnimalModel(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes());
            imageModels.add(imageModel);
        }
        return imageModels;
    }

    public void changeActive(Long id, Date adoptionDate) {
        Animals animal = this.animalsRepository.getById(id);
        animal.setActive(!animal.getActive());

        if (animal.getActive() == false) {
            LocalDateTime now = LocalDateTime.now();
            Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
            animal.setAdoptionDate(date);

            animal.setAdoptionDate(adoptionDate);
        } else {
            animal.setAdoptionDate(null);
        }
        this.animalsRepository.save(animal);
    }

    private Animals convertDtoToObject(AnimalsDTO animalsDTO, Animals animals) {
        animals.setName(animalsDTO.getName());
        animals.setInstagramURL(animalsDTO.getInstagramURL());
        animals.setAnimalAge(animalsDTO.getAnimalAge());
        animals.setAnimalType(animalsDTO.getAnimalType());
        animals.setRace(animalsDTO.getRace());
        animals.setSize(animalsDTO.getSize());
        animals.setDescription(animalsDTO.getDescription());
        animals.setPriority(animalsDTO.getPriority());
        animals.setAnimalImage(animalsDTO.getAnimalImage());
        animals.setAnimalSex(animalsDTO.getAnimalSex());
        animals.setAdoptionDate(null);
        animals.setActive(true);

        return animals;
    }

}
