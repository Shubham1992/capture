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
}
