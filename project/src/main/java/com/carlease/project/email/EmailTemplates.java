package com.carlease.project.email;

public class EmailTemplates {
    public static final String NEW_USER = "Dear %s,\n\nWelcome to %s! We're thrilled to have you on board.\n\nYour account has been successfully created. You can now log in using your credentials and explore our platform.\n\nThank you for joining us, and we look forward to serving you.\n\nBest regards,\n%s";
    public static final String FORM_ADDED = "Dear %s,\n\nYour submitted form has been added to our database.\n\nBest regards,\n%s";
    public static final String FORM_REVIEWED = "Dear %s,\n\nYour submitted form has been reviewed.\n\nBest regards,\n%s";
    public static final String FORM_APPROVED = "Dear %s,\n\nYour submitted form has been approved.\n\nThank you,\n%s";
    public static final String FORM_DECLINED = "Dear %s,\n\nUnfortunately, your submitted form has been declined.\n\nBest regards,\n%s";
    public static final String FORM_AWAITING_REVIEW = "You have been assigned to review a form. Please log in to the system to review.";
    public static final String FORM_AWAITING_APPROVAL = "A form is awaiting your approval. Please log in to review and approve or decline the form.";

    private EmailTemplates() {}
}