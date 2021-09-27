package jcef.user.selection.actions

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.ui.jcef.JBCefJSQuery
import jcef.user.selection.service.HtmlToolWindowComponentsService

import java.util.function.Function

class FetchUserSelection extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {


    val htmlToolWindowComponentsService = e.getProject.getService(classOf[HtmlToolWindowComponentsService])
    val browser = htmlToolWindowComponentsService.browser


    val query = htmlToolWindowComponentsService.query


    query.addHandler(new Function[String, JBCefJSQuery.Response]() {
      override def apply(s: String): JBCefJSQuery.Response = {
        println(System.currentTimeMillis() + " call back thread : " + Thread.currentThread().getName)
        println(s"string is >$s<")
        null
      }
    })

    println(System.currentTimeMillis() + " executing java script in thread : " + Thread.currentThread().getName)

    browser.getCefBrowser.executeJavaScript(
      s"""
         |var elements = document.querySelectorAll('input[name=interpreted_language]:checked');
         |var selectedProjects = Array.from(elements).map(element => element.value).join(';');
         |${query.inject("selectedProjects")}
         |""".stripMargin, browser.getCefBrowser.getURL, 0
    )
  }
}
