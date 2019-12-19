package yasis.apps.sasi;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

public class alertDialog {
    private static AlertDialog.Builder builder;
    public static void show(Context ctx, String title,String message, String negativeBtn){
        builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setIcon(R.drawable.error_png);
        builder.setNegativeButton(negativeBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
