package com.ibopar.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Fabio Moura on 18/10/2015.
 */
public class ReadStream {

    public static String realizaLeituraDoStream(InputStream inputStream){

        String aux = "";
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            while((aux = bufferedReader.readLine()) != null ) {
                stringBuilder.append(aux);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

}
