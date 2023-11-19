package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.modelo.AgenciaCliente;
import co.edu.uniquindio.agencia.modelo.PaqueteTuristico;
import co.edu.uniquindio.agencia.modelo.Propiedades;
import co.edu.uniquindio.agencia.modelo.Reserva;
import co.edu.uniquindio.agencia.utils.CambioIdiomaEvent;
import co.edu.uniquindio.agencia.utils.CambioIdiomaListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.*;

public class EstadisticasPaquetesControlador implements Initializable, CambioIdiomaListener {

    @FXML
    private BarChart<String, Number> graficoPaquetes;
    @FXML
    private Button btnAtras;

    public AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();
    private final Propiedades propiedades = Propiedades.getInstance();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización normal del controlador

        // Registra este controlador como un escuchador de cambios de idioma
        Propiedades.getInstance().addCambioIdiomaListener(this);

        // Actualiza las cadenas de texto según el idioma actual
        actualizarTextos();
        // Obtener la lista de reservas
        List<Reserva> reservas = agenciaCliente.listarReservas();

        // Contar la cantidad de veces que se reserva cada paquete
        Map<String, Integer> contadorPaquetes = new HashMap<>();
        for (Reserva reserva : reservas) {
            List<PaqueteTuristico> paqueteTuristicos = new ArrayList<>();
            PaqueteTuristico paquete = reserva.getPaqueteTuristico();
            paqueteTuristicos.add(paquete);
            for (PaqueteTuristico paqueteTuristico : paqueteTuristicos) {
                contadorPaquetes.put(paqueteTuristico.getNombre(), contadorPaquetes.getOrDefault(paqueteTuristico.getNombre(), 0) + 1);
            }
        }

        // Crear una serie de datos para el gráfico
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (String paquete : contadorPaquetes.keySet()) {
            series.getData().add(new XYChart.Data<>(paquete, contadorPaquetes.get(paquete)));
        }

        // Configurar el gráfico
        graficoPaquetes.getData().add(series);
        graficoPaquetes.setTitle("Paquetes más Reservados");
        CategoryAxis xAxis = (CategoryAxis) graficoPaquetes.getXAxis();
        xAxis.setLabel("Paquete");
        NumberAxis yAxis = (NumberAxis) graficoPaquetes.getYAxis();
        yAxis.setLabel("Cantidad de Paquetes");
    }
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        // Se llama cuando se cambia el idioma

        // Actualiza las cadenas de texto según el nuevo idioma
        actualizarTextos();
    }

    private void actualizarTextos() {
        btnAtras.setText(propiedades.getResourceBundle().getString("TextoAtras"));

    }

    public void atras(ActionEvent event){
        Object evt = event.getSource();
        if(evt.equals(btnAtras)){
            agenciaCliente.loadStage("/ventanas/inicioAdmin.fxml", event);
        }
    }
}