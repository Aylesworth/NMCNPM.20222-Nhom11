package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import io.github.aylesw.mch.frontend.common.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.swing.text.html.ImageView;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class InjectionItemController implements Initializable {
    private Map<String, Object> properties;
    private ChildDetailsController parentController;

    public InjectionItemController(Map<String, Object> properties, ChildDetailsController parentController) {
        this.properties = properties;
        this.parentController = parentController;
    }

    @FXML
    private Label lblDate;

    @FXML
    private Label lblVaccineName;

    @FXML
    private Label lblDoseNo;

    @FXML
    private Label lblNote;

    @FXML
    private Label lblStatus;

    @FXML
    private JFXButton btnHandleReaction;

    @FXML
    private Button btnAddReaction;

    @FXML
    private Button btnRemoveReaction;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXListView<String> listReactions;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (UserIdentity.isAdmin() ||
                LocalDate.parse(properties.get("date").toString()).isAfter(LocalDate.now())) {
            btnAddReaction.setVisible(false);
            btnAddReaction.setManaged(false);
            btnRemoveReaction.setVisible(false);
            btnRemoveReaction.setManaged(false);
        }
        if (!UserIdentity.isAdmin()) {
            btnEdit.setVisible(false);
//            btnEdit.setManaged(false);
            btnDelete.setVisible(false);
//            btnDelete.setManaged(false);
            btnHandleReaction.setVisible(false);
//            btnHandleReaction.setManaged(false);
        }
        btnHandleReaction.setVisible(false);
        btnRemoveReaction.setVisible(false);
        lblDate.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(properties.get("date").toString()));
        lblVaccineName.setText(properties.get("vaccineName").toString());
        lblDoseNo.setText(((Double) properties.get("vaccineDoseNo")).longValue() + "");
        lblNote.setText(Optional.ofNullable(properties.get("note")).orElse("").toString());
        lblStatus.setText(Optional.ofNullable(properties.get("status")).orElse("").toString());
        var reactions = (List<String>) properties.get("reactions");
        listReactions.setItems(FXCollections.observableArrayList(reactions));
        listReactions.getSelectionModel().selectedItemProperty().addListener((observale, oldValue, newValue) -> {
            if (newValue != null) {
                if (btnHandleReaction.isManaged())
                    btnHandleReaction.setVisible(true);
                if (btnRemoveReaction.isManaged())
                    btnRemoveReaction.setVisible(true);
            }
        });
    }

    @FXML
    void delete(ActionEvent event) {
        ButtonType buttonType = Utils.showAlert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xóa thông tin này?");
        if (!buttonType.equals(ButtonType.OK)) return;

        try {
            long childId = ((Double) properties.get("childId")).longValue();
            long id = ((Double) properties.get("id")).longValue();

            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/injections/" + id)
                    .token(Utils.getToken())
                    .method("DELETE")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Xóa thông tin mũi tiêm thành công!");
            parentController.loadInjections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void edit(ActionEvent event) {
        ScreenManager.getEditInjectionStage(properties, parentController).show();
    }

    @FXML
    void handleReaction(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Xử lý triệu chứng");
        dialog.setHeaderText("Vui lòng điền hướng dẫn xử lý triệu chứng:");
        dialog.setContentText("");
        var input = dialog.showAndWait();
        while (input.isEmpty() || input.get().isBlank()) {
            if (input.isEmpty()) return;
            Utils.showAlert(Alert.AlertType.ERROR, "Vui lòng nhập gì đó!");
            input = dialog.showAndWait();
        }

        long childId = ((Double) properties.get("childId")).longValue();
        long id = ((Double) properties.get("id")).longValue();
        String reaction = listReactions.getSelectionModel().getSelectedItem();
        String advice = input.get();
        String requestBody = new RequestBodyMap()
                .put("reaction", reaction)
                .put("advice", advice)
                .toJson();

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/injections/" + id + "/handle-reaction")
                    .token(Utils.getToken())
                    .method("POST")
                    .requestBody(requestBody)
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Gửi hướng dẫn xử lý triệu chứng thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addReaction(ActionEvent event) {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Thêm triệu chứng");
            dialog.setHeaderText("Nhập triệu chứng mới:");
            dialog.setContentText(null);
            var input = dialog.showAndWait();

            if (input.isEmpty()) return;

            long childId = ((Double) properties.get("childId")).longValue();
            long id = ((Double) properties.get("id")).longValue();
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/injections/" + id + "/add-reaction?details=" + input.get())
                    .method("POST")
                    .build().request();

            listReactions.getItems().add(input.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void removeReaction(ActionEvent event) {
        try {
            String reaction = listReactions.getSelectionModel().getSelectedItem();

            long childId = ((Double) properties.get("childId")).longValue();
            long id = ((Double) properties.get("id")).longValue();
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/injections/" + id + "/remove-reaction?details=" + reaction)
                    .method("DELETE")
                    .build().request();

            listReactions.getItems().remove(reaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
