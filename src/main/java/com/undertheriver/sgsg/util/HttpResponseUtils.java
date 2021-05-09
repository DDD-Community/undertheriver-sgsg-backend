package com.undertheriver.sgsg.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;

public class HttpResponseUtils {

    public static void writeFromJson(HttpServletResponse response, Object responseDto) throws IOException {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType jsonMimeType = MediaType.APPLICATION_JSON;

        if (jsonConverter.canWrite(responseDto.getClass(), jsonMimeType)) {
            jsonConverter.write(responseDto, jsonMimeType, new ServletServerHttpResponse(response));
        }
    }
}
