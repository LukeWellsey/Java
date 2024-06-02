import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

public class weatherApp {
    public static void main(String[] args) {
        //need to add a welcome prompt allowing user to confirm that they want to get weather
        //would be nice to figure out a way to allow user to select their location, we need coordinates.
        //maybe theres another API or open-meteo endpoint to return coordinates after entering a location?
        getWeather()
            .thenAccept(response -> {
                System.out.println("API Response: " + response);
                //need to add JSON parsing likely here?
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