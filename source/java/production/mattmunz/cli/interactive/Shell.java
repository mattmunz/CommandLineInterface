package mattmunz.cli.interactive;

import static java.nio.file.Files.createDirectories;  
import static java.util.logging.Level.INFO;

import java.io.IOException;
import java.net.URISyntaxException;

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
  
  private ShellEnvironment environment;
  private final InterpreterContext interpreterContext;
  
  Shell(ShellEnvironment environment, InterpreterContext interpreterContext) 
  {
    this.environment = environment;
    this.interpreterContext = interpreterContext;
  }

  public void start() throws URISyntaxException, IOException
  {
    configureLogging();

    printWelcomeMessage();
    
    if (environment.getScriptPath().isPresent()) 
    { 
      interpreterContext.getInterpreter().evaluate("RUN " + environment.getScriptPath().get()); 
    }
    
    while (true) { evaluateNextCommandLine(); }
  }

  private void configureLogging() throws IOException 
  {
    LogHelper logHelper = new LogHelper(new FileHandlerProvider());

    createDirectories(environment.getLogsPath());
    
    logHelper.configureRootWithHandler(environment.getLogsPath(), 
                                       environment.getLogFileName(), INFO);
  }

  private void printWelcomeMessage() throws URISyntaxException
  {
    WelcomeMessageRepository repository 
      = new WelcomeMessageRepository("Welcome.txt", environment.getResourceClass(), 
                                     interpreterContext.getConsoleName(), 
                                     interpreterContext.getHelpCommandName());
    
    environment.getConsole().format(repository.getWelcomeMessage());
  }

  private void evaluateNextCommandLine()
  {
    interpreterContext.getInterpreter().evaluate(environment.getConsole().readLine(PROMPT).trim());
  }
}
