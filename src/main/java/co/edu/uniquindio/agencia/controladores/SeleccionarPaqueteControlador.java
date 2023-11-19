package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.exceptions.AtributoVacioException;
import co.edu.uniquindio.agencia.exceptions.InformacionRepetidaException;
import co.edu.uniquindio.agencia.modelo.*;
import co.edu.uniquindio.agencia.utils.CambioIdiomaEvent;
import co.edu.uniquindio.agencia.utils.CambioIdiomaListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SeleccionarPaqueteControlador implements Initializable, CambioIdiomaListener {

    @FXML
    private TextField filtroNombre;

    @FXML
    private TextField filtroDestino;

    @FXML
    private TextField filtroDuracion;

    @FXML
    private TextField filtroServiciosAdicionales;

    @FXML
    private TextField filtroPrecio;

    @FXML
    private TextField filtroCupoMaximo;

    @FXML
    private TextField filtroFechaInicio;

    @FXML
    private TextField filtroFechaFin;

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
    private Button btnDetallesPaquete, btnAtras;

    private ObservableList<PaqueteTuristico> paquetesList = FXCollections.observableArrayList();

    private final AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();
    private final Propiedades propiedades = Propiedades.getInstance();

    private PaqueteTuristico paqueteSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización normal del controlador

        // Registra este controlador como un escuchador de cambios de idioma
        Propiedades.getInstance().addCambioIdiomaListener(this);

        // Actualiza las cadenas de texto según el idioma actual
        actualizarTextos();

        cargarPaquetes();

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
        columnDestinos.setText(propiedades.getResourceBundle().getString("TextoDestinos"));
        columnCupoMaximo.setText(propiedades.getResourceBundle().getString("TextoCupoMaximo"));
        columnPrecio.setText(propiedades.getResourceBundle().getString("TextoPrecio"));
        columnFechaInicio.setText(propiedades.getResourceBundle().getString("TextoFechaInicio"));
        columnFechaFin.setText(propiedades.getResourceBundle().getString("TextoFechaFin"));
        columnServiciosAdicionales.setText(propiedades.getResourceBundle().getString("TextoServAdicionales"));
        btnDetallesPaquete.setText(propiedades.getResourceBundle().getString("TextoDetallesPaquete"));
        filtroNombre.setPromptText(propiedades.getResourceBundle().getString("TextoNombre"));
        filtroDestino.setPromptText(propiedades.getResourceBundle().getString("TextoDestinos"));
        filtroCupoMaximo.setPromptText(propiedades.getResourceBundle().getString("TextoCupoMaximo"));
        filtroDuracion.setPromptText(propiedades.getResourceBundle().getString("TextoDuracion"));
        filtroPrecio.setPromptText(propiedades.getResourceBundle().getString("TextoPrecio"));
        filtroServiciosAdicionales.setPromptText(propiedades.getResourceBundle().getString("TextoServAdicionales"));
        filtroFechaInicio.setPromptText(propiedades.getResourceBundle().getString("TextoFechaInicio"));
        filtroFechaFin.setPromptText(propiedades.getResourceBundle().getString("TextoFechaFin"));
        btnAtras.setText(propiedades.getResourceBundle().getString("TextoAtras"));
    }


    public void cargarPaquetes() {
        ArrayList<PaqueteTuristico> paquetes = agenciaCliente.listarPaquetesTuristicos();
        paquetesList.setAll(paquetes);
        tablaPaquetes.setItems(paquetesList);
    }

    @FXML
    private void filtrar() {
        String nombreFiltro = filtroNombre.getText().toLowerCase();
        String destinoFiltro = filtroDestino.getText().toLowerCase();
        String duracionFiltro = filtroDuracion.getText().toLowerCase();
        String serviciosAdicionalesFiltro = filtroServiciosAdicionales.getText().toLowerCase();
        String precioFiltro = filtroPrecio.getText().toLowerCase();
        String cupoMaximoFiltro = filtroCupoMaximo.getText().toLowerCase();
        String fechaInicioFiltro = filtroFechaInicio.getText().toLowerCase();
        String fechaFinFiltro = filtroFechaFin.getText().toLowerCase();

        ObservableList<PaqueteTuristico> paquetesFiltrados = FXCollections.observableArrayList();

        for (PaqueteTuristico paquete : paquetesList) {
            boolean cumpleFiltroNombre = paquete.getNombre().toLowerCase().contains(nombreFiltro);
            boolean cumpleFiltroDestino = paquete.getDestinos().stream().anyMatch(destino -> destino.getNombre().toLowerCase().contains(destinoFiltro));
            boolean cumpleFiltroDuracion = paquete.getDuracion().toLowerCase().contains(duracionFiltro);
            boolean cumpleFiltroServiciosAdicionales = paquete.getServiciosAdicionales().toLowerCase().contains(serviciosAdicionalesFiltro);
            boolean cumpleFiltroPrecio = String.valueOf(paquete.getPrecio()).toLowerCase().contains(precioFiltro);
            boolean cumpleFiltroCupoMaximo = String.valueOf(paquete.getCupoMaximo()).toLowerCase().contains(cupoMaximoFiltro);
            boolean cumpleFiltroFechaInicio = paquete.getFechaInicio().toString().toLowerCase().contains(fechaInicioFiltro);
            boolean cumpleFiltroFechaFin = paquete.getFechaFin().toString().toLowerCase().contains(fechaFinFiltro);

            if (cumpleFiltroNombre && cumpleFiltroDestino && cumpleFiltroDuracion && cumpleFiltroServiciosAdicionales &&
                    cumpleFiltroPrecio && cumpleFiltroCupoMaximo && cumpleFiltroFechaInicio && cumpleFiltroFechaFin) {
                paquetesFiltrados.add(paquete);
            }
        }

        tablaPaquetes.setItems(paquetesFiltrados);
    }

    @FXML
    private void mostrarDetalle(ActionEvent event) {
        Object evt = event.getSource();
        paqueteSeleccionado = tablaPaquetes.getSelectionModel().getSelectedItem();

        if (paqueteSeleccionado == null) {
            mostrarMensaje(Alert.AlertType.ERROR, "No se ha seleccionado ningún paquete.");
        }

        agenciaCliente.setPaqueteTuristico(paqueteSeleccionado);

        if (evt.equals(btnDetallesPaquete)) {
            agenciaCliente.loadStage("/ventanas/detallesPaquete.fxml", event);
        }
    }

    @FXML
    private void actualizarBusquedasDestinos() {
        Cliente clienteAutenticado = agenciaCliente.getClienteAutenticado();

        // Verificar si el cliente está autenticado
        if (clienteAutenticado != null) {
            String nombreDestino = filtroDestino.getText(); // Obtener el nombre del destino desde el TextField

            // Llamar a AgenciaDeViajes para obtener el destino
            Destino destino = agenciaCliente.obtenerDestino(nombreDestino);

            // Verificar si el destino se encontró
            if (destino != null) {
                // Obtener la lista de busquedasDestinos del cliente
                ArrayList<Destino> busquedasDestinos = clienteAutenticado.getBusquedasDestinos();

                // Verificar si la lista existe
                if (busquedasDestinos == null) {
                    busquedasDestinos = new ArrayList<>();
                }

                // Agregar el destino a la lista
                busquedasDestinos.add(destino);

                try {
                    // Actualizar el cliente con la nueva lista de busquedasDestinos
                    agenciaCliente.actualizarCliente(clienteAutenticado.getIdentificacion(),
                            clienteAutenticado.getNombre(),
                            clienteAutenticado.getCorreo(),
                            clienteAutenticado.getTelefono(),
                            clienteAutenticado.getDireccion(),
                            busquedasDestinos);

                    BusquedasDestinos busquedaDestino = new BusquedasDestinos(destino);
                    agenciaCliente.registrarBusqueda(busquedaDestino.getDestino());

                    mostrarMensaje(Alert.AlertType.INFORMATION, "Se ha guardado la busqueda");
                } catch (AtributoVacioException | InformacionRepetidaException e) {
                    e.printStackTrace();
                }
            } else {
                mostrarMensaje(Alert.AlertType.INFORMATION, "El destino no se encontro");
            }
        }
    }

    @FXML
    private void actualizarTabla(){
        ArrayList<PaqueteTuristico> paqueteTuristicos = agenciaCliente.listarPaquetesTuristicosPorDestinos();
        ObservableList<PaqueteTuristico> paquetesFiltrados = FXCollections.observableArrayList();
        paquetesFiltrados.setAll(paqueteTuristicos);
        tablaPaquetes.setItems(paquetesFiltrados);
    }



    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

    public void atras(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnAtras)) {
            agenciaCliente.loadStage("/ventanas/inicioCliente.fxml", event);
        }

    }

}
