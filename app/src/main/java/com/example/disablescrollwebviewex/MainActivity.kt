package com.example.disablescrollwebviewex

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import com.example.disablescrollwebviewex.R

/**
 *
 */
class MainActivity : AppCompatActivity() {
    //--------------------------------------------------- 定数とか
    /**
     *
     */
    companion object {
        private const val URL_PRIVACY_POLICY = "https://cmn.point.recruit.co.jp/policy/privacy_sp.html"
        private const val MP = LinearLayout.LayoutParams.MATCH_PARENT
        private const val WP = LinearLayout.LayoutParams.WRAP_CONTENT
        private const val TEXT_READ_START = "読み込み開始"
        private const val TEXT_READ_FINISH = "読み込み完了"
        private const val TEXT_NETWORK_ERROR = "通信エラー"
    }

    //--------------------------------------------------- 変数とか
    lateinit var mScrollView: ScrollView
    lateinit var mWebView: WebView

    //--------------------------------------------------- ライフサイクル
    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    /**
     *
     */
    override fun onResume() {
        super.onResume()
        mWebView.loadUrl(URL_PRIVACY_POLICY)
    }

    //--------------------------------------------------- 関数とか
    /**
     *
     */
    private fun initViews() {
        mScrollView = findViewById<ScrollView>(R.id.scrollview)
        mWebView = findViewById<WebView>(R.id.webview);

        mWebView.webViewClient = createWebViewClient(this)
        mWebView.settings.javaScriptEnabled = true
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return event?.action == MotionEvent.ACTION_MOVE
            }
        })
    }

    /**
     *
     */
    fun createWebViewClient(ctx: Context): WebViewClient {
        return object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                // WebViewで表示すべきurlと異なるurlが入ってきた場合は、
                // 中のリンクをクリックしたとみなし外部ブラウザで表示する
                url?.let {
                    if (it.toString() != URL_PRIVACY_POLICY) {
                        view?.stopLoading()
                        moveToUrl(it)
                        return true
                    }
                }
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                showToast(ctx, TEXT_READ_START)
                updateLayoutParams(view, MP, MP)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                showToast(ctx, TEXT_READ_FINISH)
                updateLayoutParams(view, MP, WP)
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                showToast(ctx, TEXT_NETWORK_ERROR)
            }
        }
    }

    /**
     *
     */
    private fun moveToUrl(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    /**
     *
     */
    fun updateLayoutParams(view: WebView?, width: Int, height: Int) {
        view?.let { v ->
            v.layoutParams = LinearLayout.LayoutParams(width, height)
        }
    }

    /**
     *
     */
    fun showToast(ctx: Context, text: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(ctx, text, duration).show()
    }
}
