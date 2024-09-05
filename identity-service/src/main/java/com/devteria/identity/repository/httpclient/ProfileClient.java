package com.devteria.identity.repository.httpclient;

import com.devteria.identity.configuration.AuthenticationRequestInterceptor;
import com.devteria.identity.dto.request.ProfileCreationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${app.service.profile}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    Object createProfile(@RequestBody ProfileCreationRequest request);

}
