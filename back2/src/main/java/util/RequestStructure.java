package util;

import lombok.Data;

import java.util.Map;

@Data
public class RequestStructure {
    private Map<String, String[]> params;
    private Integer id;
    private int responseCode;
    private String message;
    private String requestBody;
}
