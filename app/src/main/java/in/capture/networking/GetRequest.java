package in.capture.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.SocketException;

import in.capture.utils.Parser;
import in.capture.utils.Utils;

/**
 * handles get request
 */
public class GetRequest {
    static final int CLIENT_PROTOCOL_ERROR = 90;
    static final int SOCKET_ERROR = 80;
    static final int IO_ERROR = 70;
    static final int FILE_NOT_FOUND = 60;

    Boolean success = true;
    PersistentCookieStore cookieStore;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    /**
     * constructor to get url and callback object and context
     * <p/>
     * hits a get request to url and calls callback functions
     *
     * @param url      Where request is to be made
     * @param callback object of callback
     * @param context
     */
    public GetRequest(final Callbacks callback, final String url,
                      final Context context) {

        sp = context.getSharedPreferences("Users",
                Context.MODE_PRIVATE);
        editor = sp.edit();
        class getrequest extends AsyncTask<String, Void, Integer> {

            @Override
            protected Integer doInBackground(String... arg0) {
                int statusCode = 0;
                try {
                    // HttpClient httpclient = new DefaultHttpClient();

                    HttpGet request = Parser.create_get_request(url);
                    PersistentCookieStore cookieStore1 = new PersistentCookieStore(context);
                    try {
                        if(sp.getString("sessionid", "").length() !=0)
                            request.addHeader("Cookie",
                                    sp.getString("sessionid", "").substring(0, sp.getString("sessionid", "").indexOf("; ") +1 )  + cookieStore1.getCookies().get(0).getName() + "=" + cookieStore1.getCookies().get(0).getValue() + ";");
                        else
                            request.addHeader("Cookie", cookieStore1.getCookies().get(0).getName() + "=" + cookieStore1.getCookies().get(0).getValue() + ";");
                    } catch (Exception e) {

                    }

                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters,
                            10000);
                    HttpConnectionParams.setSoTimeout(httpParameters, 15000);
                    HttpClient httpclient = new DefaultHttpClient(
                            httpParameters);
                    request.addHeader("X-Requested-From", "BizManager");
                    HttpResponse response = httpclient.execute(request);
                    statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        cookieStore = Parser.store_cookies(httpclient, context);
                        success = true;
                    } else {
                        success = false;
                    }
                    callback.processResponse(response, url);

                } catch (ClientProtocolException e) {
                    //error code 90
                    success = false;
                    statusCode = CLIENT_PROTOCOL_ERROR;
                                e.printStackTrace();
                } catch (ConnectTimeoutException e) {
                    success = false;
                                e.printStackTrace();
                } catch (SocketException e) {
                    success = false;
                    statusCode = SOCKET_ERROR;
                                e.printStackTrace();
                } catch (IOException e) {
                    // error code 70
                    success = false;
                    statusCode = IO_ERROR;
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

            @Override
            protected void onPostExecute(Integer statusCode) {
                try {
                    if (statusCode == 401) {
                       /* Utils.showToast("Your Session is expired, attempting to create new session", context);
                        Intent to_reauthorize = new Intent(context, Login.class);

                        editor.putBoolean("has_session", false);
                        editor.commit();
                        context.getSharedPreferences("Users_shop", Context.MODE_PRIVATE).edit().clear().commit();
*/
//
//                        try {
//                            GoogleCloudMessaging.getInstance(context).unregister();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                       /* to_reauthorize.putExtra("reauthorize", true);
                        to_reauthorize.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        to_reauthorize.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                            to_reauthorize.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(to_reauthorize);*/
                    } else if(statusCode == 402){
                        /*Utils.showToast("Your account has been temporarily suspended, please make your payment", context);
                        Intent to_reauthorize = new Intent(context, PaymentRequired.class);
                        to_reauthorize.putExtra("reauthorize", true);
                        to_reauthorize.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        to_reauthorize.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                            to_reauthorize.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(to_reauthorize);*/
                    } else if (success)
                        callback.postexecute(url, statusCode);
                    else {
                        callback.postexecute("failed", statusCode);
                    }
                } catch (Exception e) {
                    Log.d("error", "cancel request");
                }

            }
        }
        if (Utils.isConnectingToInternet(context)) {
            new getrequest().execute();
        }
    }

}
