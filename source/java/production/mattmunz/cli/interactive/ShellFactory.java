package mattmunz.cli.interactive;

import static java.util.Arrays.asList;

import java.io.Console;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import mattmunz.cli.commandline.CommandLine;
import mattmunz.cli.commandline.Option;
import mattmunz.cli.commandline.Parser;

public abstract class ShellFactory
{
  private final String defaultLogsDirectoryName;
  private final String logFileName;
  private final String consoleName;
  private final String helpCommandName;
  private final Class<?> resourceClass;

  
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
    Option scriptOption = new Option('s', "script", true);
    Option logsOption = new Option('l', "logsDirectory", true);
    List<Option> options = asList(scriptOption, logsOption);

    FileSystem fileSystem = FileSystems.getDefault();

    Path defaultLogsPath = fileSystem.getPath(defaultLogsDirectoryName).toAbsolutePath();
    
    CommandLine commandLine = new Parser(options, 0, 4).parse(arguments);
    Path logsPath = commandLine.getOptionValue(logsOption, Path.class).orElse(defaultLogsPath);
    Optional<String> scriptPath = commandLine.getOptionValue(scriptOption, String.class);
    
    Console console = new ConsoleFactory().getSystemConsole();
    
    return new Shell(getInterpreter(console, fileSystem), resourceClass, console, logsPath, 
                     logFileName, scriptPath, consoleName, helpCommandName);
  } 
}
