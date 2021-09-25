package jcef.user.selection

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}
import com.intellij.ui.jcef.JBCefBrowser
import jcef.user.selection.service.MdBrowserService

class MdToolWindowFactory extends ToolWindowFactory {

  override def createToolWindowContent(project: Project, toolWindow: ToolWindow): Unit = {

    val projectBrowserService = ServiceManager.getService(project, classOf[MdBrowserService])

    val browser: JBCefBrowser = projectBrowserService.browser

    toolWindow.getComponent.add( browser.getComponent )

    browser.loadHTML( MdToolWindowFactory.htmlContent )
  }
}

object MdToolWindowFactory{
  val htmlContent =
    """
      |<!DOCTYPE html>
      |<html>
      |
      |<head>
      |    <script type="text/javascript">
      |            function user_selection() {
      |
      |               var elements = document.querySelectorAll('input[name=interpreted_language]:checked');
      |               var selectedProjects = Array.from(elements).map(element => element.value).join(';');
      |               alert (selectedProjects);
      |            }
      |
      |            function hardCoded() {
      |
      |               var selectedProjects = 'Hello';
      |               alert (selectedProjects);
      |            }
      |
      |    </script>
      |</head>
      |
      |<body>
      |
      |<form>
      |    <p>Which is a interpreted language ?</p>
      |    <input type="radio" id="c" name="interpreted_language" value="C">
      |    <label for="c">C</label><br>
      |    <input type="radio" id="java" name="interpreted_language" value="Java">
      |    <label for="java">Java</label><br>
      |    <input type="radio" id="python" name="interpreted_language" value="Python">
      |    <label for="python">Python</label>
      |
      |    <input type="button" id="user selection" value="Find User Input" onclick="user_selection();"/>
      |
      |</form>
      |</body>
      |</html>
      |""".stripMargin
}
