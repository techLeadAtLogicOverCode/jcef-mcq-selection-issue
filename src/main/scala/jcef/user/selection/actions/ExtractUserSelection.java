package jcef.user.selection.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.jcef.JBCefJSQuery;
import com.intellij.ui.jcef.JCEFHtmlPanel;
import jcef.user.selection.service.HtmlToolWindowComponentsService;
import org.cef.browser.CefBrowser;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.SynchronousQueue;
import java.util.function.Function;

public class ExtractUserSelection extends AnAction implements LogCommons {

    private final SynchronousQueue<String> stringSynchronousQueue = new SynchronousQueue<>(true);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        HtmlToolWindowComponentsService htmlToolWindowComponentsService = e.getProject().getService(HtmlToolWindowComponentsService.class);

        JCEFHtmlPanel browser = htmlToolWindowComponentsService.browser();

        JBCefJSQuery javaScriptEngineProxy = htmlToolWindowComponentsService.query();


        javaScriptEngineProxy.addHandler(new Function<String, JBCefJSQuery.Response>() {
            @Override
            public JBCefJSQuery.Response apply(String s) {
                System.out.println(logMsgPrefix() + "adding >" + s + "< to synchronous queue");

                try {
                    stringSynchronousQueue.put(s);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.out.println(logMsgPrefix() + "added >" + s + "< to synchronous queue");

                return null;
            }
        });

        System.out.println(logMsgPrefix() + " executing java script from action thread");

        CefBrowser cefBrowser = browser.getCefBrowser();
        String injectedJavaScript = "var answers = findAnswers()" + "\n" +
                javaScriptEngineProxy.inject("answers");

        //PROBLEM STATEMENT :-
        //this is supposed to trigger line 28 in a different thread (AWT-AppKit)
        //but, it's not getting triggered.
        //this is working file in a similar example in ShowUserSelection.java
        cefBrowser.executeJavaScript(injectedJavaScript, browser.getCefBrowser().getURL(), 0);

        System.out.println(logMsgPrefix() + "now wait for element in queue");
        try {
            String str = stringSynchronousQueue.take();
            System.out.println(logMsgPrefix() + "element fetched from queue is >" + str + "<");
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
