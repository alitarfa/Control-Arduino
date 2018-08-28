package com.controller.tr.controllearduino.youtube;

import org.json.JSONObject;

import java.net.URL;
import org.apache.commons.io.IOUtils;

/**
 * Created by tarfa on 8/4/17.
 */

public class TitleManager {
    public static String getTitle(String youtubeUrl) {
        try {
            if (youtubeUrl != null) {
                URL embededURL = new URL("http://www.youtube.com/oembed?url=" +
                        youtubeUrl + "&format=json"
                );

                return new JSONObject(IOUtils.toString(embededURL)).getString("title");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
