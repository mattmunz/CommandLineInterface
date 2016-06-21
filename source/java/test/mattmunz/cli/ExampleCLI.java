package mattmunz.cli;

import static java.util.Arrays.asList; 
import static java.util.Collections.emptyList;

import java.util.List;

public class ExampleCLI
{
	public static void main(String[] arguments) { new ExampleCLI().run(arguments); }

	private void run(String[] arguments)
	{
		System.out.println("Arguments: " + asList(arguments));
		
		List<Option> options = emptyList();
		
		CommandLine commandLine = new Parser(options, 2, 4).parse(arguments);
		
		List<Parameter<?>> parameters = commandLine.getParameters();
		
		System.out.println("Parameters: " + parameters);
		System.out.println("Options: " + commandLine.getOptions());
	}
}
