import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.Scanner;

public class weatherApp {
    public static void main(String[] args) {
        String tempUnits = "Fahrenheit";
        String tempString = "&temperature_unit=fahrenheit";
        boolean proceed = true;
        Scanner input = new Scanner(System.in);

        while (proceed) {
            String instructions = "\nPlease type the number of an available option below:\n" +
                    " 1. Set Location\n" +
                    " 2. Change Temperature Units (currently set to " + tempUnits + ")\n" +
                    " 3. Check Weather\n" +
                    " 4. Exit\n" +
                    "Enter a number: \n";
            System.out.println(instructions);
            int decision = input.nextInt();

            switch (decision) {
                case 1: // Set location
                    System.out.println("I haven't set this up to return a set of coordinates from an entered location yet :)");
                    break;
                case 2: // Reverse temperature units
                    if (tempUnits.equals("Celsius")) {
                        tempUnits = "Fahrenheit";
                        tempString = "&temperature_unit=fahrenheit";
                    } else {
                        tempUnits = "Celsius";
                        tempString = "";
                    }
                    System.out.println("Temperature Units changed to: " + tempUnits);
                    break;
                case 3: // Call getWeather()
                    final String currentTempUnits = tempUnits; // Make a final copy
                    getWeather(tempString)
                        .thenAccept(response -> {
                            //These next few lines created with help from ChatGPT
                            WeatherResponse weatherResponse = new Gson().fromJson(response, WeatherResponse.class);
                            List<String> times = weatherResponse.hourly.time;
                            List<Double> temperatures = weatherResponse.hourly.temperature2m;

                            for (int i = 0; i < times.size(); i++) {
                                System.out.println("Time: " + times.get(i) + " - Temperature: " + temperatures.get(i) + "° " + currentTempUnits);
                            }
                        })
                        .exceptionally(ex -> {
                            System.err.println("Failed to fetch data: " + ex.getMessage());
                            return null;
                        })
                        .join();
                        proceed = false;
                    break;
                case 4: // Exit the program loop
                    proceed = false;
                    break;
                default:
                    System.out.println("Invalid selection, please try again.");
                    break;
            }
        }
        input.close();
    }

    /************************************************************
     * getWeather: handles the API communication with open-meteo
     * @return CompletableFuture containing an API response
     ************************************************************/
    public static CompletableFuture<String> getWeather(String tempString) {
        try {
            // Initialize the client
            HttpClient client = HttpClient.newHttpClient();

            // Construct the request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.open-meteo.com/v1/forecast?latitude=40.3916&longitude=-111.8508&hourly=temperature_2m" + tempString))
                .GET()
                .build();

            // Returns the http response I receive after the client sends its request to open-meteo
            return client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body);

        // In the event of an exception, return the exception
        } catch(Exception e) {
            CompletableFuture<String> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }

    public static class WeatherResponse {
        @SerializedName("latitude")
        public double latitude;

        @SerializedName("longitude")
        public double longitude;

        @SerializedName("generationtime_ms")
        public double generationTimeMs;

        @SerializedName("utc_offset_seconds")
        public int utcOffsetSeconds;

        @SerializedName("timezone")
        public String timezone;

        @SerializedName("timezone_abbreviation")
        public String timezoneAbbreviation;

        @SerializedName("elevation")
        public double elevation;

        @SerializedName("hourly_units")
        public HourlyUnits hourlyUnits;

        @SerializedName("hourly")
        public Hourly hourly;

        public static class HourlyUnits {
            @SerializedName("time")
            public String time;

            @SerializedName("temperature_2m")
            public String temperature2m;
        }

        public static class Hourly {
            @SerializedName("time")
            public List<String> time;

            @SerializedName("temperature_2m")
            public List<Double> temperature2m;
        }
    }
}
