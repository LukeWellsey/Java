import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.Scanner;

public class weatherApp {
    public static void main(String[] args) {
        //would be nice to figure out a way to allow user to select their location, we need coordinates.
        //maybe theres another API or open-meteo endpoint to return coordinates after entering a location?
        String tempUnits = "Celsius";
        String tempString = "";
        
        Scanner input = new Scanner(System.in);
        String instructions = "\nPlease type the number of an available option below:\n" + //
                " 1. Set Location\n" +              //Allow users to input a location instead of coordinates
                " 2. Change Temperature Units (currently set to: "+ tempUnits +")\n" +  //swap between Celcius and Fahrenheit 
                " 3. Check Weather\n" +               //run the getWeather helper method
                "Enter a number: ";                
        System.out.println(instructions);
        Integer decision = input.nextInt();
        
        switch (decision) {
            case 1:
                System.out.println("I haven't set this up to return a set of coordinates from an entered location yet :)");
                break;
            case 2:
                if (tempUnits.equals("Celsius")) {
                    tempUnits = "Fahrenheit";
                    tempString = "&temperature_unit=fahrenheit";
                } else {
                    tempUnits.equals("Celsius");
                    tempString = "";
                }
                System.out.println("tempUnits: " + tempUnits + "tempString" + tempString);
                break;
            case 3:

                break;
            default:
                System.out.println("Invalid selection, please try again.");
                break;
        }
        input.close();

        getWeather()
            .thenAccept(response -> {
                System.out.println("API Response: " + response);
                //need to add JSON parsing likely here? need to match timestamps to temperature next
            })
            .exceptionally(ex -> {
                System.err.println("Failed to fetch data: " + ex.getMessage());
                return null;
            })
            .join();
    }
    
    /************************************************************
     * getWeather: handles the API communication with open-meteo
     * @return CompletableFuture containing an API response
     ************************************************************/
    public static CompletableFuture<String> getWeather() {
        try {
            //initialize the client
            HttpClient client = HttpClient.newHttpClient();

            //construct the request
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.open-meteo.com/v1/forecast?latitude=40.3916&longitude=-111.8508&hourly=temperature_2m"))
            .GET()
            .build();

            //returns the http response I recieve after the client sends its request to open-meteo
            return client.sendAsync(request, BodyHandlers.ofString())
            .thenApply(HttpResponse::body);
            
            //in the event of an exception, return the exception
        } catch(Exception e) {
            CompletableFuture<String> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;        
        }
    }
}