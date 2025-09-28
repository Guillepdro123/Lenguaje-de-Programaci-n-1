
package calculadorarmi;

import java.rmi.Naming;

public class Servidor {
    public static void main(String[] args) {
        try {
            CalculadoraImpl calc = new CalculadoraImpl();
            Naming.rebind("rmi://localhost/CalculadoraService", calc);
            System.out.println("Servidor RMI de Calculadora listo y en espera...");
        } catch (Exception e) {
            System.out.println("Error en el servidor: " + e);
        }
    }
}

