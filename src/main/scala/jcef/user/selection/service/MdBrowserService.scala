package jcef.user.selection.service

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.jcef.{JBCefApp, JBCefClient, JBCefJSQuery, JCEFHtmlPanel}

class MdBrowserService(val project: Project) {

  val browser: JCEFHtmlPanel = {
    val client: JBCefClient = JBCefApp.getInstance().createClient()
    val b = new JCEFHtmlPanel(client, null)
    b.getJBCefClient.setProperty(JBCefClient.Properties.JS_QUERY_POOL_SIZE, 10)
    b
  }

  val jbCefJSQuery = JBCefJSQuery.create(browser)
  Disposer.register(project, browser)
}
