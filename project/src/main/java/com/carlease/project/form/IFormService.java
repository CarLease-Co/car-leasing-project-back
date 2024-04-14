package com.carlease.project.form;

import com.carlease.project.form.exceptions.FormValidationException;

public interface IFormService {
    void save(Form form) throws FormValidationException;
}
