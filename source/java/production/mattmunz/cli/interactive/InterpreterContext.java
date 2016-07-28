package mattmunz.cli.interactive;

class InterpreterContext
{
  private final CommandInterpreter interpreter;
  private final String consoleName;
  private final String helpCommandName;

  InterpreterContext(CommandInterpreter interpreter, String consoleName, 
                     String helpCommandName) 
  {
    this.interpreter = interpreter;
    this.consoleName = consoleName;
    this.helpCommandName = helpCommandName;
  }

  CommandInterpreter getInterpreter() { return interpreter; }

  String getConsoleName() { return consoleName; }

  String getHelpCommandName() { return helpCommandName; }
}
