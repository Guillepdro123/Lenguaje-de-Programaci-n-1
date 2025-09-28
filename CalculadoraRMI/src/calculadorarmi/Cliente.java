
package calculadorarmi;

import java.rmi.Naming;

public class Cliente {
    public static void main(String[] args) {
        try {
            Calculadora calc = (Calculadora) Naming.lookup("rmi://localhost/CalculadoraService");

            System.out.println("Prueba de operaciones con la calculadora remota:");
            System.out.println("5 + 3 = " + calc.sumar(5, 3));
            System.out.println("10 - 4 = " + calc.restar(10, 4));
            System.out.println("6 * 7 = " + calc.multiplicar(6, 7));
            System.out.println("20 / 5 = " + calc.dividir(20, 5));
        } catch (Exception e) {
            System.out.println("Error en el cliente: " + e);
        }
    }
}

