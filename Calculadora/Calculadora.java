package Calculadora;

public class Calculadora {
    
    
    public double somar(double a, double b) {
        return a + b;
    }
    
   
    public double subtrair(double a, double b) {
        return a - b;
    }
    
 
    public double multiplicar(double a, double b) {
        return a * b;
    }
    
    
    public double dividir(double a, double b) throws ArithmeticException {
        if (b == 0) {
            throw new ArithmeticException("Divisão por zero não é permitida");
        }
        return a / b;
    }
    
    public double calcular(String operacao, double a, double b) {
        switch (operacao) {
            case "+":
                return somar(a, b);
            case "-":
                return subtrair(a, b);
            case "*":
            case "x":
                return multiplicar(a, b);
            case "/":
            case ":":
                return dividir(a, b);
            default:
                throw new IllegalArgumentException("Operação '" + operacao + "' não suportada");
        }
    }
}