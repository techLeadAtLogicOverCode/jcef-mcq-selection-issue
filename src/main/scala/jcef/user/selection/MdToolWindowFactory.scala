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
      |            function findAnswers() {
      |
      |               var elements = document.querySelectorAll('input[name=mcq]:checked');
      |               var selectedAnswers = Array.from(elements).map(element => element.id).join(';');
      |               var correctAnswers = document.getElementById('answers').value;
      |               return selectedAnswers.concat(':').concat(correctAnswers);
      |            }
      |
      |    </script>
      |</head>
      |
      |<body>
      |
      |<form>
      |    <p>Which is a interpreted language ?</p>
      |    <input type="radio" id="a" name="mcq" value="C">
      |    <label for="a">C</label><br>
      |    <input type="radio" id="b" name="mcq" value="Python">
      |    <label for="b">Python</label><br>
      |    <input type="radio" id="c" name="mcq" value="Java">
      |    <label for="c">Java</label><br>
      |
      |    <input type="button" id="user selection" value="Find User Input" onclick="user_selection();"/>
      |
      |    <input type="hidden" id="answers" value="c">
      |
      |</form>
      |</body>
      |</html>
      |""".stripMargin
}
