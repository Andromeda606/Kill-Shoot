package com.androsoft.killshot.dialog;

import android.content.Context;
import android.webkit.WebView;

import java.util.HashMap;
import java.util.Map;

public class InfoDialog extends CustomDialog {
    public InfoDialog(Context context) {
        super(context);
    }

    public InfoDialog setHyperlinks(HashMap<String, String> hyperlinks) {
        WebView webView = new WebView(this.alertDialogBuilder.getContext());
        StringBuilder hyperLinkList = new StringBuilder();
        hyperLinkList.append("data:text/html,Aşağıdaki Linkler sayesinde ben ikonları elde ettim.<br>");
        for (Map.Entry<String, String> hyperLink : hyperlinks.entrySet()) {
            hyperLinkList.append(String.format("<a href=\"%s\">%s</a><br>", hyperLink.getValue(), hyperLink.getKey()));
        }
        webView.loadUrl(hyperLinkList.toString());
        this.alertDialogBuilder.setView(webView);
        return this;
    }
}
