package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.modelo.AgenciaCliente;
import co.edu.uniquindio.agencia.modelo.Destino;
import co.edu.uniquindio.agencia.modelo.PaqueteTuristico;
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

public class PaquetesControlador implements Initializable, CambioIdiomaListener {

    @FXML
    private TableColumn<PaqueteTuristico, String> columnNombre;

    @FXML
    private TableColumn<PaqueteTuristico, String> columnDestinos;

    @FXML
    private TableColumn<PaqueteTuristico, String> columnDuracion;

    @FXML
    private TableColumn<PaqueteTuristico, String> columnServiciosAdicionales;

    @FXML
    private TableColumn<PaqueteTuristico, String> columnPrecio;

    @FXML
    private TableColumn<PaqueteTuristico, String> columnCupoMaximo;

    @FXML
    private TableColumn<PaqueteTuristico, String> columnFechaInicio;

    @FXML
    private TableColumn<PaqueteTuristico, String> columnFechaFin;

    @FXML
    private TableView<PaqueteTuristico> tablaPaquetes;

    @FXML
    private Button btnAtras;

    private final AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();

    private final Propiedades propiedades = Propiedades.getInstance();
    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización normal del controlador

        // Registra este controlador como un escuchador de cambios de idioma
        Propiedades.getInstance().addCambioIdiomaListener(this);

        // Actualiza las cadenas de texto según el idioma actual
        actualizarTextos();

        columnNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        columnDestinos.setCellValueFactory(cellData -> {
            String destinos = cellData.getValue().getDestinos().stream()
                    .map(Destino::getNombre)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            return new SimpleStringProperty(destinos);
        });
        columnDuracion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDuracion()));
        columnServiciosAdicionales.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServiciosAdicionales()));
        columnPrecio.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPrecio())));
        columnCupoMaximo.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCupoMaximo())));
        columnFechaInicio.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaInicio().toString()));
        columnFechaFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaFin().toString()));

        tablaPaquetes.setItems(FXCollections.observableArrayList(agenciaCliente.listarPaquetesTuristicos()));
    }
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        // Se llama cuando se cambia el idioma

        // Actualiza las cadenas de texto según el nuevo idioma
        actualizarTextos();
    }

    private void actualizarTextos() {
        columnNombre.setText(propiedades.getResourceBundle().getString("TextoNombre"));
        columnDuracion.setText(propiedades.getResourceBundle().getString("TextoDuracion"));
        columnServiciosAdicionales.setText(propiedades.getResourceBundle().getString("TextoServAdicionales"));
        columnPrecio.setText(propiedades.getResourceBundle().getString("TextoPrecio"));
        columnCupoMaximo.setText(propiedades.getResourceBundle().getString("TextoCupoMaximo"));
        columnFechaInicio.setText(propiedades.getResourceBundle().getString("TextoFechaInicio"));
        columnFechaFin.setText(propiedades.getResourceBundle().getString("TextoFechaFin"));
        btnAtras.setText(propiedades.getResourceBundle().getString("TextoAtras"));
    }

    public void regresarInicio(ActionEvent event){
        Object evt = event.getSource();
        if(evt.equals(btnAtras)){
            agenciaCliente.loadStage("/ventanas/inicioAdmin.fxml", event);
        }
    }
}
