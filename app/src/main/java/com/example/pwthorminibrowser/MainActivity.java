package com.example.pwthorminibrowser;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceError;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

public class MainActivity extends Activity {
    private WebView webView;
    private EditText urlInput;
    private TextView status;
    private ProgressBar progress;
    private static final String HOME = "https://pwthor.live/study/batches";
    private static final String TAG = "MiniBrowser";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi();
        setupWebView();
        loadUrl(HOME);
    }

    private void buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(10, 10, 10, 0);
        root.setBackgroundColor(Color.WHITE);

        TextView heading = new TextView(this);
        heading.setText("PW Thor Mini Browser");
        heading.setTextSize(20);
        heading.setTextColor(Color.BLACK);
        heading.setPadding(4, 4, 4, 8);
        root.addView(heading);

        LinearLayout addressRow = new LinearLayout(this);
        addressRow.setOrientation(LinearLayout.HORIZONTAL);
        urlInput = new EditText(this);
        urlInput.setSingleLine(true);
        urlInput.setHint("https://example.com");
        addressRow.addView(urlInput, new LinearLayout.LayoutParams(0, dp(48), 1));

        Button go = new Button(this);
        go.setText("Go");
        go.setOnClickListener(v -> loadUrl(urlInput.getText().toString().trim()));
        addressRow.addView(go);
        root.addView(addressRow);

        LinearLayout controls = new LinearLayout(this);
        controls.setGravity(Gravity.CENTER_VERTICAL);
        addButton(controls, "Back", v -> { if (webView.canGoBack()) webView.goBack(); });
        addButton(controls, "Reload", v -> webView.reload());
        addButton(controls, "Home", v -> loadUrl(HOME));
        root.addView(controls);

        progress = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progress.setMax(100);
        root.addView(progress, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(3)));

        status = new TextView(this);
        status.setText("Ready");
        status.setTextSize(12);
        status.setTextColor(Color.DKGRAY);
        status.setPadding(4, 5, 4, 5);
        root.addView(status);

        webView = new WebView(this);
        root.addView(webView, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        setContentView(root);
    }

    private void addButton(LinearLayout row, String label, View.OnClickListener click) {
        Button b = new Button(this);
        b.setText(label);
        b.setOnClickListener(click);
        row.addView(b, new LinearLayout.LayoutParams(0, dp(46), 1));
    }

    private int dp(int value) {
        return (int)(value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private void setupWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setAllowContentAccess(false);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override public void onProgressChanged(WebView view, int newProgress) {
                progress.setProgress(newProgress);
                progress.setVisibility(newProgress >= 100 ? View.GONE : View.VISIBLE);
            }
            @Override public void onReceivedTitle(WebView view, String title) {
                status.setText("Page: " + title);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                String scheme = uri.getScheme();
                if (!"https".equalsIgnoreCase(scheme) && !"http".equalsIgnoreCase(scheme)) {
                    status.setText("Blocked unsupported URL scheme");
                    return true;
                }
                Log.i(TAG, "Navigation: " + uri);
                status.setText("Opening: " + uri.getHost());
                urlInput.setText(uri.toString());
                return false;
            }
            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                status.setText("Loading: " + Uri.parse(url).getHost());
                urlInput.setText(url);
                Log.i(TAG, "Page started: " + url);
            }
            @Override public void onPageFinished(WebView view, String url) {
                status.setText("Loaded: " + Uri.parse(url).getHost());
                urlInput.setText(url);
                Log.i(TAG, "Page finished: " + url);
            }
            @Override public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    status.setText("Load error: " + error.getDescription());
                }
            }
        });
    }

    private void loadUrl(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            status.setText("Enter a URL first");
            return;
        }
        String url = raw.trim();
        if (!url.matches("(?i)^https?://.*")) url = "https://" + url;
        Uri uri = Uri.parse(url);
        if (uri.getHost() == null) {
            status.setText("Invalid URL");
            return;
        }
        urlInput.setText(url);
        webView.loadUrl(url);
    }

    @Override public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    @Override protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
        }
        super.onDestroy();
    }
}
