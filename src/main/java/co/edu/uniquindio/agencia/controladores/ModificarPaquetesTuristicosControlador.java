package co.edu.uniquindio.agencia.controladores;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

public class ModificarPaquetesTuristicosControlador implements Initializable, CambioIdiomaListener {

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
    private ComboBox<String> cbxDestinosPaquete;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnAgregarDestino;

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnAtras;

    @FXML
    private Button btnEliminarDestino;

    @FXML
    private Label nombrePaquete, duracion, serviciosAdicionales, precio, cupoMaximo, fechaInicio, fechaFin, destinosAsignados;

    public AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();
    private final Propiedades propiedades = Propiedades.getInstance();

    private PaqueteTuristico paqueteTuristico;

    private ArrayList<Destino> destinos;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización normal del controlador

        // Registra este controlador como un escuchador de cambios de idioma
        Propiedades.getInstance().addCambioIdiomaListener(this);

        // Actualiza las cadenas de texto según el idioma actual
        actualizarTextos();

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
        nombrePaquete.setText(propiedades.getResourceBundle().getString("TextoNombrePaquete"));
        duracion.setText(propiedades.getResourceBundle().getString("TextoDuracion"));
        serviciosAdicionales.setText(propiedades.getResourceBundle().getString("TextoServiciosAdicionales"));
        precio.setText(propiedades.getResourceBundle().getString("TextoPrecio"));
        cupoMaximo.setText(propiedades.getResourceBundle().getString("TextoCupoMaximo"));
        fechaInicio.setText(propiedades.getResourceBundle().getString("TextoFechaInicio"));
        fechaFin.setText(propiedades.getResourceBundle().getString("TextoFechaFin"));
        destinosAsignados.setText(propiedades.getResourceBundle().getString("TextoDestinosAsignados"));
        btnBuscar.setText(propiedades.getResourceBundle().getString("TextoBuscar"));
        btnEliminarDestino.setText(propiedades.getResourceBundle().getString("TextoEliminarDestino"));
        btnAgregarDestino.setText(propiedades.getResourceBundle().getString("TextoAgregarDestino"));
        btnAtras.setText(propiedades.getResourceBundle().getString("TextoAtras"));
        btnActualizar.setText(propiedades.getResourceBundle().getString("TextoActualizar"));
        btnEliminar.setText(propiedades.getResourceBundle().getString("TextoEliminarPaquete"));
    }


    public void setPaqueteTuristico() {
        this.paqueteTuristico = agenciaCliente.obtenerPaqueteTuristico(txtNombrePaquete.getText());

        if (this.paqueteTuristico != null) {
            this.destinos = paqueteTuristico.getDestinos();
            llenarComboBox();
            actualizarCampos(); // Llamamos a este método para llenar los campos inicialmente
        } else {
            mostrarMensaje(Alert.AlertType.ERROR, "Paquete Turistico no encontrado.");
        }
    }


    public void llenarComboBox(){
        ObservableList<String> nombresDestinosPaquete = FXCollections.observableArrayList();

        for (Destino destino : destinos) {
            nombresDestinosPaquete.add(destino.getNombre());
        }

        cbxDestinosPaquete.setItems(nombresDestinosPaquete);
    }

    private void actualizarCampos() {
        if (paqueteTuristico != null) {
            txtNombrePaquete.setText(paqueteTuristico.getNombre());
            txtDuracion.setText(paqueteTuristico.getDuracion());
            txtServiciosAdicionales.setText(paqueteTuristico.getServiciosAdicionales());
            txtPrecio.setText(String.valueOf(paqueteTuristico.getPrecio()));
            txtCupoMaximo.setText(String.valueOf(paqueteTuristico.getCupoMaximo()));
            dpFechaInicio.setValue(paqueteTuristico.getFechaInicio());
            dpFechaFin.setValue(paqueteTuristico.getFechaFin());

        } else {
            mostrarMensaje(Alert.AlertType.INFORMATION, propiedades.getResourceBundle().getString("TextoPaqueteS"));
        }
    }

    @FXML
    void regresarInicio(ActionEvent event) {
        Object evt = event.getSource();
        if(evt.equals(btnAtras)){
            agenciaCliente.loadStage("/ventanas/inicioAdmin.fxml", event);
        }
    }

    @FXML
    void actualizarPaqueteTuristico(ActionEvent event) {

        try {
            agenciaCliente.actualizarPaqueteTuristico(
                    txtNombrePaquete.getText(),
                    destinos,
                    txtDuracion.getText(),
                    txtServiciosAdicionales.getText(),
                    Float.parseFloat(txtPrecio.getText()),
                    Integer.parseInt(txtCupoMaximo.getText()),
                    dpFechaInicio.getValue(),
                    dpFechaFin.getValue()
            );
            mostrarMensaje(Alert.AlertType.INFORMATION, propiedades.getResourceBundle().getString("TextoPaqueteS1") + paqueteTuristico.getNombre());
        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
        }
    }


    @FXML
    void eliminarDestino(ActionEvent event){
        String nombreSeleccionado = cbxDestinosPaquete.getValue();

        if(nombreSeleccionado != null){
            Iterator<Destino> iter = destinos.iterator();
            while (iter.hasNext()) {
                Destino destino = iter.next();
                if(destino.getNombre().equals(nombreSeleccionado)){
                    iter.remove(); // Usamos el iterador para eliminar el destino de la lista
                    mostrarMensaje(Alert.AlertType.INFORMATION, propiedades.getResourceBundle().getString("TextoPaqueteS2"));
                }
            }

            // Después de eliminar el destino, actualizamos el ComboBox de destinos
            llenarComboBox();
        }
    }


    @FXML
    void buscarDestino(ActionEvent event) {
        String nombreSeleccionado = cbxDestinos.getValue(); // Obtiene el nombre seleccionado del ComboBox

        if (nombreSeleccionado != null) {
            boolean destinoEncontrado = false;
            for (Destino destino : destinos) {
                if (destino.getNombre().equals(nombreSeleccionado)) {
                    destinoEncontrado = true;
                    break;
                }
            }

            if (!destinoEncontrado) {
                // Si el destino no se encuentra en la lista, lo agregamos
                Destino nuevoDestino = agenciaCliente.obtenerDestino(nombreSeleccionado);
                if (nuevoDestino != null) {
                    destinos.add(nuevoDestino);
                    llenarComboBox();
                    mostrarMensaje(Alert.AlertType.INFORMATION, propiedades.getResourceBundle().getString("TextoPaqueteS3") + nombreSeleccionado + propiedades.getResourceBundle().getString("TextoPaqueteTuristico2"));
                } else {
                    mostrarMensaje(Alert.AlertType.ERROR, propiedades.getResourceBundle().getString("TextoPaqueteS4"));
                }
            } else {
                mostrarMensaje(Alert.AlertType.ERROR, propiedades.getResourceBundle().getString("TextoPaqueteTuristico3") + nombreSeleccionado +propiedades.getResourceBundle().getString("TextoPaqueteS5"));
            }
        }
    }


    @FXML
    void eliminarPaqueteTuristico(ActionEvent event) {
        // Implementa la lógica para eliminar un paquete turistico
        agenciaCliente.eliminarPaquete(txtNombrePaquete.getText());
        mostrarMensaje(Alert.AlertType.INFORMATION, "Se elimino el Paquete Turistico: "+txtNombrePaquete.getText());

    }

    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}