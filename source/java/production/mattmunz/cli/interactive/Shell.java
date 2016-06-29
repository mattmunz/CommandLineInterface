package mattmunz.cli.interactive;

import static java.nio.file.Files.createDirectories;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import mattmunz.lang.ClassResourceHelper;
import mattmunz.lang.SystemHelper;
import mattmunz.logging.FileHandlerProvider;
import mattmunz.logging.LogHelper;

/**
 * This class handles the basic logistics common to all shells. The 
 * {@link CommandInterpreter} does the domain-specific stuff -- it is the brains of the 
 * shell.
 */
public class Shell
{
  private static final String PROMPT = "> ";
  
  private static final Logger logger = getLogger(Shell.class.getName());

  /*
   * TODO There are a lot of fields here. Perhaps condense into a class or two. Maybe 
   *      Environment (paths, system console, class), and InterpreterContext 
   *      (domain-specific bits)
   */
  
  private final Optional<String> scriptPath;
  private final CommandInterpreter interpreter;
  private final Console console;
  private final Path logsPath;
  private final String logFileName;
  private final String consoleName;
  private final String helpCommandName;
  private final Class<?> resourceClass;
  
  Shell(CommandInterpreter interpreter, Class<?> resourceClass, Console console, Path logsPath, String logFileName, 
        Optional<String> scriptPath, String consoleName, String helpCommandName) 
  {
    this.scriptPath = scriptPath; 
    this.interpreter = interpreter;
    this.resourceClass = resourceClass;
    this.console = console;
    this.logsPath = logsPath;
    this.logFileName = logFileName;
    this.consoleName = consoleName;
    this.helpCommandName = helpCommandName;
  }

  public void start() throws URISyntaxException, IOException
  {
    configureLogging();

    printWelcomeMessage();
    
    if (scriptPath.isPresent()) { interpreter.evaluate("RUN " + scriptPath.get()); }
    
    while (true) { evaluateNextCommandLine(); }
  }

  private void configureLogging() throws IOException 
  {
    LogHelper logHelper = new LogHelper(new FileHandlerProvider());

    createDirectories(logsPath);
    
    logHelper.configureRootWithHandler(logsPath, logFileName, INFO);
  }

  private void printWelcomeMessage() throws URISyntaxException
  {
    String bannerFileName = "Welcome.txt";
    
    try (InputStream resource = new ClassResourceHelper().getResource(resourceClass, bannerFileName); 
         BufferedReader reader = new BufferedReader(new InputStreamReader(resource)))
    {
      reader.lines().forEach(line -> console.format("%s%n", line));
    }
    catch (IOException e)
    {
      String message 
        = "Couldn't load the welcome banner file: " + bannerFileName + ", from class/classpath: " 
          + resourceClass.getName() + ", " + new SystemHelper().getClasspath();

      logger.log(SEVERE, message, e);
      
      console.format("Welcome to the " + consoleName + " console! Type '" 
                     + helpCommandName + "' to get started.%n");
    }
  }

  private void evaluateNextCommandLine()
  {
    interpreter.evaluate(console.readLine(PROMPT).trim());
  }
}
