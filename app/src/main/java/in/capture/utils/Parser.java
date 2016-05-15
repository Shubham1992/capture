package in.capture.utils;

import android.content.Context;
import android.util.Log;



import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import in.capture.networking.PersistentCookieStore;

/**
 * Created by Shubham.Goel on 11-05-2016.
 */
public class Parser {

    public static HttpGet create_get_request(String url) {

        HttpGet request = new HttpGet(URI.create(url.replace(" ", "%20")));
        return request;
    }

    /**
     * extracts cookies from httpclient returns cookies extracted
     *
     * @param httpclient containing cookies
     * @param context
     */
    public static PersistentCookieStore store_cookies(HttpClient httpclient,
                                                      Context context) {
        List<Cookie> cookies = ((AbstractHttpClient) httpclient)
                .getCookieStore().getCookies();
        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
        for (Cookie c : cookies) {
            if (c.getName().equalsIgnoreCase("csrftoken")) {
                cookieStore.clear();
                cookieStore.addCookie(c);
            }
        }
        return cookieStore;
    }



}
