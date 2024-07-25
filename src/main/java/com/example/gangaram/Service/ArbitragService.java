package com.example.gangaram.Service;

import com.example.gangaram.entity.Login;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
public class ArbitragService {
    public String getAccessToken(String code) throws IOException {
        String apiUrl = "https://api.upstox.com/v2/login/authorization/token";
        HttpURLConnection con = (HttpURLConnection) new URL(apiUrl).openConnection();

        // Set the request method
        con.setRequestMethod("POST");

        // Set the request headers
        con.setRequestProperty("accept", "application/json");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // Enable input/output streams
        con.setDoOutput(true);

        // Set the request data
        String data = "code=" + code +
                "&client_id=c55e77ab-9ec8-4b12-b82d-3224a8320b16" +
                "&client_secret=xs655bpf7g" +
                "&redirect_uri=https://127.0.0.1:3009/" +
                "&grant_type=authorization_code";

        // Write the request data to the output stream
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.write(data.getBytes(StandardCharsets.UTF_8));
            wr.flush();
        }

        // Get the response code
        int responseCode = con.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw new RuntimeException("Unauthorized: Check your credentials or token.");
        } else if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Request failed: " + responseCode);
        }

        StringBuilder response = new StringBuilder();
        // Read the response
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading the response: " + e.getMessage());
        }

        // Parse the JSON response and populate the UserResponse entity
        JSONObject jsonResponse = new JSONObject(response.toString());

        Login userResponse = new Login();
        userResponse.setEmail(jsonResponse.optString("email"));
        userResponse.setExchanges(jsonResponse.optJSONArray("exchanges").toList().stream().map(Object::toString).collect(Collectors.toList()));
        userResponse.setProducts(jsonResponse.optJSONArray("products").toList().stream().map(Object::toString).collect(Collectors.toList()));
        userResponse.setBroker(jsonResponse.optString("broker"));
        userResponse.setUser_id(jsonResponse.optString("user_id"));
        userResponse.setUser_name(jsonResponse.optString("user_name"));
        userResponse.setOrder_types(jsonResponse.optJSONArray("order_types").toList().stream().map(Object::toString).collect(Collectors.toList()));
        userResponse.setUser_type(jsonResponse.optString("user_type"));
        userResponse.setPoa(jsonResponse.optBoolean("poa"));
        userResponse.setIs_active(jsonResponse.optBoolean("is_active"));
        userResponse.setAccess_token(jsonResponse.optString("access_token"));
        userResponse.setExtended_token(jsonResponse.optString("extended_token"));

        return userResponse.getAccess_token();
    }

    public String getRealTimeStockPrice(String instrumentKey, String accessToken) throws IOException, InterruptedException {
//        instrumentKey = "BSE_EQ|INE917I01010";
        String encodedKey = URLEncoder.encode(instrumentKey, StandardCharsets.UTF_8);
        String url = "https://api.upstox.com/v2/market-quote/ltp?instrument_key=" + encodedKey;
//        String encodedInstrumentKey = URLEncoder.encode(instrumentKey, StandardCharsets.UTF_8.toString());
//        String url = "https://api.upstox.com/v2/market-quote/ltp?instrument_key=BSE_EQ%INE134E07323";
        String acceptHeader = "application/json";
        String authorizationHeader = "Bearer "+ accessToken;

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", acceptHeader)
                .header("Authorization", authorizationHeader)
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        HttpHeaders headers = response.headers();
        String responseBody = response.body();

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Headers: " + headers);
        System.out.println("Response Body: " + responseBody);
        return responseBody;
    }
}


//@Service
//public class UpstoxService {
//
//    public String getAccessToken(String authorizationCode) throws IOException {
//        String apiUrl = "https://api.upstox.com/index/oauth/token";
//        HttpURLConnection con = (HttpURLConnection) new URL(apiUrl).openConnection();
//
//        con.setRequestMethod("POST");
//        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        con.setDoOutput(true);
//
//        String data = "code=" + authorizationCode +
//                "&client_id=your_client_id" +
//                "&client_secret=your_client_secret" +
//                "&redirect_uri=your_redirect_uri" +
//                "&grant_type=authorization_code";
//
//        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
//            wr.write(data.getBytes(StandardCharsets.UTF_8));
//            wr.flush();
//        }
//
//        int responseCode = con.getResponseCode();
//        if (responseCode != 200) {
//            throw new RuntimeException("Failed to get access token: HTTP error code " + responseCode);
//        }
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuilder response = new StringBuilder();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//
//        JSONObject jsonResponse = new JSONObject(response.toString());
//        return jsonResponse.getString("access_token");
//    }
//
//    public String getRealTimeStockPrice(String instrumentKey, String accessToken) throws IOException {
//        String apiUrl = "https://api.upstox.com/index/market/quotes?exchange=NSE&symbol=" + instrumentKey;
//        HttpURLConnection con = (HttpURLConnection) new URL(apiUrl).openConnection();
//
//        con.setRequestMethod("GET");
//        con.setRequestProperty("Authorization", "Bearer " + accessToken);
//
//        int responseCode = con.getResponseCode();
//        if (responseCode != 200) {
//            throw new RuntimeException("Failed to get stock price: HTTP error code " + responseCode);
//        }
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuilder response = new StringBuilder();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//
//        return response.toString();
//    }
//}
