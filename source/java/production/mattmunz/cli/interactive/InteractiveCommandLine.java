package mattmunz.cli.interactive;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.Range;

import mattmunz.cli.commandline.CommandLine;
import mattmunz.cli.commandline.Parameter;

/**
 * A specialized command line for programs that support interactivity.
 * 
 *  TODO Ideally this should share the interface with CommandLine.
 */
public class InteractiveCommandLine
{
  private final Optional<String> scriptPath;
  private final Path logsPath;
  private final CommandLine commandLine;
  
  InteractiveCommandLine(Optional<String> scriptPath, Path logsPath, CommandLine commandLine)
  {
    this.scriptPath = scriptPath;
    this.logsPath = logsPath;
    this.commandLine = commandLine;
  }

  Optional<String> getScriptPath() { return scriptPath; }

  Path getLogsPath() { return logsPath; }

  public List<Parameter<?>> getParameters() { return commandLine.getParameters(); }

  public <V> Optional<Parameter<V>> getParameter(int index, Class<V> valueType) 
  {
    return commandLine.getParameter(index, valueType); 
  }

  public <V> Collection<Parameter<V>> getParameters(Range<Integer> range, Class<V> valueType)
  {
    return commandLine.getParameters(range, valueType);
  }
}
