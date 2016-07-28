package mattmunz.cli.interactive;

import java.io.Console;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

public abstract class ShellFactory
{
  private final String defaultLogsDirectoryName;
  private final String logFileName;
  private final String consoleName;
  private final String helpCommandName;
  private final Class<?> resourceClass;
  private final FileSystem fileSystem = FileSystems.getDefault();
  
  protected ShellFactory(Class<?> resourceClass, String defaultLogsDirectoryName, 
                         String logFileName, String consoleName, String helpCommandName)
  {
    this.resourceClass = resourceClass;
    this.defaultLogsDirectoryName = defaultLogsDirectoryName;
    this.logFileName = logFileName;
    this.consoleName = consoleName;
    this.helpCommandName = helpCommandName;
  }
  
  protected abstract CommandInterpreter getInterpreter(Console console, FileSystem fileSystem);

  public Shell getShell(String[] arguments) 
  {
    return getShell(getInteractiveCommandLine(arguments, 0, 4));
  }

  public Shell getShell(InteractiveCommandLine commandLine)
  {
    ShellEnvironment shellEnvironment
      = new ShellEnvironment(resourceClass, commandLine, logFileName);

    CommandInterpreter interpreter = getInterpreter(shellEnvironment.getConsole(), fileSystem);
    
    return new Shell(shellEnvironment, 
                     new InterpreterContext(interpreter, consoleName, helpCommandName));
  }

  private InteractiveCommandLine 
    getInteractiveCommandLine(String[] arguments, int minimumArgumentCount, 
                              int maximumArgumentCount)
  {
    InteractiveCommandLineParser parser 
      = new InteractiveCommandLineParser(minimumArgumentCount, maximumArgumentCount, 
                                         defaultLogsDirectoryName, fileSystem);
    
    return parser.parse(arguments);
  } 
}
