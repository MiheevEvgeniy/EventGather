package ru.practicum;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class StatClient {
    protected final RestTemplate rest;

    public StatClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected ResponseEntity<Object> get(String path) {
        return get(path, null);
    }

    protected ResponseEntity<Object> get(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, parameters, body);
    }

    protected <T> ResponseEntity<Object> put(String path, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PUT, path, parameters, body);
    }

    protected <T> ResponseEntity<Object> patch(String path) {
        return patch(path, null, null);
    }

    protected <T> ResponseEntity<Object> patch(String path, T body) {
        return patch(path, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, Map<String, Object> parameters) {
        return patch(path, parameters, null);
    }

    protected <T> ResponseEntity<Object> patch(String path,@Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PATCH, path, parameters, body);
    }

    protected ResponseEntity<Object> delete(String path) {
        return delete(path,null);
    }

    protected ResponseEntity<Object> delete(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.DELETE, path, parameters, null);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);
        System.out.println(path);
        ResponseEntity<Object> serverResponse;
        try {
            if (parameters != null) {
                serverResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                serverResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareResponse(serverResponse);
    }

    private static ResponseEntity<Object> prepareResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
