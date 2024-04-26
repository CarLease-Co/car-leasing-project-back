package com.carlease.project.autosuggestor;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/autosuggestors")
public class AutosuggestorController {
    private final AutosuggestorServiceImpl autosuggestorService;

    @GetMapping(produces = "application/json")
    ResponseEntity<List<AutosuggestorDto>> getAutosuggestions() {
        List<AutosuggestorDto> list = autosuggestorService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}

