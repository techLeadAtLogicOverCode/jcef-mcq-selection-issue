package jcef.user.selection.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.jcef.JBCefJSQuery;
import com.intellij.ui.jcef.JCEFHtmlPanel;
import jcef.user.selection.service.HtmlToolWindowComponentsService;
import org.cef.browser.CefBrowser;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;


public class ShowUserSelection extends AnAction implements LogCommons{



    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        HtmlToolWindowComponentsService htmlToolWindowComponentsService = e.getProject().getService(HtmlToolWindowComponentsService.class);

        JCEFHtmlPanel browser = htmlToolWindowComponentsService.browser();

        JBCefJSQuery javaScriptEngineProxy = htmlToolWindowComponentsService.query();


        javaScriptEngineProxy.addHandler(new Function<String, JBCefJSQuery.Response>() {
            @Override
            public JBCefJSQuery.Response apply(String s) {
                System.out.println(logMsgPrefix() + "string is >" + s + "<");
                return null;
            }
        });









        System.out.println(logMsgPrefix() + "executing java script from action thread");

        CefBrowser cefBrowser = browser.getCefBrowser();
        String injectedJavaScript = "var answers = findAnswers()" + "\n" +
                javaScriptEngineProxy.inject("answers");

        //this will trigger line 28 in a different thread (AWT-AppKit)
        //It's working as expected, but same logic is not working in a similar example in ExtractUserSelection.java
        //where thread that is injecting javascript wait for the response from
        //JBCefJSQuery handler using SynchronousQueue.
        cefBrowser.executeJavaScript(injectedJavaScript, browser.getCefBrowser().getURL(), 0);
    }
}
