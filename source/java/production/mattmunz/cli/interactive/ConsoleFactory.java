package mattmunz.cli.interactive;

import static java.lang.System.exit;

import java.io.Console;

/**
 * A helper designed for use with {@link Shell}. 
 */
class ConsoleFactory
{
  Console getSystemConsole()
  {
    Console console = System.console();

    if (console == null)
    {
      String message 
        = "A system console couldn't be set up. Only interactive mode is supported.";
      System.err.println(message);
      exit(1);
    }

    return console;
  }
}
