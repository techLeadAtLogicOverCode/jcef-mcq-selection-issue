package jcef.user.selection.actions

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.util.Disposer
import com.intellij.ui.jcef.{JBCefBrowserBase, JBCefClient, JBCefJSQuery}
import jcef.user.selection.service.MdBrowserService

import java.util.function.Function

class FetchUserSelection extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {

    val projectBrowserService = ServiceManager.getService(e.getProject, classOf[MdBrowserService])
    val browser: JBCefBrowserBase = projectBrowserService.browser


    val query = projectBrowserService.jbCefJSQuery

    query.addHandler(new Function[String, JBCefJSQuery.Response]() {
      override def apply(s: String): JBCefJSQuery.Response = {
        println(System.currentTimeMillis() + " call back thread : " + Thread.currentThread().getName)
        println(s"string is >$s<")
        null
      }
    })


//    browser.getCefBrowser.executeJavaScript(
//      s"""
//         |var selectedProjects = 'Hello';
//         |alert(selectedProjects);
//         |${jbCefJSQuery.inject("selectedProjects")}
//         |""".stripMargin, browser.getCefBrowser.getURL, 0
//    )

    println(System.currentTimeMillis() + " executing java script in thread : " + Thread.currentThread().getName)
    browser.getCefBrowser.executeJavaScript(
      s"""
         |var elements = document.querySelectorAll('input[name=interpreted_language]:checked');
         |var selectedProjects = Array.from(elements).map(element => element.value).join(';');
         |${query.inject("selectedProjects")}
         |""".stripMargin, browser.getCefBrowser.getURL, 0
    )

    //Disposer.dispose(jbCefJSQuery)
  }
}
