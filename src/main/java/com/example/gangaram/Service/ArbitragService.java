package com.example.gangaram.Service;

import com.example.gangaram.entity.BSC;
import com.example.gangaram.entity.Login;
import com.example.gangaram.entity.NSC;
import com.example.gangaram.repository.BSCRepo;
import com.example.gangaram.repository.NSCRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArbitragService {

    @Autowired
    private BSCRepo BSCRepo;

    @Autowired
    private NSCRepo nscRepo;
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
            while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);
        } catch (IOException e) {
            throw new RuntimeException("Error reading the response: " + e.getMessage());
        }

        JSONObject jsonResponse = new JSONObject(response.toString());
//
        Login userResponse = new Login();
//        userResponse.setEmail(jsonResponse.optString("email"));
//        userResponse.setExchanges(jsonResponse.optJSONArray("exchanges").toList().stream().map(Object::toString).collect(Collectors.toList()));
//        userResponse.setProducts(jsonResponse.optJSONArray("products").toList().stream().map(Object::toString).collect(Collectors.toList()));
//        userResponse.setBroker(jsonResponse.optString("broker"));
//        userResponse.setUser_id(jsonResponse.optString("user_id"));
//        userResponse.setUser_name(jsonResponse.optString("user_name"));
//        userResponse.setOrder_types(jsonResponse.optJSONArray("order_types").toList().stream().map(Object::toString).collect(Collectors.toList()));
//        userResponse.setUser_type(jsonResponse.optString("user_type"));
//        userResponse.setPoa(jsonResponse.optBoolean("poa"));
//        userResponse.setIs_active(jsonResponse.optBoolean("is_active"));
        userResponse.setAccess_token(jsonResponse.optString("access_token"));
//        userResponse.setExtended_token(jsonResponse.optString("extended_token"));

        return userResponse.getAccess_token();
    }

    public String getRealTimeStockPrice(String BSCCompanyName,String NSCCompanyName, String accessToken) throws IOException, InterruptedException {

//        List<BSC> bsc= BSCRepo.findInstrumentKeysByName(BSCCompanyName);
//        List<NSC> nsc= nscRepo.findInstrumentKeysByName(NSCCompanyName);
//        if(bsc.size()==0)
//            return "Why";
//        String bscInstrumentKey = bsc.get(0).getInstrument_Key();
//        String nscInstrumentKey = nsc.get(0).getInstrument_Key();

        String bscEncodedKey = URLEncoder.encode("BSE_EQ|INE002A01018", StandardCharsets.UTF_8);
        String nscEncodedKey = URLEncoder.encode("NSE_EQ|INE002A01018", StandardCharsets.UTF_8);

        String bscURL = "https://api.upstox.com/v2/market-quote/ltp?instrument_key=" + bscEncodedKey;
        String nscURL = "https://api.upstox.com/v2/market-quote/ltp?instrument_key=" + nscEncodedKey;
        String acceptHeader = "application/json";
        String authorizationHeader = "Bearer "+ accessToken;

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest bschttpRequest = HttpRequest.newBuilder()
                .uri(URI.create(bscURL))
                .header("Accept", acceptHeader)
                .header("Authorization", authorizationHeader)
                .build();

        HttpClient nschttpClient = HttpClient.newHttpClient();
        HttpRequest nschttpRequest = HttpRequest.newBuilder()
                .uri(URI.create(nscURL))
                .header("Accept", acceptHeader)
                .header("Authorization", authorizationHeader)
                .build();

        for(int i=0;i<100;i++) {
            HttpResponse<String> bscResponse = httpClient.send(bschttpRequest, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> nscResponse = nschttpClient.send(nschttpRequest, HttpResponse.BodyHandlers.ofString());


//            int statusCode = nscResponse.statusCode();
//            HttpHeaders headers = nscResponse.headers();
            String nscResponseBody = nscResponse.body();

            String bscResponseBody = bscResponse.body();

            double nscPrice = extractLastPrice(nscResponseBody);
            double bscPrice = extractLastPrice(bscResponseBody);

            double diff=Math.abs(nscPrice-bscPrice);
            if(diff>0){
                if(nscPrice>bscPrice)
                    System.out.println("Buy on BSC and Sell on NSC");
                else
                    System.out.println("Buy on NSC and sell on BSC");
                System.out.println("NSC Price="+nscPrice+"  BSC Price="+bscPrice+" differnce="+diff);
                break;
            }

            System.out.println("BSC =" + " " + bscPrice + " " + "NSC =" + " " + nscPrice + " " + "Differnce=" + " " + Math.abs(nscPrice - bscPrice));
        }
        return "Done";
    }


    private double extractLastPrice(String responseBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode dataNode = rootNode.path("data");

        if (dataNode.isMissingNode()) {
            throw new IOException("Missing 'data' node in the response");
        }

        // Assuming there is only one key inside "data"
        JsonNode stockNode = dataNode.elements().next();
        JsonNode lastPriceNode = stockNode.path("last_price");

        if (lastPriceNode.isMissingNode()) {
            throw new IOException("Missing 'last_price' node in the response");
        }

        return lastPriceNode.asDouble();
    }
}

