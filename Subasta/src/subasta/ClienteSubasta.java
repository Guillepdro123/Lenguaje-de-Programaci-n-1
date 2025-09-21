
package subasta;

import java.io.*;
import java.net.*;

public class ClienteSubasta {
    public static void main(String[] args) {
        String servidor = "localhost"; // Si los clientes están en otra PC, poner la IP del servidor
        int puerto = 5000;

        try (
            // Conexión al servidor
            Socket socket = new Socket(servidor, puerto);

            // Flujo de entrada: mensajes que llegan del servidor
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Flujo de salida: mensajes que enviamos al servidor
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Teclado del usuario
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String mensajeServidor;
            // Ciclo para leer mensajes del servidor
            while ((mensajeServidor = in.readLine()) != null) {
                System.out.println("Servidor: " + mensajeServidor);

                // Si el servidor espera un nombre o una oferta, el usuario escribe
                if (mensajeServidor.contains("nombre") || mensajeServidor.contains("oferta")) {
                    String input = teclado.readLine();
                    out.println(input); // Enviamos al servidor
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


