package in.com.demo.myrecycleviewerapplication.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import in.com.demo.myrecycleviewerapplication.R;

public class DialogUtils {

    public static  void showWebview(String URL, Context context){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.view_item_details);
        WebView webView = dialog.findViewById(R.id.item_browser_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(URL);
        dialog.setCancelable(true);
        dialog.show();
    }

}
