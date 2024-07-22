package com.winner_cat.domain.oauth2.dto;

import java.util.Map;


/**
 * Google Response Attribute Dto
 * {
 * 		resultcode=00, message=success, id=123123123, name= 최강
 * }
 */
public class GoogleResponse implements OAuth2Response{
    private final Map<String, Object> attribute;

    public GoogleResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
