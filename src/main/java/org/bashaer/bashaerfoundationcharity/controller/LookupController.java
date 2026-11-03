package org.bashaer.bashaerfoundationcharity.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bashaer.bashaerfoundationcharity.dao.LookupDao;
import org.bashaer.bashaerfoundationcharity.model.CategoryItem;

public class LookupController {
    @FXML private TableView<CategoryItem> categoryTable;
    @FXML private TableColumn<CategoryItem, Integer> colId;
    @FXML private TableColumn<CategoryItem, String> colName;
    @FXML private TableColumn<CategoryItem, String> colDesc;
    @FXML private TextField nameField;
    @FXML private TextField descField;

    @FXML
    public void initialize() {
        // Map table columns to CategoryItem properties
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadTableData();
    }

    private void loadTableData() {
        categoryTable.setItems(LookupDao.getAllCategories());
    }

    @FXML
    protected void handleAdd() {
        String name = nameField.getText();
        String desc = descField.getText();

        if (name != null && !name.trim().isEmpty()) {
            LookupDao.addCategory(name, desc);
            nameField.clear();
            descField.clear();
            loadTableData(); // Refresh table
        }
    }

    @FXML
    protected void handleDelete() {
        CategoryItem selected = categoryTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            LookupDao.deleteCategory(selected.getId());
            loadTableData(); // Refresh table
        }
    }
}