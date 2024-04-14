package com.carlease.project.form;

import com.carlease.project.form.exceptions.FormValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FormController {

    private final IFormService formService;

    @Autowired
    public FormController(IFormService formService) {
        this.formService = formService;
    }

    @PostMapping("/form")
    public void save(Form form) throws FormValidationException {
        formService.save(form);
    }
}
