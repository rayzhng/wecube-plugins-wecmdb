package com.webank.plugins.wecmdb.service.helper;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.webank.plugins.wecmdb.exception.PluginException;

public class HttpServletResponseWriter {

    private HttpServletResponse response;

    public HttpServletResponseWriter(HttpServletResponse response) {
        this.response = response;
    }

    public void writeHttpResponse(ResponseEntity<byte[]> responseEntity) {
        response.setStatus(responseEntity.getStatusCodeValue());
        writeHeaders(responseEntity.getHeaders());
        writeBody(responseEntity.getBody());
    }

    private void writeHeaders(HttpHeaders headers) {
        if (headers == null || headers.isEmpty())
            return;
        for (Map.Entry<String, List<String>> headerEntry : headers.entrySet()) {
            String headerName = headerEntry.getKey();
            List<String> headerValues = headerEntry.getValue();
            if (headerValues != null && !headerValues.isEmpty()) {
                headerValues.forEach(headerValue -> response.addHeader(headerName, headerValue));
            }
        }
    }

    private void writeBody(byte[] body) {
        if (body == null)
            return;

        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(body);
            outputStream.flush();
        } catch (Exception e) {
            throw new PluginException(String.format("Failed to write http servlet response data due to %s ", e.getMessage()));
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
            }
        }
    }

}