package pro.games_box.alphanews.api;

import com.google.gson.annotations.Expose;

/**
 * Created by Tesla on 10.05.2017.
 */

public class ApiError {
    @Expose
    private String message;

    @Expose
    private String description;

    public ApiError() {
    }

    public String getMessage() {
        return message;
    }

    public String getDescription(){
        return description;
    }
}
