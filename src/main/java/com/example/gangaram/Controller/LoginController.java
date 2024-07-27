package com.example.gangaram.Controller;

import com.example.gangaram.Service.ArbitragService;
import com.example.gangaram.entity.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/upstox")
public class LoginController {
    @Autowired
    private final ArbitragService arbitragService;

    public LoginController(ArbitragService arbitragService) {
        this.arbitragService = arbitragService;
    }
    @PostMapping("/getAccessToken")
    public String getAccessToken(@RequestParam String authorizationCode) throws IOException {
        return arbitragService.getAccessToken(authorizationCode);
    }

    @GetMapping("/getStockPrice")
    public String getStockPrice(@RequestParam String BSCCompanyName,@RequestParam String NSCCompanyName, @RequestParam String accessToken) throws IOException, InterruptedException {
        return arbitragService.getRealTimeStockPrice(BSCCompanyName,NSCCompanyName, accessToken);
    }
}