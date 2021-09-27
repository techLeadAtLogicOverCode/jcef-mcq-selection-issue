package jcef.user.selection.service

import com.intellij.ui.jcef.{JBCefApp, JBCefBrowserBase, JBCefClient, JBCefJSQuery, JCEFHtmlPanel}

final class HtmlToolWindowComponentsService{
  lazy val browser: JCEFHtmlPanel = {
    println("creating browser")
    val client: JBCefClient = JBCefApp.getInstance().createClient()
    val b = new JCEFHtmlPanel(client, null)
    b.getJBCefClient.setProperty(JBCefClient.Properties.JS_QUERY_POOL_SIZE, 10)
    b
  }

  lazy val query = {
    println("creating js query")
    JBCefJSQuery.create(browser.asInstanceOf[JBCefBrowserBase])
  }
}