package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.modelo.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EstadisticasDestinosMasBuscadosControlador implements Initializable, CambioIdiomaListener {

    @FXML
    private BarChart<String, Number> graficoDestinos;
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
        List<BusquedasDestinos> busquedasDestinos = agenciaCliente.listarBusquedas();

        // Contar la cantidad de veces que se reserva cada destino
        Map<String, Integer> contadorDestinos = new HashMap<>();
        for (BusquedasDestinos busquedasDestinos1 : busquedasDestinos) {
            contadorDestinos.put(busquedasDestinos1.getDestino().getNombre(), contadorDestinos.getOrDefault(busquedasDestinos1.getDestino().getNombre(), 0) + 1);
        }

        // Crear una serie de datos para el gráfico
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (String destino : contadorDestinos.keySet()) {
            series.getData().add(new XYChart.Data<>(destino, contadorDestinos.get(destino)));
        }

        // Configurar el gráfico
        graficoDestinos.getData().add(series);
        graficoDestinos.setTitle("Destinos más Buscados");
        CategoryAxis xAxis = (CategoryAxis) graficoDestinos.getXAxis();
        xAxis.setLabel("Destino");
        NumberAxis yAxis = (NumberAxis) graficoDestinos.getYAxis();
        yAxis.setLabel("Cantidad de Destinos");
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