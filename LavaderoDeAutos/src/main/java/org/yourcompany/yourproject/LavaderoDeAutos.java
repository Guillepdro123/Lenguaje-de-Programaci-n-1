

package org.yourcompany.yourproject;

class Trabajador extends Thread {
    private String tarea;

    public Trabajador(String nombre, String tarea) {
        super(nombre); // Asignamos nombre al hilo
        this.tarea = tarea;
    }

    @Override
    public void run() {
        try {
            System.out.println(getName() + " Empieza la tarea: " + tarea);

            // Simula el tiempo que tarda en hacer la tarea
            Thread.sleep((int)(Math.random() * 2000) + 1000);

            System.out.println(getName() + " termino la tarea: " + tarea);
        } catch (InterruptedException e) {
            System.out.println(getName() + " fue interrumpido.");
        }
    }
}

public class LavaderoDeAutos {
    public static void main(String[] args) {
        System.out.println("=== Simulacion de un lavadero de autos ===");

        // Creamos tres trabajadores con distintas tareas
        Trabajador t1 = new Trabajador("Trabajador 1", "Lavar carroceria");
        Trabajador t2 = new Trabajador("Trabajador 2", "Aspirar interior");
        Trabajador t3 = new Trabajador("Trabajador 3", "Secar vehiculo");

        // Iniciamos los hilos
        t1.start();
        t2.start();
        t3.start();

        try {
            // join() asegura que el programa espere a todos
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            System.out.println("La simulacion fue interrumpida.");
        }

        System.out.println("=== El carro ya esta listo y limpio ===");
    }
}
