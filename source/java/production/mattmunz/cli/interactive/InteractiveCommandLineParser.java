package mattmunz.cli.interactive;

import static java.util.Arrays.asList;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Optional;

import mattmunz.cli.commandline.CommandLine;
import mattmunz.cli.commandline.Option;
import mattmunz.cli.commandline.Parser;

public class InteractiveCommandLineParser
{
  private final Option logsOption = new Option('l', "logsDirectory", true);
  private final Option scriptOption = new Option('s', "script", true);
  private final Parser parser;
  private Path defaultLogsPath;

  public InteractiveCommandLineParser(int minimumArgumentCount, int maximumArgumentCount, 
                                      String defaultLogsDirectoryName, FileSystem fileSystem)
  {
    defaultLogsPath = fileSystem.getPath(defaultLogsDirectoryName).toAbsolutePath();
    parser = new Parser(asList(scriptOption, logsOption), minimumArgumentCount, 
                        maximumArgumentCount);
  }

  public InteractiveCommandLine parse(String[] arguments)
  {
    CommandLine commandLine = parser.parse(arguments);
    
    Path logsPath = commandLine.getOptionValue(logsOption, Path.class).orElse(defaultLogsPath);
    Optional<String> scriptPath = commandLine.getOptionValue(scriptOption, String.class);

    return new InteractiveCommandLine(scriptPath, logsPath, commandLine);
  }
}
