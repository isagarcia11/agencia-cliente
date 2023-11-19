package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.exceptions.AtributoNegativoException;
import co.edu.uniquindio.agencia.exceptions.AtributoVacioException;
import co.edu.uniquindio.agencia.exceptions.FechaInvalidaException;
import co.edu.uniquindio.agencia.exceptions.InformacionRepetidaException;
import co.edu.uniquindio.agencia.modelo.AgenciaCliente;
import co.edu.uniquindio.agencia.modelo.Destino;
import co.edu.uniquindio.agencia.modelo.PaqueteTuristico;
import co.edu.uniquindio.agencia.modelo.Propiedades;
import co.edu.uniquindio.agencia.utils.CambioIdiomaEvent;
import co.edu.uniquindio.agencia.utils.CambioIdiomaListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CrearPaqueteTuristicoControlador implements Initializable, CambioIdiomaListener {

    @FXML
    private TextField txtNombrePaquete;

    @FXML
    private TextField txtDuracion;

    @FXML
    private TextField txtServiciosAdicionales;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtCupoMaximo;

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private DatePicker dpFechaFin;

    @FXML
    private ComboBox<String> cbxDestinos;

    @FXML
    private Button btnAgregarDestino;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnAtras;

    public AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();
    private final Propiedades propiedades = Propiedades.getInstance();

    public ArrayList<Destino> destinosEncontrados = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización normal del controlador

        // Registra este controlador como un escuchador de cambios de idioma
        Propiedades.getInstance().addCambioIdiomaListener(this);

        // Actualiza las cadenas de texto según el idioma actual
        actualizarTextos();

        // Obtener la lista de destinos de la agencia de viajes
        ObservableList<String> nombresDestinos = FXCollections.observableArrayList();

        for (Destino destino : agenciaCliente.listarDestino()) {
            nombresDestinos.add(destino.getNombre());
        }

        cbxDestinos.setItems(nombresDestinos);
    }
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        // Se llama cuando se cambia el idioma

        // Actualiza las cadenas de texto según el nuevo idioma
        actualizarTextos();
    }

    private void actualizarTextos() {
        txtNombrePaquete.setPromptText(propiedades.getResourceBundle().getString("TextoNombrePaquete"));
        txtDuracion.setPromptText(propiedades.getResourceBundle().getString("TextoDuracion"));
        txtCupoMaximo.setPromptText(propiedades.getResourceBundle().getString("TextoCupoMaximo"));
        txtPrecio.setPromptText(propiedades.getResourceBundle().getString("TextoPrecio"));
        txtServiciosAdicionales.setPromptText(propiedades.getResourceBundle().getString("TextoServiciosAdicionales"));
        btnAgregarDestino.setText(propiedades.getResourceBundle().getString("TextoAgregarDestino"));
        btnAtras.setText(propiedades.getResourceBundle().getString("TextoAtras"));
        btnGuardar.setText(propiedades.getResourceBundle().getString("TextoGuardar"));
        dpFechaInicio.setPromptText(propiedades.getResourceBundle().getString("TextoFechaInicio"));
        dpFechaFin.setPromptText(propiedades.getResourceBundle().getString("TextoFechaFin"));

    }

    @FXML
    void regresarInicio(ActionEvent event) {
        // Implementa la lógica para regresar a la pantalla de inicio
        Object evt = event.getSource();
        if(evt.equals(btnAtras)){
            agenciaCliente.loadStage("/ventanas/inicioAdmin.fxml", event);
        }
    }

    @FXML
    void registrarPaqueteTuristico(ActionEvent event) {
        try {
            String nombre = txtNombrePaquete.getText();
            ArrayList<Destino> destinos = destinosEncontrados;
            String duracion = txtDuracion.getText();
            String serviciosAdicionales = txtServiciosAdicionales.getText();

            // Verifica si el campo de texto está vacío antes de intentar la conversión
            float precio = txtPrecio.getText().isEmpty() ? 0 : Float.parseFloat(txtPrecio.getText());

            // Verifica si el campo de texto está vacío antes de intentar la conversión
            int cupoMaximo = txtCupoMaximo.getText().isEmpty() ? 0 : Integer.parseInt(txtCupoMaximo.getText());

            LocalDate fechaInicio = dpFechaInicio.getValue();
            LocalDate fechaFin = dpFechaFin.getValue();

            String mensaje = agenciaCliente.registrarPaqueteTuristico(nombre, destinos, duracion, serviciosAdicionales, precio, cupoMaximo, fechaInicio, fechaFin);
            mostrarMensaje(Alert.AlertType.INFORMATION, mensaje);
        } catch (AtributoVacioException | InformacionRepetidaException | AtributoNegativoException | FechaInvalidaException e) {
            mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
        } catch (NumberFormatException e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Por favor, ingrese valores válidos para Precio y Cupo Máximo.");
        }
    }


    @FXML
    void buscarDestino(ActionEvent event) {
        String nombreSeleccionado = cbxDestinos.getValue(); // Obtiene el nombre seleccionado del ComboBox

        if (nombreSeleccionado != null) {
            for (Destino destino : agenciaCliente.listarDestino()) {
                if (destino.getNombre().equals(nombreSeleccionado)) {
                    if (!destinosEncontrados.contains(destino)) {
                        destinosEncontrados.add(destino);
                        mostrarMensaje(Alert.AlertType.INFORMATION, propiedades.getResourceBundle().getString("TextoPaqueteTuristico1")+nombreSeleccionado+propiedades.getResourceBundle().getString("TextoPaqueteTuristico2")+txtNombrePaquete.getText());
                    } else {
                        mostrarMensaje(Alert.AlertType.ERROR, propiedades.getResourceBundle().getString("TextoPaqueteTuristico3")+nombreSeleccionado+propiedades.getResourceBundle().getString("TextoPaqueteTuristico4"));
                    }
                }
            }
        }
    }

    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

}
