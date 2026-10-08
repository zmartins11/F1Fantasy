package com.example.f1.scoring_service.adapter;

import com.example.f1.scoring_service.contracts.user.UserData;
import com.example.f1.scoring_service.contracts.user.UserReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Component
public class UserRestAdapter implements UserReader {

    private final RestClient restClient;

    public UserRestAdapter(
            @Value("${services.auth.url}") String authServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(authServiceUrl)
                .build();
    }

    @Override
    public List<UserData> findAll() {
        UserData[] users = restClient.get()
                .uri("/internal/users")
                .retrieve()
                .body(UserData[].class);

        return users == null ? List.of() : List.of(users);
    }
}
