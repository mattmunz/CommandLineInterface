package mattmunz.cli.interactive;

import java.io.Console;
import java.nio.file.Path;
import java.util.Optional;

class ShellEnvironment
{
  private final Console console = new ConsoleFactory().getSystemConsole();
  private final Class<?> resourceClass;
  private final Optional<String> scriptPath;
  private final Path logsPath;
  private final String logFileName;
  
  ShellEnvironment(Class<?> resourceClass, InteractiveCommandLine commandLine, 
                   String logFileName)
  {
    this.resourceClass = resourceClass;
    this.scriptPath = commandLine.getScriptPath(); 
    this.logsPath = commandLine.getLogsPath();
    this.logFileName = logFileName;
  }

  Console getConsole() { return console; }

  Class<?> getResourceClass() { return resourceClass; }

  Optional<String> getScriptPath() { return scriptPath; }

  Path getLogsPath() { return logsPath; }

  String getLogFileName() { return logFileName; }
}
