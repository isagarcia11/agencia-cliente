package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.enums.Estado;
import co.edu.uniquindio.agencia.exceptions.*;
import co.edu.uniquindio.agencia.modelo.*;
import co.edu.uniquindio.agencia.utils.CambioIdiomaEvent;
import co.edu.uniquindio.agencia.utils.CambioIdiomaListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class VentanaReservasClienteControlador  implements Initializable, CambioIdiomaListener {

    @FXML
    private TableView<Reserva> tablaReservas;

    @FXML
    private TableColumn<Reserva, String> columnNombrePaquete;

    @FXML
    private TableColumn<Reserva, String> columnDestinos;

    @FXML
    private TableColumn<Reserva, String> columnFechaInicioPaquete;

    @FXML
    private TableColumn<Reserva, String> columnFechaFinPaquete;

    @FXML
    private TableColumn<Reserva, String> columnCantidadPersonas;

    @FXML
    private TableColumn<Reserva, String> columnEstado;

    @FXML
    private TableColumn<Reserva, String> columnFechaSolicitud;

    @FXML
    private TableColumn<Reserva, String> columnFechaViaje;

    @FXML
    private Button btnAtras;

    @FXML
    private Button btnCancelarReserva,btnCalificarGuia;

    @FXML
    private Button btnConfirmarReserva, btnCalificarDestino;

    private final AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();
    private final Propiedades propiedades = Propiedades.getInstance();
    private Cliente cliente = agenciaCliente.getClienteAutenticado();

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización normal del controlador

        // Registra este controlador como un escuchador de cambios de idioma
        Propiedades.getInstance().addCambioIdiomaListener(this);

        // Actualiza las cadenas de texto según el idioma actual
        actualizarTextos();

        ArrayList<Reserva> reservasCliente = new ArrayList<>();

        for (Reserva reserva : agenciaCliente.listarReservas()) {
            if (reserva.getCliente().equals(cliente)) {
                reservasCliente.add(reserva);
            }

        }
        // Configurar las columnas de la tabla
        columnNombrePaquete.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaqueteTuristico().getNombre()));

        columnDestinos.setCellValueFactory(cellData -> {
            String destinos = cellData.getValue().getPaqueteTuristico().getDestinos().stream()
                    .map(Destino::getNombre)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            return new SimpleStringProperty(destinos);
        });

        columnFechaInicioPaquete.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaqueteTuristico().getFechaInicio().toString()));
        columnFechaFinPaquete.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaqueteTuristico().getFechaFin().toString()));

        columnCantidadPersonas.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCantidadDePersonas())));
        columnEstado.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getEstado())));
        columnFechaSolicitud.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaDeSolicitud().toString()));
        columnFechaViaje.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaDeViaje().toString()));

        tablaReservas.setItems(FXCollections.observableArrayList(reservasCliente));
    }
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        // Se llama cuando se cambia el idioma

        // Actualiza las cadenas de texto según el nuevo idioma
        actualizarTextos();
    }

    private void actualizarTextos() {
        btnAtras.setText(propiedades.getResourceBundle().getString("TextoAtras"));
        btnCancelarReserva.setText(propiedades.getResourceBundle().getString("TextoCancelarReserva"));
        btnConfirmarReserva.setText(propiedades.getResourceBundle().getString("TextoConfirmarReserva"));
        btnCalificarDestino.setText(propiedades.getResourceBundle().getString("TextoCalificarDestino"));
        btnCalificarGuia.setText(propiedades.getResourceBundle().getString("TextoCalificarGuia"));
        columnNombrePaquete.setText(propiedades.getResourceBundle().getString("TextoNombrePaquete"));
        columnDestinos.setText(propiedades.getResourceBundle().getString("TextoDestinos"));
        columnFechaInicioPaquete.setText(propiedades.getResourceBundle().getString("TextoFechaInicio"));
        columnFechaFinPaquete.setText(propiedades.getResourceBundle().getString("TextoFechaFin"));
        columnCantidadPersonas.setText(propiedades.getResourceBundle().getString("TextoCantidadPersonas"));
        columnEstado.setText(propiedades.getResourceBundle().getString("TextoEstado"));
        columnFechaSolicitud.setText(propiedades.getResourceBundle().getString("TextoFechaSolicitud"));
        columnFechaViaje.setText(propiedades.getResourceBundle().getString("TextoFechaViaje"));
    }
    @FXML
    private void cancelarReserva(ActionEvent event) throws AtributoVacioException, ReservaDuplicadaException, AtributoNegativoException, CupoMaximoExcedidoException, FechaInvalidaException {
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();

        if (reservaSeleccionada != null) {
            if (reservaSeleccionada.getEstado() == Estado.CONFIRMADA) {
                mostrarMensaje(Alert.AlertType.WARNING, propiedades.getResourceBundle().getString("TextoAlerta"));
            } else {
                // Cambiar el estado de la reserva a CANCELADA
                reservaSeleccionada.setEstado(Estado.CANCELADA);

                agenciaCliente.cancelarReserva(reservaSeleccionada);

                // Actualizar la tabla
                tablaReservas.refresh();

                mostrarMensaje(Alert.AlertType.INFORMATION, propiedades.getResourceBundle().getString("TextoAlerta1"));
            }
        } else {
            mostrarMensaje(Alert.AlertType.WARNING, propiedades.getResourceBundle().getString("TextoAlerta2"));
        }
    }


    @FXML
    private void confirmarReserva(ActionEvent event) throws AtributoVacioException, ReservaDuplicadaException, AtributoNegativoException, CupoMaximoExcedidoException, FechaInvalidaException {
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();

        if (reservaSeleccionada != null) {
            reservaSeleccionada.setEstado(Estado.CONFIRMADA);

            agenciaCliente.confirmarReserva(reservaSeleccionada);

            tablaReservas.refresh();

            mostrarMensaje(Alert.AlertType.INFORMATION, propiedades.getResourceBundle().getString("TextoAlerta3"));
            agenciaCliente.confirmarReserva(reservaSeleccionada);

            mostrarMensaje(Alert.AlertType.INFORMATION, "La reserva ha sido confirmada.");
        } else {
            mostrarMensaje(Alert.AlertType.WARNING, propiedades.getResourceBundle().getString("TextoAlerta4"));
        }
    }

    @FXML
    private void calificarDestinos(ActionEvent event) {
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();

        agenciaCliente.setReserva(reservaSeleccionada);

        if (reservaSeleccionada != null) {
            agenciaCliente.loadStage("/ventanas/calificarDestino.fxml", event);
        } else {
            mostrarMensaje(Alert.AlertType.WARNING, propiedades.getResourceBundle().getString("TextoAlerta5"));
        }
    }

    @FXML
    private void calificarGuia(ActionEvent event) {
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();

        agenciaCliente.setReserva(reservaSeleccionada);

        LocalDate fechaActual = LocalDate.now();
        if (reservaSeleccionada != null) {
            if(reservaSeleccionada.getPaqueteTuristico().getFechaFin().isBefore(fechaActual)){
                agenciaCliente.loadStage("/ventanas/calificarGuia.fxml", event);
            } else {
                mostrarMensaje(Alert.AlertType.WARNING, propiedades.getResourceBundle().getString("TextoAlerta6"));
            }

        } else {
            mostrarMensaje(Alert.AlertType.WARNING, propiedades.getResourceBundle().getString("TextoAlerta5"));
        }
    }


    @FXML
    public void volverInicio(ActionEvent event){
        Object evt = event.getSource();
        if(evt.equals(btnAtras)){
            agenciaCliente.loadStage("/ventanas/inicioCliente.fxml", event);
        }
    }

    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

}


