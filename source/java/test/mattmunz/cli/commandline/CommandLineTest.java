package mattmunz.cli.commandline;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.junit.Assert.assertEquals;

import java.util.List;

import mattmunz.cli.commandline.CommandLine;
import mattmunz.cli.commandline.Parameter;

import org.junit.Test;

import com.google.common.collect.Range;

// TODO There's some verbose code here which could use refactoring
public class CommandLineTest
{
	@Test
	public void getParameter()
	{
		List<Parameter<?>> parameters = asList(new Parameter<String>("42"), new Parameter<String>("foo"));
		CommandLine commandLine = new CommandLine(parameters, emptySet(), emptyMap());
		
		assertEquals(new Integer(42), commandLine.getParameter(0,Integer.class).get().getValue());
		assertEquals("foo", commandLine.getParameter(1, String.class).get().getValue());
	}

	@Test
	public void getParametersInRange()
	{
		List<Parameter<?>> parameters 
			= asList(new Parameter<String>("foo"), new Parameter<String>("42"), new Parameter<String>("614"));
		CommandLine commandLine = new CommandLine(parameters, emptySet(), emptyMap());
		
		List<Parameter<Integer>> actualParameters 
			= commandLine.getParameters(Range.greaterThan(0), Integer.class);
		
		assertEquals("Two many parameters: " + actualParameters, 2, actualParameters.size());
		
		assertEquals(new Integer(42), actualParameters.get(0).getValue());
		assertEquals(new Integer(614), actualParameters.get(1).getValue());
	}
}
