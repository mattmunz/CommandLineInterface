package mattmunz.cli.commandline;

import static java.util.Arrays.asList; 
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import mattmunz.cli.commandline.CommandLine;
import mattmunz.cli.commandline.Option;
import mattmunz.cli.commandline.Parameter;
import mattmunz.cli.commandline.Parser;

import org.junit.Test;

public class ParserTest
{
	@Test
	public void parse()
	{
		CommandLine commandLine = new Parser().parse(new String[]{});
		
		assertIsEmpty(commandLine.getParameters());
		assertIsEmpty(commandLine.getOptions());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void noArgumentsExpected()
	{
		new Parser().parse(new String[]{"foo"});
	}

	@Test
	public void parseWithArguments()
	{
		List<Option> options = emptyList();
		
		Integer minimumArgumentCount = 2;
		Integer maximumArgumentCount = 4;
		
		Parser parser = new Parser(options, minimumArgumentCount, maximumArgumentCount);
		
		String[] arguments = new String[]{"foo", "bar", "baz"};
		
		List<Parameter<?>> parameters = parser.parse(arguments).getParameters();
		
		assertEquals(3, parameters.size());
		
		int index = 0;
		
		for (Parameter<?> parameter : parameters)
		{
			assertEquals(arguments[index], parameter.getValue());
			
			index++;
		}
	}
	
	@Test
	public void parseWithOneOption()
	{
		Option option = new Option('c', "character", false);
		List<Option> options = asList(option);
		
		CommandLine commandLine = new Parser(options).parse(new String[]{"-c"});
		
		assertIsEmpty(commandLine.getParameters());
		
		Set<Option> actualOptions = commandLine.getOptions(); 
		
		assertEquals(1, actualOptions.size());
		assertEquals(option, actualOptions.iterator().next());
		assertTrue(commandLine.hasOption(option));		
	}

	@Test(expected=IllegalArgumentException.class)
	public void parseWithWrongFlag()
	{
		new Parser(asList(new Option('j', "java", false))).parse(new String[]{"-j", "-c"});
	}
	
  @Test(expected=IllegalArgumentException.class)
  public void parseWithTooFewArguments()
  {
    new Parser(asList(new Option('h', "host", false)), 2, 2).parse(new String[]{"-h"});
  }
  
  @Test
  public void parseWithMultipleArguments()
  {
    new Parser(asList(new Option('h', "host", false)), 2, 2).parse(new String[]{"-h", "www.foo.org"});
  }
  
  @Test
  public void parseWithOptionsWithValues()
  {
    Option scriptOption = new Option('s', "script", true);
    List<Option> options = asList(scriptOption, new Option('l', "logsDirectory", true));
    
    CommandLine commandLine = new Parser(options, 0, 2).parse(new String[]{"--script", "foo.sh"});
    
    assertEquals("foo.sh", commandLine.getOptionValue(scriptOption, String.class).get());
  }

	private void assertIsEmpty(Collection<?> items) { assertTrue(items.isEmpty()); }
}
