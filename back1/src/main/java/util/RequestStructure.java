package util;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Data
public class RequestStructure {
    private Map<String, String[]> params;
    private Integer id;
    private int responseCode;
    private String message;
    private String requestBody;

    private static void processId(RequestStructure rs, String path) {
        if (Objects.isNull(path) || path.length() == 1) {
            rs.setId(null);
            rs.setMessage(null);
            return;
        }
        try {
            if (path.endsWith("/")) {
                rs.setMessage(path.substring(1, path.length() - 1));
                rs.setId(Integer.parseInt(path.substring(1, path.length() - 1)));
            } else {
                rs.setMessage(path.substring(1));
                rs.setId(Integer.parseInt(path.substring(1)));
            }
        } catch (NumberFormatException e) {
            rs.setId(null);
        }
    }

    public static RequestStructure fromRequest(HttpServletRequest request) throws IOException {
        RequestStructure requestStructure = new RequestStructure();
        processId(requestStructure, request.getPathInfo());
        requestStructure.setParams(request.getParameterMap());

        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
            buffer.append(System.lineSeparator());
        }
        requestStructure.setRequestBody(buffer.toString());

        return requestStructure;
    }
}
