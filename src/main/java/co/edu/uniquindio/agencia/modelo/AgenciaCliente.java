package co.edu.uniquindio.agencia.modelo;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.enums.Clima;
import co.edu.uniquindio.agencia.enums.Estado;
import co.edu.uniquindio.agencia.enums.Idioma;
import co.edu.uniquindio.agencia.exceptions.*;
import co.edu.uniquindio.agencia.socket.Mensaje;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.java.Log;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

@Log
public class AgenciaCliente {
    private static final String HOST = "localhost";
    private static final int PUERTO = 1234;

    private static AgenciaCliente agenciaCliente;

    public static AgenciaCliente getInstance() {
        if(agenciaCliente == null){
            agenciaCliente= new AgenciaCliente();
        }

        return agenciaCliente;
    }

    public String registrarCliente(String identificacion, String nombre, String correo, String
            telefono, String direccion, ArrayList<Destino> busquedasDestinos) throws AtributoVacioException, InformacionRepetidaException {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se crea un cliente con los datos obtenidos desde la ventana
            Cliente cliente = Cliente.builder()
                    .identificacion(identificacion)
                    .nombre(nombre)
                    .correo(correo)
                    .telefono(telefono)
                    .direccion(direccion)
                    .busquedasDestinos(busquedasDestinos)
                    .build();


            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("agregarCliente")
                    .contenido(cliente).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();


            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se imprime la respuesta del servidor. Según la respuesta se podría lanzar una excepción
            return respuesta.toString();

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String actualizarCliente(String identificacion, String nombre, String correo, String
            telefono, String direccion, ArrayList<Destino> busquedasDestinos) throws AtributoVacioException, InformacionRepetidaException {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se crea un cliente con los datos obtenidos desde la ventana
            Cliente cliente = Cliente.builder()
                    .identificacion(identificacion)
                    .nombre(nombre)
                    .correo(correo)
                    .telefono(telefono)
                    .direccion(direccion)
                    .busquedasDestinos(busquedasDestinos)
                    .build();


            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("actualizarCliente")
                    .contenido(cliente).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();


            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se imprime la respuesta del servidor. Según la respuesta se podría lanzar una excepción
            return respuesta.toString();

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean verificarDatos(String nombre, String identificacion) throws AtributoVacioException{

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Cliente cliente = Cliente.builder()
                    .nombre(nombre)
                    .identificacion(identificacion)
                    .build();

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("verificarDatos")
                    .contenido(cliente)
                    .build());

            //Obtenemos la respuesta del servidor
            Mensaje respuestaMensaje = (Mensaje) in.readObject();

            if (respuestaMensaje.getTipo().equals("Error")) {
                throw new AtributoVacioException("El usuario y contrasena son obligatorios");
            }

            boolean respuesta = (Boolean) respuestaMensaje.getContenido();

            // Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            // Retornamos el contenido del mensaje de respuesta
            return respuesta;
        } catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Cliente> listarClientes(){

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("listarClientes").build() );

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se hace un casting de la respuesta Object a un ArrayList<Cliente>
            ArrayList<Cliente> list = (ArrayList<Cliente>) respuesta;

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se retorna a lista de clientes
            return list;
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Destino> listarDestino(){

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("listarDestinos").build() );

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se hace un casting de la respuesta Object a un ArrayList<Destino>
            ArrayList<Destino> list = (ArrayList<Destino>) respuesta;

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se retorna a lista de destinos
            return list;
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ArrayList<PaqueteTuristico> listarPaquetesTuristicos(){

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("listarPaquetesTuristicos").build() );

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se hace un casting de la respuesta Object a un ArrayList<PaqueteTuristico>
            ArrayList<PaqueteTuristico> list = (ArrayList<PaqueteTuristico>) respuesta;

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se retorna a lista de clientes
            return list;
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ArrayList<PaqueteTuristico> listarPaquetesTuristicosPorDestinos(){

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("listarPaquetesTuristicosPorDestinos").build() );

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se hace un casting de la respuesta Object a un ArrayList<PaqueteTuristico>
            ArrayList<PaqueteTuristico> list = (ArrayList<PaqueteTuristico>) respuesta;

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se retorna a lista de clientes
            return list;
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }



    public ArrayList<GuiaTuristico> listarGuiasTuristicos(){

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("listarGuiasTuristicos").build() );

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se hace un casting de la respuesta Object a un ArrayList<PaqueteTuristico>
            ArrayList<GuiaTuristico> list = (ArrayList<GuiaTuristico>) respuesta;

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se retorna a lista de clientes
            return list;
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void loadStage(String url, Event event){

        try {
            ((Node)(event.getSource())).getScene().getWindow().hide();

            Parent root = FXMLLoader.load(Objects.requireNonNull(AgenciaApp.class.getResource(url)));
            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Agencia De Viajes");
            newStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String setDestino(Destino destino){

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("setDestino")
                    .contenido(destino).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se retorna la respuesta
            return respuesta.toString();
        }catch (EOFException eofException) {
            // Manejo específico de EOFException
            log.warning("Se alcanzó el final del archivo de manera inesperada.");
        } catch (IOException | ClassNotFoundException e) {
            // Otras excepciones
            log.severe("Error de lectura: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    public String setPaqueteTuristico(PaqueteTuristico paqueteTuristico) {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("setPaqueteTuristico")
                    .contenido(paqueteTuristico).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se retorna la respuesta
            return respuesta.toString();
        } catch (EOFException eofException) {
            // Manejo específico de EOFException
            log.warning("Se alcanzó el final del archivo de manera inesperada.");
        } catch (IOException | ClassNotFoundException e) {
            // Otras excepciones
            log.severe("Error de lectura: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }


    public String setCliente(Cliente cliente){

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("setCliente")
                    .contenido(cliente).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se retorna la respuesta
            return respuesta.toString();
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String registrarDestino(String nombreDestino, String ciudad, String descripcion, String imagen, Clima clima, ArrayList<Integer> calificaciones, ArrayList<String> comentarios) throws AtributoVacioException, InformacionRepetidaException {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            ArrayList<String> imagenes = new ArrayList<>();
            imagenes.add(imagen);

            //Se crea un destino con los datos obtenidos desde la ventana
            Destino destino = Destino.builder()
                    .nombre(nombreDestino)
                    .ciudad(ciudad)
                    .descripcion(descripcion)
                    .imagenes(imagenes)
                    .clima(clima)
                    .calificaciones(calificaciones)
                    .comentarios(comentarios)
                    .build();


            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("agregarDestino")
                    .contenido(destino).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();


            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se imprime la respuesta del servidor. Según la respuesta se podría lanzar una excepción
            return respuesta.toString();

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String actualizarDestino(String nombreDestino, String ciudad, String descripcion, String imagen, Clima clima, ArrayList<Integer> calificaciones, ArrayList<String> comentarios) throws AtributoVacioException, InformacionRepetidaException {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            ArrayList<String> imagenes = new ArrayList<>();
            imagenes.add(imagen);

            //Se crea un destino con los datos obtenidos desde la ventana
            Destino destino = Destino.builder()
                    .nombre(nombreDestino)
                    .ciudad(ciudad)
                    .descripcion(descripcion)
                    .imagenes(imagenes)
                    .clima(clima)
                    .calificaciones(calificaciones)
                    .comentarios(comentarios)
                    .build();


            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("actualizarDestino")
                    .contenido(destino).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();


            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se imprime la respuesta del servidor. Según la respuesta se podría lanzar una excepción
            return respuesta.toString();

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Destino obtenerDestino(String nombreDestino) {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Destino destino = Destino.builder()
                    .nombre(nombreDestino)
                    .build();

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("obtenerDestino")
                    .contenido(destino).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            in.close();
            out.close();

            if (respuesta instanceof Destino) {
                // El servidor envió un objeto Destino
                return (Destino) respuesta;
            } else if (respuesta instanceof String) {
                return null;
            } else {
                throw new RuntimeException("Respuesta inesperada del servidor");
            }

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String eliminarDestino(String nombreDestino) {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Destino destino = Destino.builder()
                    .nombre(nombreDestino)
                    .build();

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("eliminarDestino")
                    .contenido(destino).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            in.close();
            out.close();

            return respuesta.toString();

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String registrarPaqueteTuristico(String nombre, ArrayList<Destino> destinos, String duracion, String serviciosAdicionales, float precio, int cupoMaximo, LocalDate fechaInicio, LocalDate fechaFin) throws AtributoVacioException, InformacionRepetidaException, AtributoNegativoException, FechaInvalidaException {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se crea un destino con los datos obtenidos desde la ventana
            PaqueteTuristico paqueteTuristico = PaqueteTuristico.builder()
                    .nombre(nombre)
                    .destinos(destinos)
                    .duracion(duracion)
                    .serviciosAdicionales(serviciosAdicionales)
                    .precio(precio)
                    .cupoMaximo(cupoMaximo)
                    .fechaInicio(fechaInicio)
                    .fechaFin(fechaFin)
                    .build();


            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("agregarPaqueteTuristico")
                    .contenido(paqueteTuristico).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se imprime la respuesta del servidor. Según la respuesta se podría lanzar una excepción
            return respuesta.toString();

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public PaqueteTuristico obtenerPaqueteTuristico(String nombrePaqueteTuristico) {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            PaqueteTuristico paqueteTuristico = PaqueteTuristico.builder()
                    .nombre(nombrePaqueteTuristico)
                    .build();

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("obtenerPaquete")
                    .contenido(paqueteTuristico).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            in.close();
            out.close();

            if (respuesta instanceof PaqueteTuristico) {
                // El servidor envió un objeto Paquete Turistico
                return (PaqueteTuristico) respuesta;
            } else if (respuesta instanceof String) {
                // El servidor envió un mensaje de error
                System.out.println("Mensaje de error del servidor: " + respuesta);
                return null;  // O realiza alguna acción adecuada para manejar el error
            } else {
                throw new RuntimeException("Respuesta inesperada del servidor");
            }

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException("Error al obtener el Paquete Turistico: " + e.getMessage());
        }
    }

    public String actualizarPaqueteTuristico(String nombre, ArrayList<Destino> destinos, String duracion, String serviciosAdicionales, float precio, int cupoMaximo, LocalDate fechaInicio, LocalDate fechaFin) throws AtributoVacioException, InformacionRepetidaException, AtributoNegativoException, FechaInvalidaException {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se crea un destino con los datos obtenidos desde la ventana
            PaqueteTuristico paqueteTuristico = PaqueteTuristico.builder()
                    .nombre(nombre)
                    .destinos(destinos)
                    .duracion(duracion)
                    .serviciosAdicionales(serviciosAdicionales)
                    .precio(precio)
                    .cupoMaximo(cupoMaximo)
                    .fechaInicio(fechaInicio)
                    .fechaFin(fechaFin)
                    .build();


            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("actualizarPaqueteTuristico")
                    .contenido(paqueteTuristico).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se imprime la respuesta del servidor. Según la respuesta se podría lanzar una excepción
            return respuesta.toString();

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String eliminarPaquete(String nombre) {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            PaqueteTuristico paqueteTuristico = PaqueteTuristico.builder()
                    .nombre(nombre)
                    .build();

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("eliminarPaqueteTuristico")
                    .contenido(paqueteTuristico).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            in.close();
            out.close();

            return respuesta.toString();

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String registrarGuiaTuristico(String nombre, String identificacion, ArrayList<Idioma> idiomas, float experiencia, ArrayList<Integer> calificaciones, ArrayList<String> comentarios) throws AtributoVacioException, InformacionRepetidaException, AtributoNegativoException {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se crea un destino con los datos obtenidos desde la ventana
            GuiaTuristico guiaTuristico = GuiaTuristico.builder()
                    .nombre(nombre)
                    .identificacion(identificacion)
                    .idiomas(idiomas)
                    .experiencia(experiencia)
                    .calificaciones(calificaciones)
                    .comentarios(comentarios)
                    .build();



            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("agregarGuiaTuristico")
                    .contenido(guiaTuristico).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se imprime la respuesta del servidor. Según la respuesta se podría lanzar una excepción
            return respuesta.toString();

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public GuiaTuristico obtenerGuiaTuristico(String identificacion) {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            GuiaTuristico guiaTuristico = GuiaTuristico.builder()
                    .identificacion(identificacion)
                    .build();

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("obtenerGuia")
                    .contenido(guiaTuristico).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            in.close();
            out.close();

            if (respuesta instanceof GuiaTuristico) {
                // El servidor envió un objeto Guia Turistico
                return (GuiaTuristico) respuesta;
            } else if (respuesta instanceof String) {
                // El servidor envió un mensaje de error
                System.out.println("Mensaje de error del servidor: " + respuesta);
                return null;
            } else {
                throw new RuntimeException("Respuesta inesperada del servidor");
            }

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException("Error al obtener el Guia Turistico: " + e.getMessage());
        }

    }

    public GuiaTuristico obtenerGuiaTuristicoNombre(String nombre) {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            GuiaTuristico guiaTuristico = GuiaTuristico.builder()
                    .nombre(nombre)
                    .build();

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("obtenerGuiaNombre")
                    .contenido(guiaTuristico).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            in.close();
            out.close();

            if (respuesta instanceof GuiaTuristico) {
                // El servidor envió un objeto Guia Turistico
                return (GuiaTuristico) respuesta;
            } else if (respuesta instanceof String) {
                // El servidor envió un mensaje de error
                System.out.println("Mensaje de error del servidor: " + respuesta);
                return null;
            } else {
                throw new RuntimeException("Respuesta inesperada del servidor");
            }

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException("Error al obtener el Guia Turistico: " + e.getMessage());
        }

    }

    public String actualizarGuiaTuristico(String nombre, String identificacion, ArrayList<Idioma> idiomas, float experiencia, ArrayList<Integer> calificaciones, ArrayList<String> comentarios) throws AtributoVacioException, InformacionRepetidaException, AtributoNegativoException {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se crea un destino con los datos obtenidos desde la ventana
            GuiaTuristico guiaTuristico = GuiaTuristico.builder()
                    .nombre(nombre)
                    .identificacion(identificacion)
                    .idiomas(idiomas)
                    .experiencia(experiencia)
                    .calificaciones(calificaciones)
                    .comentarios(comentarios)
                    .build();



            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("actualizarGuiaTuristico")
                    .contenido(guiaTuristico).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se imprime la respuesta del servidor. Según la respuesta se podría lanzar una excepción
            return respuesta.toString();

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public String eliminarGuia(String identificacion) {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            GuiaTuristico guiaTuristico = GuiaTuristico.builder()
                    .identificacion(identificacion)
                    .build();

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("eliminarGuia")
                    .contenido(guiaTuristico).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            in.close();
            out.close();

            return respuesta.toString();

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Cliente getClienteAutenticado(){

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("getCliente").build() );

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se hace un casting de la respuesta Object
            Cliente cliente = (Cliente) respuesta;

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se retorna el cliente
            return cliente;
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public PaqueteTuristico getPaqueteTuristico(){

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("getPaqueteTuristico").build() );

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se hace un casting de la respuesta Object
            PaqueteTuristico paqueteTuristico = (PaqueteTuristico) respuesta;

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            return paqueteTuristico;
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public String registrarBusqueda(Destino destino) throws AtributoVacioException, InformacionRepetidaException {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se crea un cliente con los datos obtenidos desde la ventana
            BusquedasDestinos busquedasDestinos = BusquedasDestinos.builder()
                    .destino(destino)
                    .build();


            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("agregarBusqueda")
                    .contenido(busquedasDestinos).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();


            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se imprime la respuesta del servidor. Según la respuesta se podría lanzar una excepción
            return respuesta.toString();

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String registrarReserva(Cliente cliente, int cantidadDePersonas, PaqueteTuristico paqueteTuristico, GuiaTuristico guiaTuristico, Estado estado, LocalDate fechaDeSolicitud, LocalDate fechaDeViaje) throws CupoMaximoExcedidoException, FechaInvalidaException, AtributoVacioException, AtributoNegativoException, ReservaDuplicadaException{

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se crea un destino con los datos obtenidos desde la ventana
            Reserva reserva = Reserva.builder()
                    .Cliente(cliente)
                    .cantidadDePersonas(cantidadDePersonas)
                    .paqueteTuristico(paqueteTuristico)
                    .guiaTuristico(guiaTuristico)
                    .estado(estado)
                    .fechaDeSolicitud(fechaDeSolicitud)
                    .fechaDeViaje(fechaDeViaje)
                    .build();


            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("agregarReserva")
                    .contenido(reserva).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se imprime la respuesta del servidor. Según la respuesta se podría lanzar una excepción
            return respuesta.toString();

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Reserva> listarReservas(){

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("listarReservas").build() );

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se hace un casting de la respuesta Object a un ArrayList<Cliente>
            ArrayList<Reserva> list = (ArrayList<Reserva>) respuesta;

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se retorna a lista de clientes
            return list;
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ArrayList<BusquedasDestinos> listarBusquedas(){

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("listarBusquedas").build() );

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se hace un casting de la respuesta Object a un ArrayList<Cliente>
            ArrayList<BusquedasDestinos> list = (ArrayList<BusquedasDestinos>) respuesta;

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se retorna a lista de clientes
            return list;
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String cancelarReserva(Reserva reserva) throws CupoMaximoExcedidoException, FechaInvalidaException, AtributoVacioException, AtributoNegativoException, ReservaDuplicadaException {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(Mensaje.builder()
                    .tipo("cancelarReserva")
                    .contenido(reserva).build());

            Object respuesta = in.readObject();

            in.close();
            out.close();

            return respuesta.toString();
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }



    public String confirmarReserva(Reserva reserva) throws CupoMaximoExcedidoException, FechaInvalidaException, AtributoVacioException, AtributoNegativoException, ReservaDuplicadaException{

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());



            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("confirmarReserva")
                    .contenido(reserva).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se imprime la respuesta del servidor. Según la respuesta se podría lanzar una excepción
            return respuesta.toString();

        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String setReserva(Reserva reserva){

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("setReserva")
                    .contenido(reserva).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se retorna la respuesta
            return respuesta.toString();
        }catch (EOFException eofException) {
            // Manejo específico de EOFException
            log.warning("Se alcanzó el final del archivo de manera inesperada.");
        } catch (IOException | ClassNotFoundException e) {
            // Otras excepciones
            log.severe("Error de lectura: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    public String setReservas(ArrayList<Reserva> reservas){

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("setReservas")
                    .contenido(reservas).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se retorna la respuesta
            return respuesta.toString();
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Reserva getReserva(){

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("getReserva").build() );

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se hace un casting de la respuesta Object
            Reserva reserva = (Reserva) respuesta;

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            return reserva;
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Destino getDestino(){

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("getDestino").build() );

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se hace un casting de la respuesta Object
            Destino destino= (Destino) respuesta;

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            return destino;
        }catch (EOFException eofException) {
            // Manejo específico de EOFException
            log.warning("Se alcanzó el final del archivo de manera inesperada.");
        } catch (IOException | ClassNotFoundException e) {
            // Otras excepciones
            log.severe("Error de lectura: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

}