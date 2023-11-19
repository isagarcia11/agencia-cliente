package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.modelo.AgenciaCliente;
import co.edu.uniquindio.agencia.modelo.GuiaTuristico;
import co.edu.uniquindio.agencia.modelo.Propiedades;
import co.edu.uniquindio.agencia.utils.CambioIdiomaEvent;
import co.edu.uniquindio.agencia.utils.CambioIdiomaListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiasControlador implements Initializable, CambioIdiomaListener {
    public TableView<GuiaTuristico> tableView;
    public TableColumn<GuiaTuristico, String> columnNombre;
    public TableColumn<GuiaTuristico, String> columnIdentificacion;
    public TableColumn<GuiaTuristico, String> columnIdiomas;
    public TableColumn<GuiaTuristico, String> columnExperiencia;

    @FXML
    private Button btnAtras;

    public AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();
    private final Propiedades propiedades = Propiedades.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización normal del controlador

        // Registra este controlador como un escuchador de cambios de idioma
        Propiedades.getInstance().addCambioIdiomaListener(this);

        // Actualiza las cadenas de texto según el idioma actual
        actualizarTextos();

        columnNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        columnIdentificacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdentificacion()));
        columnIdiomas.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdiomas().toString()));
        columnExperiencia.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getExperiencia())));

        tableView.setItems(FXCollections.observableArrayList(agenciaCliente.listarGuiasTuristicos()));
    }
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        // Se llama cuando se cambia el idioma

        // Actualiza las cadenas de texto según el nuevo idioma
        actualizarTextos();
    }

    private void actualizarTextos() {
        columnNombre.setText(propiedades.getResourceBundle().getString("TextoNombre"));
        columnIdentificacion.setText(propiedades.getResourceBundle().getString("TextoIdentificacion"));
        columnIdiomas.setText(propiedades.getResourceBundle().getString("TextoIdiomas"));
        columnExperiencia.setText(propiedades.getResourceBundle().getString("TextoExperiencia"));
        btnAtras.setText(propiedades.getResourceBundle().getString("TextoAtras"));

    }

    public void volverAlInicio(ActionEvent event){

        Object evt = event.getSource();

        if(evt.equals(btnAtras)){
            agenciaCliente.loadStage("/ventanas/inicioAdmin.fxml", event);
        }
    }
}

