package in.capture.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;


import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.FileNotFoundException;
import java.net.SocketException;
import java.util.List;

import in.capture.utils.Interfaces;
import in.capture.utils.Utils;

/**
 * handles post request
 */
public class DeleteRequest {
    Boolean success = true;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    /**
     * constructor to get url and callback object and context
     * <p/>
     * hits a get request to url and calls callback functions
     *
     * @param url      Where request is to be made
     * @param callback object of callback
     */
    public DeleteRequest(final Interfaces.PutCallbacks callback, final String url, final Context context) {

        sp = context.getSharedPreferences("Users",
                Context.MODE_PRIVATE);
        editor = sp.edit();
        class deleterequest extends AsyncTask<String, String, Integer> {

            @Override
            protected Integer doInBackground(String... arg0) {
                int statusCode = 0;
                try {
                    HttpParams httpParameters = new BasicHttpParams();
                    BasicCookieStore cookieStore2 = new BasicCookieStore();
                    PersistentCookieStore cookieStore = new PersistentCookieStore(context);
                    //cookieStore2.addCookie(cookieStore.getCookies().get(0));
                    // cookieStore2.addCookie(loginCookies.get(1));
                    HttpContext localContext = new BasicHttpContext();
                    localContext.setAttribute(ClientContext.COOKIE_STORE,
                            cookieStore2);
                    SharedPreferences sp;
                    sp = context.getSharedPreferences("Users", Context.MODE_PRIVATE);
                    String username = sp.getString("userName", "");
                    // Setup timeouts
                    HttpConnectionParams.setConnectionTimeout(httpParameters,
                            10000);
                    if (!username.equalsIgnoreCase("") &&
                            (url.contains("mobile/" + username + "/product/") || url.contains("mobile/" + username + "/category/"))) {
                        HttpConnectionParams.setSoTimeout(httpParameters, 45000);
                    } else {
                        HttpConnectionParams.setSoTimeout(httpParameters, 15000);
                    }

                    HttpClient httpclients = new DefaultHttpClient(
                            httpParameters);

                    HttpDelete httpDelete = new HttpDelete(url);
                    httpDelete.setHeader("Referer", url);
                    httpDelete.setHeader("X-Requested-From", "BizManager");
                    httpDelete.setHeader("X-CSRFToken", cookieStore.getCookies()
                            .get(0).getValue());

                    try {
                        if(sp.getString("sessionid", "").length() !=0)
                            httpDelete.addHeader("Cookie",
                                    sp.getString("sessionid", "").substring(0, sp.getString("sessionid", "").indexOf("; ") +1 )  + cookieStore.getCookies().get(0).getName() + "=" + cookieStore.getCookies().get(0).getValue() + ";");
                        else
                            httpDelete.addHeader("Cookie", cookieStore.getCookies().get(0).getName() + "=" + cookieStore.getCookies().get(0).getValue() + ";");
                    } catch (Exception e) {

                    }


                    HttpResponse response = httpclients.execute(httpDelete,
                            localContext);
                    //Added
                    List<Cookie> cookies = cookieStore2.getCookies();
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("csrftoken")) {
                            cookieStore.clear();
                            cookieStore.addCookie(cookie);
                        }
                    }
                    statusCode = response.getStatusLine().getStatusCode();
                    try {
                        if (url.contains("login") || url.contains("registration")) {
                            Header[] head = response.getHeaders("Set-Cookie");
                            if (head != null) {
                                for (int i = 0; i < head.length; i++) {
                                    if (head[i].getValue().contains("sessionid")) {
                                        editor.putString("sessionid",
                                                head[i].getValue().substring(0,head[i].getValue().indexOf("; ") +1 ) );
                                        editor.commit();
                                        break;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                    }


                    callback.processResponse(response, url);

                } catch (ClientProtocolException e) {
                    statusCode = GetRequest.CLIENT_PROTOCOL_ERROR;
                    success = false;
                                e.printStackTrace();
                } catch (SocketException e) {
                    statusCode = GetRequest.SOCKET_ERROR;
                    success = false;
                                e.printStackTrace();
                } catch (ConnectTimeoutException e) {
                    success = false;
                                e.printStackTrace();
                    statusCode = GetRequest.IO_ERROR;
                    success = false;
                                e.printStackTrace();
                } catch (FileNotFoundException e) {
                    statusCode = GetRequest.FILE_NOT_FOUND;
                    success = false;
                                e.printStackTrace();
                } catch (Exception e) {
                    success = false;
                                e.printStackTrace();
                } finally {
                    return statusCode;
                }
            }

            @Override
            protected void onPreExecute() {
                callback.preexecute(url);
                super.onPreExecute();
            }
/*
            @Override
            protected void onProgressUpdate(String... values) {
               // super.onProgressUpdate(values);
                Log.i("makemachine", "onProgressUpdate(): " + String.valueOf(values[0]));
                callback.preexecute(url, (Integer.parseInt(values[0]))*2);
            }
            */

            @Override
            protected void onPostExecute(Integer statusCode) {
                if (statusCode == 401) {
                    Utils.showToast("Your Session is expired, attempting to create new session", context);


                    editor.putBoolean("has_session", false);
                    editor.commit();

                    context.getSharedPreferences("Users_shop", Context.MODE_PRIVATE).edit().clear().commit();
//                    try {
//                        GoogleCloudMessaging.getInstance(context).unregister();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }



                }  else if(statusCode == 402){
                    Utils.showToast("Your account has been temporarily suspended, please make your payment", context);

                } else if (success)
                    callback.postexecute(url, statusCode);
                else {
                    callback.postexecute("failed", statusCode);
                }
            }
        }
        if (Utils.isConnectingToInternet(context))
            new deleterequest().execute();
    }

}
