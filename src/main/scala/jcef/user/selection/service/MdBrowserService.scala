package jcef.user.selection.service

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.jcef.{JBCefApp, JBCefClient, JCEFHtmlPanel}

class MdBrowserService(val project: Project) {

  val browser: JCEFHtmlPanel = {
    val client: JBCefClient = JBCefApp.getInstance().createClient()
    new JCEFHtmlPanel(client, null)
  }

  browser.getJBCefClient.setProperty(JBCefClient.Properties.JS_QUERY_POOL_SIZE, 10)
  Disposer.register(project, browser)
}
