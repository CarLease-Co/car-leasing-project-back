package com.carlease.project.autosuggestor;

import com.carlease.project.application.ApplicationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("api/v1/autosuggestors")
public class AutosuggestorController {
    public AutosuggestorServiceImpl autosuggestorService;
}

