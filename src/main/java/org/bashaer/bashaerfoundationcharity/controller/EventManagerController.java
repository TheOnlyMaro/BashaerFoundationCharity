package org.bashaer.bashaerfoundationcharity.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bashaer.bashaerfoundationcharity.dao.EventDao;
import org.bashaer.bashaerfoundationcharity.dao.LookupDao;
import org.bashaer.bashaerfoundationcharity.model.CategoryItem;
import org.bashaer.bashaerfoundationcharity.pattern.observer.EventSubjectManager;

public class EventManagerController {
    @FXML private TableView<EventDao.EventRecord> eventTable;
    @FXML private TableColumn<EventDao.EventRecord, Integer> idCol;
    @FXML private TableColumn<EventDao.EventRecord, String> titleCol;
    @FXML private TableColumn<EventDao.EventRecord, String> categoryCol;
    @FXML private TableColumn<EventDao.EventRecord, String> statusCol;

    @FXML private TextField titleField;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private ComboBox<String> statusCombo;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        statusCombo.getItems().addAll("Upcoming", "Active", "Completed", "Cancelled");
        loadCategories();
        loadEvents();
    }

    @FXML
    protected void handleCategoryClick() {
        loadCategories();
    }

    private void loadCategories() {
        categoryCombo.getItems().clear();
        for (CategoryItem item : LookupDao.getAllCategories()) {
            categoryCombo.getItems().add(item.getName());
        }
    }

    private void loadEvents() {
        eventTable.setItems(FXCollections.observableArrayList(EventDao.getAllEvents()));
    }

    @FXML
    protected void handleCreateEvent() {
        String title = titleField.getText();
        String category = categoryCombo.getValue();
        if (title == null || title.trim().isEmpty() || category == null) {
            statusLabel.setText("Error: Enter title and select category.");
            return;
        }

        EventDao.createEvent(title, category, "Upcoming");
        loadEvents();
        titleField.clear();
        statusLabel.setText("Event created successfully!");

        EventSubjectManager.broadcast(category, "New event created: '" + title + "' in category [" + category + "]");
    }

    @FXML
    protected void handleUpdateStatus() {
        EventDao.EventRecord selected = eventTable.getSelectionModel().getSelectedItem();
        String newStatus = statusCombo.getValue();
        if (selected == null || newStatus == null) {
            statusLabel.setText("Error: Select an event and new status.");
            return;
        }

        EventDao.updateEventStatus(selected.getId(), newStatus);
        loadEvents();
        statusLabel.setText("Status updated to " + newStatus);

        EventSubjectManager.broadcast(selected.getCategory(), "Event '" + selected.getTitle() + "' status changed to: " + newStatus);
    }
}