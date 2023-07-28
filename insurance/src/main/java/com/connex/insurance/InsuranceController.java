package com.connex.insurance;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin
public class InsuranceController {

    @PostMapping("/request")
    Response newRequest(@RequestBody Insurance newRequest) {

        Response response = new Response();
        response.setSuccess("pending");
        var template = new RestTemplate();
        var premium_resp = template.getForEntity(
                "https://storage.googleapis.com/connex-th/insurance_assignment/base_premium.json", BasePremium.class);

        double default_base_premium = 1500;

        if (premium_resp.getBody() != null && premium_resp.getStatusCode() == HttpStatus.OK) {
            default_base_premium = premium_resp.getBody().base_premium;
        }
        double total_premium = default_base_premium;
        if (newRequest.getAge() < 25) {
            total_premium *= 1.3;
        } else if (newRequest.getAge() < 40) {
            total_premium *= 1;
        } else if (newRequest.getAge() < 70) {
            total_premium *= 0.9;
        } else {
            response.setMsg("age greater than 70");
            return response;
        }
        if (newRequest.getDriving_exp() < 2) {
            total_premium *= 1.5;
        } else if (newRequest.getDriving_exp() < 5) {
            total_premium *= 1.3;
        } else if (newRequest.getDriving_exp() < 10) {
            total_premium *= 1;
        } else {
            total_premium *= 0.9;
        }


        if (newRequest.getAccidents() == 0) {
            total_premium *= 1;
        } else if (newRequest.getAccidents() == 1) {
            total_premium *= 1.1;
        } else if (newRequest.getAccidents() <= 3) {
            total_premium *= 1.3;
        } else {
            response.setMsg("violation/accidents greater than 3");
            return response;
        }

        if (newRequest.getClaims() == 0) {
            total_premium *= 1;
        } else if (newRequest.getClaims() == 1) {
            total_premium *= 1.1;
        } else if (newRequest.getClaims() <= 3) {
            total_premium *= 1.3;
        } else {
            response.setMsg("claims greater than 3");
            return response;
        }

        double current_car_value = newRequest.getValue();
        if (newRequest.getAge() <= 3) {
            current_car_value -= current_car_value * (15/100.0) * newRequest.getAge();
        }
        newRequest.setAge(newRequest.getAge() - 3);

        if (newRequest.getAge() > 0) {
            current_car_value -= current_car_value * (10/100.0) * newRequest.getAge();
        }
        
        if (current_car_value < 30000){
            total_premium *= 0.8;
        }else if (current_car_value < 60000){
            total_premium *= 1;
        }else if (current_car_value < 100000){
            total_premium *= 1.2;
        }else if (current_car_value < 150000){
            total_premium *= 1.5;
        }else if (current_car_value < 200000){
            total_premium *= 2;
        }else{
            response.setMsg("current car value greater than $200,000");
            return response;
        }

        if (newRequest.getDistance() < 20000) {
            total_premium *= 0.9;
        } else if (newRequest.getDistance() < 30000) {
            total_premium *= 1;
        } else if (newRequest.getDistance() < 50000) {
            total_premium *= 1.1;
        } else {
            total_premium *= 1.3;
        }

        if (newRequest.getHistory() == 0) {
            total_premium *= 1.2;
        } else if (newRequest.getHistory() < 2) {
            total_premium *= 1.1;
        } else {
            total_premium *= 1;
        }

        response.setPremium(total_premium);
        response.setSuccess("success");
        Random random = new Random();
        long randomNumber = random.nextLong() % 10000000000L;
        if (randomNumber < 0) {
            randomNumber *= -1;
        }
        response.setQuote_reference(String.format("Q%010d", randomNumber));
        return response;
    }
}
