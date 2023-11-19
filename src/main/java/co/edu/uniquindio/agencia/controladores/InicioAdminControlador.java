package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.modelo.AgenciaCliente;
import co.edu.uniquindio.agencia.modelo.Propiedades;
import co.edu.uniquindio.agencia.utils.CambioIdiomaEvent;
import co.edu.uniquindio.agencia.utils.CambioIdiomaListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class InicioAdminControlador implements Initializable, CambioIdiomaListener {

    @FXML
    private Button btnCrearDestino, btnActualizarDestino, btnCrearPaquete, btnActualizarPaquete, btnCrearGuiaTuristico;

    @FXML
    private Button btnActualizarGuia, btnEstadisticas, btnEstadisticasGuias, btnEstadisticasPaquetes, btnEstadisticasDestinos;

    @FXML
    private Button btnClientes, btnDestinos, btnPaquetes, btnGuias, btnSalir;

    private final AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();
    private final Propiedades propiedades = Propiedades.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización normal del controlador

        // Registra este controlador como un escuchador de cambios de idioma
        Propiedades.getInstance().addCambioIdiomaListener(this);

        // Actualiza las cadenas de texto según el idioma actual
        actualizarTextos();
    }

    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        // Se llama cuando se cambia el idioma

        // Actualiza las cadenas de texto según el nuevo idioma
        actualizarTextos();
    }

    private void actualizarTextos() {
        btnCrearDestino.setText(propiedades.getResourceBundle().getString("TextoCrearDestino"));
        btnActualizarDestino.setText(propiedades.getResourceBundle().getString("TextoActualizarDestino"));
        btnCrearPaquete.setText(propiedades.getResourceBundle().getString("TextoCrearPaquete"));
        btnActualizarPaquete.setText(propiedades.getResourceBundle().getString("TextoActualizarPaquete"));
        btnCrearGuiaTuristico.setText(propiedades.getResourceBundle().getString("TextoCrearGuia"));
        btnActualizarGuia.setText(propiedades.getResourceBundle().getString("TextoActualizarGuia1"));
        btnClientes.setText(propiedades.getResourceBundle().getString("TextoReporteClientes"));
        btnDestinos.setText(propiedades.getResourceBundle().getString("TextoReporteDestinos"));
        btnPaquetes.setText(propiedades.getResourceBundle().getString("TextoReportePaquetes"));
        btnGuias.setText(propiedades.getResourceBundle().getString("TextoReporteGuias"));
        btnEstadisticas.setText(propiedades.getResourceBundle().getString("TextoEstadisticas"));
        btnEstadisticasGuias.setText(propiedades.getResourceBundle().getString("TextoEstadisticasGuias"));
        btnEstadisticasPaquetes.setText(propiedades.getResourceBundle().getString("TextoEstadisticasPaquetes"));
        btnEstadisticasDestinos.setText(propiedades.getResourceBundle().getString("TextoEstadisticasDestinos"));
        btnSalir.setText(propiedades.getResourceBundle().getString("TextoSalir"));
    }

    public void ingresarCrearDestino(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnCrearDestino)) {
            agenciaCliente.loadStage("/ventanas/crearDestino.fxml", event);
        }
    }

    public void ingresarActualizarDestino(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnActualizarDestino)) {
            agenciaCliente.loadStage("/ventanas/modificarDestino.fxml", event);
        }
    }

    public void ingresarCrearPaquete(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnCrearPaquete)) {
            agenciaCliente.loadStage("/ventanas/crearPaquete.fxml", event);
        }
    }

    public void ingresarActualizarPaquete(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnActualizarPaquete)) {
            agenciaCliente.loadStage("/ventanas/modificarPaquetesTuristicos.fxml", event);
        }
    }

    public void ingresarCrearGuia(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnCrearGuiaTuristico)) {
            agenciaCliente.loadStage("/ventanas/crearGuiaTuristico.fxml", event);
        }
    }

    public void ingresarActualizarGuia(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnActualizarGuia)) {
            agenciaCliente.loadStage("/ventanas/modificarGuia.fxml", event);
        }
    }

    public void ingresarReporteClientes(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnClientes)) {
            agenciaCliente.loadStage("/ventanas/clientes.fxml", event);
        }
    }

    public void ingresarReporteDestinos(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnDestinos)) {
            agenciaCliente.loadStage("/ventanas/destinos.fxml", event);
        }
    }

    public void ingresarReportePaquetes(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnPaquetes)) {
            agenciaCliente.loadStage("/ventanas/paquetes.fxml", event);
        }
    }

    public void ingresarReporteGuias(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnGuias)) {
            agenciaCliente.loadStage("/ventanas/guias.fxml", event);
        }
    }

    public void irEstadisticas(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnEstadisticas)) {
            agenciaCliente.loadStage("/ventanas/estadisticas.fxml", event);
        }
    }

    public void irGuias(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnEstadisticasGuias)) {
            agenciaCliente.loadStage("/ventanas/estadisticasGuias.fxml", event);
        }
    }

    public void irPaquetes(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnEstadisticasPaquetes)) {
            agenciaCliente.loadStage("/ventanas/estadisticasPaquetes.fxml", event);
        }

    }

    public void irDestinos(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnEstadisticasDestinos)) {
            agenciaCliente.loadStage("/ventanas/estadisticasReservas.fxml", event);
        }
    }
    public void salir(ActionEvent event){

        Object evt = event.getSource();

        if(evt.equals(btnSalir)){
            agenciaCliente.loadStage("/ventanas/login.fxml", event);
        }
    }
}
