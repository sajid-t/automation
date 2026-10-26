
package com.example.webautomation;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    private WebView webView;

    private static final String HOME_URL =
        "https://pwthor.live/study/batches";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);

        Button back = new Button(this);
        back.setText("Back");

        Button reload = new Button(this);
        reload.setText("Reload");

        Button home = new Button(this);
        home.setText("Home");

        Button run = new Button(this);
        run.setText("Run Script");

        toolbar.addView(back,
            new LinearLayout.LayoutParams(0, -2, 1));
        toolbar.addView(reload,
            new LinearLayout.LayoutParams(0, -2, 1));
        toolbar.addView(home,
            new LinearLayout.LayoutParams(0, -2, 1));
        toolbar.addView(run,
            new LinearLayout.LayoutParams(0, -2, 2));

        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        root.addView(toolbar);
        root.addView(webView,
            new LinearLayout.LayoutParams(-1, 0, 1));

        setContentView(root);

        webView.loadUrl(HOME_URL);

        back.setOnClickListener(v -> {
            if (webView.canGoBack()) webView.goBack();
        });

        reload.setOnClickListener(v -> webView.reload());

        home.setOnClickListener(v ->
            webView.loadUrl(HOME_URL));

        run.setOnClickListener(v -> runScript());
    }

    private void runScript() {

        String script =
            "(function() {"
          + "const result = {"
          + "url: location.href,"
          + "title: document.title,"
          + "links: document.querySelectorAll('a').length"
          + "};"
          + "return JSON.stringify(result);"
          + "})();";

        webView.evaluateJavascript(script, result -> {
            Toast.makeText(
                this,
                "Script executed: " + result,
                Toast.LENGTH_LONG
            ).show();
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
            }
