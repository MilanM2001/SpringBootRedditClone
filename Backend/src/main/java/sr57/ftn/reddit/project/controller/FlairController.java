package sr57.ftn.reddit.project.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sr57.ftn.reddit.project.model.dto.flairDTOs.AddFlairDTO;
import sr57.ftn.reddit.project.model.dto.flairDTOs.FlairDTO;
import sr57.ftn.reddit.project.model.entity.Flair;
import sr57.ftn.reddit.project.repository.FlairRepository;
import sr57.ftn.reddit.project.service.FlairService;

import java.util.List;

@RestController
@RequestMapping("api/flairs")
public class FlairController {
    final FlairService flairService;
    final ModelMapper modelMapper;
    final FlairRepository flairRepository;

    @Autowired
    public FlairController(FlairService flairService, ModelMapper modelMapper, FlairRepository flairRepository) {
        this.flairService = flairService;
        this.modelMapper = modelMapper;
        this.flairRepository = flairRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<FlairDTO>> getAll() {
        List<Flair> flairs = flairService.findAll();

        List<FlairDTO> flairsDTO = modelMapper.map(flairs, new TypeToken<List<FlairDTO>>() {
        }.getType());
        return new ResponseEntity<>(flairsDTO, HttpStatus.OK);
    }

    @GetMapping("/{flair_id}")
    public ResponseEntity<FlairDTO> getFlair(@PathVariable("flair_id") Integer flair_id) {
        Flair flair = flairService.findOne(flair_id);
        return flair == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(modelMapper.map(flair, FlairDTO.class), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<AddFlairDTO> addFlair(@RequestBody AddFlairDTO addFlairDTO) {
        Flair newFlair = new Flair();

        newFlair.setName(addFlairDTO.getName());

        newFlair = flairService.save(newFlair);
        return new ResponseEntity<>(modelMapper.map(newFlair, AddFlairDTO.class), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{flair_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFlair(@PathVariable("flair_id") Integer flair_id) {
        Flair flair = flairService.findOne(flair_id);

        if (flair != null) {
            flairService.remove(flair_id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
