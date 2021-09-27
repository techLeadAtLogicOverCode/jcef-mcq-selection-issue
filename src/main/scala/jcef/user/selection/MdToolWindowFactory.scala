package jcef.user.selection

import com.intellij.openapi.actionSystem.{ActionManager, ActionPlaces, DefaultActionGroup}
import com.intellij.openapi.project.{DumbAware, Project}
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}
import com.intellij.ui.jcef._
import jcef.user.selection.service.HtmlToolWindowComponentsService

import java.awt.BorderLayout
import javax.swing.JPanel



class MdToolWindowFactory extends ToolWindowFactory with DumbAware{

  override def createToolWindowContent(project: Project, toolWindow: ToolWindow): Unit = {

    println("creating tool window component")

    val toolBarActionGroup = {
      val defaultActionGroup = new DefaultActionGroup()

      val fetchUserSelectionAction = ActionManager.getInstance().getAction("inspect.FetchUserSelection") ;
      defaultActionGroup.addAction(fetchUserSelectionAction)
      defaultActionGroup
    }


    val htmlToolWindowComponentsService = project.getService(classOf[HtmlToolWindowComponentsService])
    val browser: JBCefBrowser = htmlToolWindowComponentsService.browser
    val query = htmlToolWindowComponentsService.query

    val toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLWINDOW_TITLE, toolBarActionGroup, true) ;

    val toolWindowPanel = {
      val panel = new JPanel()
      panel.setLayout( new BorderLayout() )
      panel.add( browser.getComponent, BorderLayout.CENTER )
      panel.add( toolbar.getComponent, BorderLayout.NORTH )
      panel
    }

    toolbar.setTargetComponent(toolWindowPanel)
    toolWindow.getComponent.add( toolWindowPanel )

    Disposer.register(project, browser)
    Disposer.register(project, query)

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
