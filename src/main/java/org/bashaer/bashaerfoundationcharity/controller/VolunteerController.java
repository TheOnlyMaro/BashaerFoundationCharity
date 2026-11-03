package org.bashaer.bashaerfoundationcharity.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.bashaer.bashaerfoundationcharity.dao.LookupDao;
import org.bashaer.bashaerfoundationcharity.model.CategoryItem;
import org.bashaer.bashaerfoundationcharity.pattern.observer.EventSubject;
import org.bashaer.bashaerfoundationcharity.pattern.observer.EventSubjectManager;
import org.bashaer.bashaerfoundationcharity.pattern.observer.VolunteerObserver;

public class VolunteerController {
    @FXML private TextField volunteerNameField;
    @FXML private ComboBox<String> subscribeCategoryCombo;
    @FXML private TextArea notificationArea;

    private VolunteerObserver observer;

    @FXML
    public void initialize() {
        loadCategories();
    }

    @FXML
    protected void handleSubscribeCategoryClick(){
        loadCategories();
    }

    public void loadCategories() {
        subscribeCategoryCombo.getItems().clear();
        for (CategoryItem item : LookupDao.getAllCategories()) {
            subscribeCategoryCombo.getItems().add(item.getName());
        }
    }

    @FXML
    protected void handleSubscribe() {
        String name = volunteerNameField.getText();
        String category = subscribeCategoryCombo.getValue();

        if (name == null || name.trim().isEmpty() || category == null) {
            notificationArea.appendText("System Error: Enter name and select a category.\n");
            return;
        }

        if (observer == null) {
            observer = new VolunteerObserver(name);
            observer.setUiCallback(msg -> notificationArea.appendText(msg + "\n"));
        }

        EventSubject subject = EventSubjectManager.getSubject(category);
        subject.addObserver(observer);

        notificationArea.appendText("Successfully subscribed to category: " + category + "\n");
    }
}