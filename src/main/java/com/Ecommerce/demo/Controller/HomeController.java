package com.Ecommerce.demo.Controller;

import com.Ecommerce.demo.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public ApiResponse Home(){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("this is the api response");
        return apiResponse;
    }
}
