package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.modelo.AgenciaCliente;
import co.edu.uniquindio.agencia.modelo.GuiaTuristico;
import co.edu.uniquindio.agencia.modelo.Propiedades;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EstadisticasGuiaTuristicoControlador implements Initializable, CambioIdiomaListener {

    @FXML
    private BarChart<String, Number> graficoGuias;

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
        // Obtener la lista de guías turísticos
        List<GuiaTuristico> guias = agenciaCliente.listarGuiasTuristicos();

        // Calcular el promedio de calificaciones por guía
        Map<String, Double> promedioCalificaciones = new HashMap<>();
        for (GuiaTuristico guia : guias) {
            ArrayList<Integer> calificaciones = guia.getCalificaciones();
            if (!calificaciones.isEmpty()) {
                double promedio = calificaciones.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                promedioCalificaciones.put(guia.getNombre(), promedio);
            }
        }

        // Crear una serie de datos para el gráfico
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (String guia : promedioCalificaciones.keySet()) {
            series.getData().add(new XYChart.Data<>(guia, promedioCalificaciones.get(guia)));
        }

        // Configurar el gráfico
        graficoGuias.getData().add(series);
        graficoGuias.setTitle("Guías Turísticos Mejor Puntuados");
        CategoryAxis xAxis = (CategoryAxis) graficoGuias.getXAxis();
        xAxis.setLabel("Guía Turístico");
        NumberAxis yAxis = (NumberAxis) graficoGuias.getYAxis();
        yAxis.setLabel("Promedio de Calificaciones");
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

