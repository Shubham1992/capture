package in.capture.utils;

import android.content.Context;
import android.widget.Toast;

import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

/**
 * Created by Shubham.Goel on 18-05-2016.
 */
public class Utility {


    public static String capitalizeFirstLetter(String original) {
        if (original.length() == 0)
            return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public  static  void showToast(Context context, String message)
    {
        showToast(message, context);
    }
    public static void showToast(String message, Context context) {
        // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        SnackbarManager.show(
                com.nispok.snackbar.Snackbar.with(context)
                        .type(SnackbarType.MULTI_LINE)
                        .text(message)
        );


    }

    public static void showToastPersistent(final Context context, String st, String actionLabel, boolean type) { //"Toast toast" is declared in the class

       /* ActionClickListener actionClickListenerSync;

        if(type) {
            actionClickListenerSync = new ActionClickListener() {
                @Override
                public void onActionClicked(com.nispok.snackbar.Snackbar snackbar) {
                    final DatabaseHandler db = new DatabaseHandler(context);
                    sp = context.getSharedPreferences(Constants.User_sp, Context.MODE_PRIVATE);
                    String username = sp.getString("userName", "test");
                    String uri_send_cat = Constants.base_uri + "mobile/" + username + "/category/";
                    if (db.errorsynccategory())
                        new PostRequest((Callbacks) context, uri_send_cat, context);

                }
            };
        }else {
            actionClickListenerSync = new ActionClickListener() {
                @Override
                public void onActionClicked(com.nispok.snackbar.Snackbar snackbar) {
                    final DatabaseHandler db = new DatabaseHandler(context);
                    sp = context.getSharedPreferences(Constants.User_sp, Context.MODE_PRIVATE);
                    String username = sp.getString("userName", "test");
                    String uri_send_cat = Constants.base_uri + "mobile/" + username + "/product/";
                    if (db.errorsync())
                        new PostRequest((Callbacks) context, uri_send_cat, context);

                }
            };
        }*/



        SnackbarManager.show(
                com.nispok.snackbar.Snackbar.with(context)
                        .text(st)
                        //.actionLabel(actionLabel)
                        //.actionListener(actionClickListenerSync)
                        .duration(com.nispok.snackbar.Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
        );
        //finally display it
    }
}
