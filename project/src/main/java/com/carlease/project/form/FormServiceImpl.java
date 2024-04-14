package com.carlease.project.form;

import com.carlease.project.form.exceptions.FormValidationException;

public class FormServiceImpl implements IFormService {

    @Override
    public void save(Form form) throws FormValidationException {
        if (!form.isValid()) {
            throw new FormValidationException("Form is not valid");
        }
    }
}
