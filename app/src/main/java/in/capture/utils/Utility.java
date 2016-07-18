package in.capture.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Shubham.Goel on 18-05-2016.
 */
public class Utility {
    public static void showToast(Context context,String message )
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String capitalizeFirstLetter(String original) {
        if (original.length() == 0)
            return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}
