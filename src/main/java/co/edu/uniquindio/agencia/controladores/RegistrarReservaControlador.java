package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.exceptions.*;
import co.edu.uniquindio.agencia.modelo.AgenciaCliente;
import co.edu.uniquindio.agencia.modelo.Cliente;
import co.edu.uniquindio.agencia.modelo.PaqueteTuristico;
import co.edu.uniquindio.agencia.modelo.Reserva;
import co.edu.uniquindio.agencia.modelo.GuiaTuristico;
import co.edu.uniquindio.agencia.enums.Estado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RegistrarReservaControlador implements Initializable {

    @FXML
    private ComboBox<String> comboPaquetes;

    @FXML
    private ComboBox<String> comboGuias;

    @FXML
    private TextField txtCantidadPersonas;

    @FXML
    private DatePicker dateFechaViaje;

    @FXML
    private Button btnRegistrar, btnRegresar;

    private final AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();

    private Cliente cliente;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCliente(cliente);
        cargarPaquetes();
        cargarGuias();
    }


    public void setCliente(Cliente cliente) {
        this.cliente = agenciaCliente.getClienteAutenticado();
    }

    public void cargarPaquetes() {
        ObservableList<String> nombresPaquetes = FXCollections.observableArrayList();

        for (PaqueteTuristico paquete : agenciaCliente.listarPaquetesTuristicos()) {
            nombresPaquetes.add(paquete.getNombre());
        }

        comboPaquetes.setItems(nombresPaquetes);
    }

    public void cargarGuias() {
        ObservableList<String> nombresGuias = FXCollections.observableArrayList();

        for (GuiaTuristico guia : agenciaCliente.listarGuiasTuristicos()) {
            nombresGuias.add(guia.getNombre());
        }

        comboGuias.setItems(nombresGuias);
    }


    public void registrarReserva(ActionEvent actionEvent) {
        try {
            PaqueteTuristico paqueteSeleccionado = agenciaCliente.obtenerPaqueteTuristico(comboPaquetes.getValue());
            GuiaTuristico guiaSeleccionado = agenciaCliente.obtenerGuiaTuristicoNombre(comboGuias.getValue());
            int cantidadPersonas = Integer.parseInt(txtCantidadPersonas.getText());
            LocalDate fechaSolicitud = LocalDate.now();
            LocalDate fechaViaje = dateFechaViaje.getValue();

            String mensaje = agenciaCliente.registrarReserva(
                    cliente,
                    cantidadPersonas,
                    paqueteSeleccionado,
                    guiaSeleccionado,
                    Estado.PENDIENTE,
                    fechaSolicitud,
                    fechaViaje
            );

            agenciaCliente.actualizarPaqueteTuristico(paqueteSeleccionado.getNombre(), paqueteSeleccionado.getDestinos(), paqueteSeleccionado.getDuracion(), paqueteSeleccionado.getServiciosAdicionales(), paqueteSeleccionado.getPrecio(), (paqueteSeleccionado.getCupoMaximo() - cantidadPersonas), paqueteSeleccionado.getFechaInicio(), paqueteSeleccionado.getFechaFin());

            mostrarMensaje(Alert.AlertType.INFORMATION, mensaje);

        } catch (AtributoVacioException | CupoMaximoExcedidoException | FechaInvalidaException |
                 AtributoNegativoException | ReservaDuplicadaException | InformacionRepetidaException e) {
            mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    public void regresarInicio(ActionEvent event) {
        agenciaCliente.loadStage("/ventanas/inicioCliente.fxml", event);
    }

    public void mostrarMensaje(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}


