package Monedas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) throws IOException, InterruptedException {
        // API base URL: URL completa para obtener las tasas de cambio desde la API
        String apiKey = "8157e33626a57e988b55857f"; // Clave personal de la API
        String baseCurrency = "USD"; // Moneda base para las conversiones
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + baseCurrency;

        // Configuración del cliente HTTP para enviar solicitudes a la API
        HttpClient client = HttpClient.newHttpClient();

        // Creación de la solicitud HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)) // URL de la API
                .GET() // Método HTTP GET para obtener datos
                .build();

        // Envío de la solicitud y obtención de la respuesta de la API
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body(); // Guardar el JSON devuelto por la API

        // Conversión del JSON recibido a una clase Java usando Gson
        Gson gson = new GsonBuilder().create();
        ValorDeLasMonedas elValorDeLasMonedas = gson.fromJson(json, ValorDeLasMonedas.class);

        // Configuración del menú interactivo
        Scanner scanner = new Scanner(System.in); // Escáner para capturar entradas del usuario
        boolean continuar = true; // Variable para controlar el ciclo del menú

        // Ciclo del menú principal
        while (continuar) {
            // Mostrar el menú usando un bloque de texto
            System.out.println("""
                    === Conversor de Monedas ===
                    Selecciona una opción:
                    1. Convertir USD ↔ ARS
                    2. Convertir USD ↔ BRL
                    3. Convertir USD ↔ EUR
                    4. Convertir USD ↔ COP
                    5. Convertir USD ↔ CLP
                    6. Convertir USD ↔ PEN
                    7. Salir
                    """);

            // Leer la opción seleccionada por el usuario
            int opcion = scanner.nextInt();

            // Manejo de las opciones del menú principal con un switch
            switch (opcion) {
                case 1 -> conversionBidireccional(scanner, elValorDeLasMonedas, "USD", "ARS");
                case 2 -> conversionBidireccional(scanner, elValorDeLasMonedas, "USD", "BRL");
                case 3 -> conversionBidireccional(scanner, elValorDeLasMonedas, "USD", "EUR");
                case 4 -> conversionBidireccional(scanner, elValorDeLasMonedas, "USD", "COP");
                case 5 -> conversionBidireccional(scanner, elValorDeLasMonedas, "USD", "CLP");
                case 6 -> conversionBidireccional(scanner, elValorDeLasMonedas, "USD", "PEN");
                case 7 -> {
                    continuar = false; // Salir del ciclo
                    System.out.println("Saliendo del sistema. ¡Gracias por usar el conversor de monedas!");
                }
                default -> System.out.println("Opción no válida. Por favor, selecciona una opción del menú.");
            }
        }

        scanner.close(); // Cerrar el escáner al finalizar
    }

    /**
     * Método para realizar conversiones bidireccionales entre dos monedas.
     *
     * @param scanner    Escáner para capturar entradas del usuario
     * @param tasas      Objeto con las tasas de cambio
     * @param moneda1    Código de la primera moneda (por ejemplo, USD)
     * @param moneda2    Código de la segunda moneda (por ejemplo, ARS)
     */
    private static void conversionBidireccional(Scanner scanner, ValorDeLasMonedas tasas, String moneda1, String moneda2) {
        System.out.println("Selecciona el tipo de conversión:");
        System.out.println("1. Convertir de " + moneda1 + " a " + moneda2);
        System.out.println("2. Convertir de " + moneda2 + " a " + moneda1);

        // Leer la opción seleccionada para elegir la dirección de la conversión
        int opcionConversion = scanner.nextInt();
        switch (opcionConversion) {
            case 1 -> realizarConversion(scanner, tasas, moneda1, moneda2); // De moneda1 a moneda2
            case 2 -> realizarConversion(scanner, tasas, moneda2, moneda1); // De moneda2 a moneda1
            default -> System.out.println("Opción no válida.");
        }
    }

    /**
     * Método para realizar una conversión entre dos monedas.
     *
     * @param scanner        Escáner para capturar entradas del usuario
     * @param tasas          Objeto que contiene las tasas de cambio
     * @param monedaOrigen   Código de la moneda de origen (por ejemplo, USD)
     * @param monedaDestino  Código de la moneda de destino (por ejemplo, ARS)
     */
    private static void realizarConversion(Scanner scanner, ValorDeLasMonedas tasas, String monedaOrigen, String monedaDestino) {
        // Obtener las tasas de cambio para las monedas seleccionadas
        Double tasaOrigen = tasas.conversion_rates().get(monedaOrigen);
        Double tasaDestino = tasas.conversion_rates().get(monedaDestino);

        // Validar que las tasas existan
        if (tasaOrigen == null || tasaDestino == null) {
            System.out.println("No se encontró información para las monedas seleccionadas.");
            return;
        }

        // Pedir al usuario el monto a convertir
        System.out.println("Introduce el monto en " + monedaOrigen + ":");
        double monto = scanner.nextDouble();

        // Calcular el monto convertido
        double resultado = (monto / tasaOrigen) * tasaDestino;
        System.out.printf("El monto equivalente en %s es: %.2f%n", monedaDestino, resultado);
    }
}






