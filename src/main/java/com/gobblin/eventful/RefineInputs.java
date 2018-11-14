package com.gobblin.eventful;

import java.util.Map;

/**
 * Created by exa0051 on 31/10/18.
 */
public class RefineInputs {

    private static String refinedInput = "";

    /*
    returns the input string based on the Map key value pair
    @params : Map<String, String>
    @return : String
     */
    public String refineSearchInputs(Map<String, String> inputs) {

        for (Map.Entry<String, String> entry: inputs.entrySet()) {
            refinedInput = refinedInput + entry.getKey() + "=" + entry.getValue().replace(" ", "+") + "&";
        }

        /*
        finds the last index position of '&'
         */
        int ampercendPosition = refinedInput.lastIndexOf("&");

        return refinedInput.substring(0, ampercendPosition);

    }

}
