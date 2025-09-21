
package subasta;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ServidorSubasta {
    // Puerto donde escuchará el servidor
    private static final int PUERTO = 5000;

    // Variables compartidas entre todos los clientes
    private static volatile int ofertaMaxima = 0;        // La oferta más alta
    private static volatile String mejorPostor = "Nadie"; // Quién hizo la mejor oferta

    // Pool de hilos para manejar múltiples clientes concurrentes
    private static ExecutorService pool = Executors.newCachedThreadPool();

    // Temporizador de la subasta
    private static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledFuture<?> temporizador;

    public static void main(String[] args) {
        System.out.println("Servidor de Subasta iniciado en el puerto " + PUERTO);

        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            while (true) {
                // Espera que se conecte un cliente
                Socket cliente = servidor.accept();
                System.out.println("Nuevo cliente conectado: " + cliente.getInetAddress());

                // Cada cliente se maneja con un hilo del pool
                pool.execute(new ManejadorCliente(cliente));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clase que maneja la lógica de cada cliente
    static class ManejadorCliente implements Runnable {
        private Socket socket;

        public ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                // Se solicita nombre al cliente
                out.println("Bienvenido a la Subasta! Ingresa tu nombre:");
                String nombre = in.readLine();

                // Se le avisa que ya puede pujar
                out.println("Hola " + nombre +
                    ". La subasta ha iniciado, haz tu oferta con un número entero.");

                String mensaje;
                while ((mensaje = in.readLine()) != null) {
                    try {
                        int oferta = Integer.parseInt(mensaje); // Convertimos la oferta a número

                        synchronized (ServidorSubasta.class) {
                            if (oferta > ofertaMaxima) {
                                // Nueva oferta más alta
                                ofertaMaxima = oferta;
                                mejorPostor = nombre;
                                System.out.println("Nueva oferta: " + nombre + " ofertó " + oferta);
                                out.println("¡Tu oferta es la más alta con $" + oferta + "!");

                                // Reiniciar el temporizador (10s)
                                if (temporizador != null && !temporizador.isDone()) {
                                    temporizador.cancel(false);
                                }
                                temporizador = scheduler.schedule(() -> {
                                    System.out.println("\n🏆 La subasta terminó. Ganador: " +
                                            mejorPostor + " con $" + ofertaMaxima);
                                    System.exit(0); // Cierra el servidor
                                }, 10, TimeUnit.SECONDS);

                            } else {
                                out.println("Tu oferta de $" + oferta +
                                        " no supera la actual de $" + ofertaMaxima +
                                        " de " + mejorPostor);
                            }
                        }
                    } catch (NumberFormatException e) {
                        out.println("Por favor ingresa un número válido.");
                    }
                }
            } catch (IOException e) {
                System.out.println("Cliente desconectado.");
            }
        }
    }
}
