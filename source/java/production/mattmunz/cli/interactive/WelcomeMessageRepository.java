package mattmunz.cli.interactive;

import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import mattmunz.lang.ClassResourceHelper;
import mattmunz.lang.SystemHelper;

// TODO Reuse this in Spring Shell integration somehow... 
public class WelcomeMessageRepository
{
  private static final Logger logger = getLogger(WelcomeMessageRepository.class.getName());
  
  private final String bannerFileName, consoleName, helpCommandName;
  private final Class<?> resourceClass;
  
  public WelcomeMessageRepository(String bannerFileName, Class<?> resourceClass, 
                                  String consoleName, String helpCommandName)
  {
    this.bannerFileName = bannerFileName;
    this.consoleName = consoleName;
    this.helpCommandName = helpCommandName;
    this.resourceClass = resourceClass;
  }
  
  public String getWelcomeMessage()
  {
    try (InputStream resource = new ClassResourceHelper().getResource(resourceClass, bannerFileName); 
         BufferedReader reader = new BufferedReader(new InputStreamReader(resource)))
    {
      StringBuilder messageBuilder = new StringBuilder();

      // TODO Is there a nicer way to do this with streams?
      reader.lines().forEach(line -> messageBuilder.append(format("%s%n", line)));
      
      return messageBuilder.toString();
    }
    catch (IOException e)
    {
      String message 
        = format("Couldn't load the welcome banner file: %s, from class/classpath: %s, %s", 
                 bannerFileName, resourceClass.getName(), new SystemHelper().getClasspath());

      logger.log(SEVERE, message, e);
      
      return format("Welcome to the %s console! Type '%s' to get started.%n", consoleName, 
                    helpCommandName);
    }
  }

}
