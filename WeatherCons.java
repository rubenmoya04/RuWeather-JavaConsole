package PruebasTest.practicar.PruebasConAPI.WeatherConsult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherCons {
    public static void main(String[] args) {
        String apiKey = "a576559ed6e078c8e092c0602942a646";
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                menuApp();
                // Permitir nombres con espacios usando nextLine y codificar para URL
                String ciudad = sc.nextLine().trim();
                if (ciudad.equalsIgnoreCase("f")) {
                    System.out.println("Finalizando programa...");
                    break;
                }

                String url = "http://api.openweathermap.org/data/2.5/weather?q="
                        + ciudad.replace(" ", "%20")
                        + "&appid=" + apiKey + "&units=metric&lang=es";

                JSONObject json = requestHTTP(url);

                mostrarDatosClima(json);

                Thread.sleep(4000);

            } catch (IOException e) {
                System.out.println("❌ Error de conexión. Revisa tu internet o la URL.");
            } catch (InterruptedException e) {
                System.out.println("⏹️ La petición fue interrumpida.");
                Thread.currentThread().interrupt();
            } catch (JSONException e) {
                System.out.println("⚠️ Ciudad no encontrada o respuesta inválida de la API.");
            } catch (Exception e) {
                System.out.println("❗ Error inesperado: " + e.getMessage());
            }
        }
    }

    // Método para hacer la petición HTTP y devolver JSONObject
    public static JSONObject requestHTTP(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new JSONObject(response.body());
    }

    // Menú principal
    public static void menuApp() {
        System.out.println("══════════════════════════════════════════════");
        System.out.println("         ☀️️⛈️   RuWeather   ☀️⛈️ ️");
        System.out.println("══════════════════════════════════════════════");
        System.out.println(" · Si deseas salir escribe: F");
        System.out.print("📌 Ciudad: ");
    }

    // Mostrar datos de clima de manera organizada
    public static void mostrarDatosClima(JSONObject json) {
        String nombreCiudad = json.getString("name");
        String pais = json.getJSONObject("sys").getString("country");

        ZonedDateTime amanecer = Instant.ofEpochSecond(json.getJSONObject("sys").getInt("sunrise"))
                .atZone(ZoneId.systemDefault());
        ZonedDateTime atardecer = Instant.ofEpochSecond(json.getJSONObject("sys").getInt("sunset"))
                .atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        double temp = json.getJSONObject("main").getDouble("temp");
        double feels = json.getJSONObject("main").getDouble("feels_like");
        double humedad = json.getJSONObject("main").getDouble("humidity");
        String descripcion = json.getJSONArray("weather").getJSONObject(0).getString("description");
        double viento = json.getJSONObject("wind").getDouble("speed") * 3.6; // convertir m/s a km/h

        System.out.println("\n📍 " + nombreCiudad + " -- " + pais);
        if (temp < 18) System.out.println("❄️ Temperatura: " + temp + " ºC ❄️");
        else if (temp <= 22) System.out.println("☀️ Temperatura: " + temp + " ºC 🌞");
        else System.out.println("☀️ Temperatura: " + temp + " ºC 🔥");

        System.out.println("🌡️ Sensación térmica: " + feels + " ºC");
        System.out.println("💧 Humedad: " + humedad + " %");
        System.out.println("☁️ Descripción: " + descripcion);
        System.out.println("🌬️ Velocidad viento: " + viento + " km/h");

    }
}
