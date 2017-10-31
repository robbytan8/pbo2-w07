package com.robby.controller;

import com.robby.MainApp;
import com.robby.dao.DepartmentDaoImpl;
import com.robby.entity.Department;
import com.robby.utility.DBUtil;
import com.robby.utility.ViewUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author Robby
 */
public class MainFormController implements Initializable {

    @FXML
    private BorderPane borderPane;
    private DepartmentDaoImpl departmentDao;
    private ObservableList<Department> departments;
    private Stage departmentStage;

    public DepartmentDaoImpl getDepartmentDao() {
        if (departmentDao == null) {
            departmentDao = new DepartmentDaoImpl();
        }
        return departmentDao;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public ObservableList<Department> getDepartments() {
        if (departments == null) {
            departments = FXCollections.observableArrayList();
            departments.addAll(getDepartmentDao().showAllData());
        }
        return departments;
    }

    @FXML
    private void mnAboutAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Created by Robby");
        alert.setTitle("Developer Info");
        alert.showAndWait();
    }

    @FXML
    private void mnCloseAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void mnReportAllStudentAction(ActionEvent event) {
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport("report/Demo_Report1.jasper", new HashMap<>(),
                    DBUtil.createMySQLConnection());
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setTitle("Demo Report");
            jasperViewer.setVisible(true);
        } catch (JRException | ClassNotFoundException | SQLException ex) {
            ViewUtil.showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
        }
    }

    @FXML
    private void toolDepAction(ActionEvent event) {
        if (departmentStage == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("view/DepartmentForm.fxml"));
                AnchorPane root = loader.load();
                DepartmentFormController controller = loader.getController();
                controller.setMainController(this);
                Scene scene = new Scene(root);
                departmentStage = new Stage();
                departmentStage.setScene(scene);
                departmentStage.setTitle("Department Management");
                departmentStage.initOwner(borderPane.getScene().getWindow());
                departmentStage.initModality(Modality.NONE);
//                departmentStage.show();
            } catch (IOException ex) {
                ViewUtil.showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        }
        if (departmentStage.isShowing() && !departmentStage.isFocused()) {
            departmentStage.toFront();
        } else {
            departmentStage.show();
        }
    }

    @FXML
    private void toolStudentAction(ActionEvent event) {
    }

}
