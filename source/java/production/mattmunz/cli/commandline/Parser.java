package mattmunz.cli.commandline;

import static java.util.Arrays.asList; 
import static java.util.stream.Collectors.toList;
import static java.util.Optional.empty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Range;

public class Parser
{
	private final List<Option> options;

	private final Range<Integer> argumentCountRange;

	Parser() { this(Collections.<Option>emptyList()); }

	Parser(List<Option> options) { this(options, 0, options.size()); }

	/**
	 * TODO Since Option#hasValue indicates whether a value is required for the option, 
	 *      minimumArgumentCount and maximumArgumentCount are almost redundant. If this method 
	 *      were to also take a required/optional attribute for each option, then  
	 *      minimumArgumentCount/maximumArgumentCount can be determined from this information.
	 *      That method signature would look like Parser(requiredOptions, optionalOptions).
	 */
	public Parser(List<Option> options, Integer minimumArgumentCount,
				        Integer maximumArgumentCount)
	{
		this.options = options;
		
		if (minimumArgumentCount < 0 || minimumArgumentCount > maximumArgumentCount)
		{
			String message 
				= "Min/max argument count interval is invalid: (" 
			      + maximumArgumentCount + ", " + maximumArgumentCount + ").";
			
			throw new IllegalArgumentException(message);
		}
		
		argumentCountRange = Range.closed(minimumArgumentCount, maximumArgumentCount);
	}

	public CommandLine parse(String[] argumentTokens) throws IllegalArgumentException
	{
		validateArgumentCount(argumentTokens.length);
		
		List<Parameter<?>> parameters = new ArrayList<>();
		Set<Option> foundOptions = new HashSet<>();
    HashMap<Option, Parameter<?>> optionValues = new HashMap<Option, Parameter<?>>();
    Optional<Option> lastOption = empty(); 
		
		List<Argument> arguments 
		  = asList(argumentTokens).stream().map(this::parseArgument).collect(toList());
		
		for (Argument argument : arguments)
		{
		  lastOption = addArgument(argument, foundOptions, parameters, optionValues, lastOption);
		}
		
    return new CommandLine(parameters, foundOptions, optionValues);
	}
	
	private Optional<Option> 
	  addArgument(Argument argument, Set<Option> foundOptions, List<Parameter<?>> parameters, 
	              HashMap<Option, Parameter<?>> optionValues, Optional<Option> lastOption)
	{
	  if (argument instanceof Option)
    {
      Option option = (Option) argument;
      foundOptions.add(option);
      return Optional.of(option);
    }
    
    if (!(argument instanceof Parameter)) 
    {
      throw new IllegalStateException("Unknown argument type: " + argument); 
    } 
      
    if (!lastOption.isPresent() || !lastOption.get().hasValue()) 
    { 
      parameters.add((Parameter<?>) argument); return empty();
    }
      
    optionValues.put(lastOption.get(), (Parameter<?>) argument);
    return empty(); 
	}
	
	private Argument parseArgument(String argumentText)
	{
	  String trimmedArgument = argumentText.trim();

    if (options.isEmpty() || !trimmedArgument.startsWith("-")) 
    {
      return new Parameter<String>(trimmedArgument); 
    }
    
    if (trimmedArgument.startsWith("--"))
    {
      if (trimmedArgument.length() < 3) 
      {
        String message = "Option argument [" + trimmedArgument + "] is too short.";
        throw new IllegalArgumentException(message);
      }
      
      return getOption(trimmedArgument.substring(2));
    } 
      
    if (trimmedArgument.length() != 2)
    {
      String message = "Option argument [" + trimmedArgument + "] is too long.";
      throw new IllegalArgumentException(message);
    }

    return getOption(trimmedArgument.charAt(1));
	}

  private void validateArgumentCount(int argumentCount)
  {		
		if (!argumentCountRange.contains(argumentCount))
		{
		  String message 
		    = "Wrong number of arguments [" + argumentCount + "]. Must be in the range [" 
          + argumentCountRange + "].";
		  throw new IllegalArgumentException(message);
		}
  }

	/**
	 * TODO Introduce a hashtable to avoid looping over options every time.
	 *      Creating the hashtable in the constructor will also validate against the 
	 *      scenario where two options have the same short flag character.
	 */
	private Option getOption(char shortFlag) throws IllegalArgumentException
	{
		for (Option option : options)
		{
			if (option.getShortFlag() == shortFlag) { return option; }
		}
		
		throw new IllegalArgumentException("Unknown short flag: " + shortFlag);
	}

  /**
   * TODO Also Replace this loop with a hashtable...
   */
  private Option getOption(String longFlag)
  {
    for (Option option : options)
    {
      if (option.getLongFlag().equals(longFlag)) { return option; }
    }
    
    throw new IllegalArgumentException("Unknown long flag: " + longFlag);
  }
}
