package com.omerasaf.harftepsisi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
    private WebView mWebView;
    private TextToSpeech mTTS;
    private boolean mTTSReady = false;

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tam Ekran
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Native TTS Başlatma
        try {
            mTTS = new TextToSpeech(this, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mWebView = new WebView(this);
        setContentView(mWebView);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        // Native JavaScript Bridge
        mWebView.addJavascriptInterface(new WebAppInterface(this), "AndroidBridge");

        // Dış bağlantılar ve indirme dinleyicisi
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Sadece harici sitelere yönlendirme olduğunda tarayıcıyı aç
                if ((url.startsWith("http://") || url.startsWith("https://")) && !url.contains("raw.githubusercontent.com")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        mWebView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS && mTTS != null) {
            try {
                Locale trLocale = new Locale("tr", "TR");
                int result = mTTS.setLanguage(trLocale);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    mTTS.setLanguage(Locale.getDefault());
                }
                mTTS.setSpeechRate(0.82f);
                mTTSReady = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class WebAppInterface {
        private final Activity activity;

        public WebAppInterface(Activity activity) {
            this.activity = activity;
        }

        @JavascriptInterface
        public void speakText(final String text) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mTTSReady && mTTS != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, "harf_tepsisi_utterance");
                            } else {
                                mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @JavascriptInterface
        public void downloadAndInstallApk(final String downloadUrl) {
            startApkDownload(downloadUrl);
        }

        @JavascriptInterface
        public void vibrateDevice(long durationMs) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                        if (v != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                v.vibrate(durationMs);
                            }
                        }
                    } catch (Exception ignored) {}
                }
            });
        }
    }

    private void startApkDownload(final String downloadUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWebView.evaluateJavascript("window.onUpdateDownloadProgress && window.onUpdateDownloadProgress(0, 'İndirme başlatılıyor...');", null);
                        }
                    });

                    URL url = new URL(downloadUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setInstanceFollowRedirects(true);
                    connection.setConnectTimeout(15000);
                    connection.setReadTimeout(30000);
                    connection.connect();

                    // Yönlendirmeleri takip et (GitHub release asset redirect desteği)
                    int redirectCount = 0;
                    while ((connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM ||
                            connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP ||
                            connection.getResponseCode() == HttpURLConnection.HTTP_SEE_OTHER) && redirectCount < 5) {
                        String newUrl = connection.getHeaderField("Location");
                        connection.disconnect();
                        connection = (HttpURLConnection) new URL(newUrl).openConnection();
                        connection.setConnectTimeout(15000);
                        connection.setReadTimeout(30000);
                        connection.connect();
                        redirectCount++;
                    }

                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        final int code = connection.getResponseCode();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.evaluateJavascript("window.onUpdateDownloadError && window.onUpdateDownloadError('İndirme hatası: HTTP " + code + "');", null);
                            }
                        });
                        return;
                    }

                    int fileLength = connection.getContentLength();
                    File apkFile = new File(getCacheDir(), "HarfTepsisi-update.apk");
                    if (apkFile.exists()) {
                        apkFile.delete();
                    }

                    InputStream input = connection.getInputStream();
                    FileOutputStream output = new FileOutputStream(apkFile);

                    byte[] data = new byte[8192];
                    long total = 0;
                    int count;
                    int lastPercent = -1;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        output.write(data, 0, count);

                        if (fileLength > 0) {
                            final int percent = (int) ((total * 100) / fileLength);
                            if (percent != lastPercent) {
                                lastPercent = percent;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mWebView.evaluateJavascript("window.onUpdateDownloadProgress && window.onUpdateDownloadProgress(" + percent + ", '%" + percent + "');", null);
                                    }
                                });
                            }
                        }
                    }

                    output.flush();
                    output.close();
                    input.close();
                    connection.disconnect();

                    final File finalApk = apkFile;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWebView.evaluateJavascript("window.onUpdateDownloadProgress && window.onUpdateDownloadProgress(100, 'Kuruluma hazır!');", null);
                            promptInstallApk(finalApk);
                        }
                    });

                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String err = e.getMessage() != null ? e.getMessage().replace("'", "\\'") : "Bilinmeyen hata";
                            mWebView.evaluateJavascript("window.onUpdateDownloadError && window.onUpdateDownloadError('" + err + "');", null);
                        }
                    });
                }
            }
        }).start();
    }

    private void promptInstallApk(File apkFile) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!getPackageManager().canRequestPackageInstalls()) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
            }

            Uri apkUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    apkFile
            );

            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(installIntent);
        } catch (final Exception e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String err = e.getMessage() != null ? e.getMessage().replace("'", "\\'") : "Kurulum başlatılamadı";
                    mWebView.evaluateJavascript("window.onUpdateDownloadError && window.onUpdateDownloadError('" + err + "');", null);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            try {
                mTTS.stop();
                mTTS.shutdown();
            } catch (Exception ignored) {}
        }
        super.onDestroy();
    }
}
