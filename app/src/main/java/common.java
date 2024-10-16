  // Replace with your actual package name

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.RequiresApi;

public class common {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setStatusBarAndNotificationColor(Window window, int color, String itemColor, boolean hideStatusBar) {
        // Set the status bar background color if not hiding it
        if (!hideStatusBar) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }

        // Set notification item (icon and text) color or hide the status bar
        View decor = window.getDecorView();
        if (hideStatusBar) {
            // Hide the status bar
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            if (itemColor.equalsIgnoreCase("black")) {
                // Set icons and text to black
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                // Set icons and text to white (clear the flag)
                decor.setSystemUiVisibility(0);
            }
        }
    }
}
