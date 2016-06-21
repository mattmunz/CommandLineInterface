package mattmunz.cli;

import static java.util.Optional.ofNullable; 

import java.util.ArrayList;
import java.util.List; 
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Range;

import mattmunz.Converter;

public class CommandLine
{
  private final Converter converter = new Converter();
	private final List<Parameter<?>> parameters;
	private final Set<Option> options;
	private final Map<Option, Parameter<?>> optionValues;

	CommandLine(List<Parameter<?>> parameters, Set<Option> options, 
	            Map<Option, Parameter<?>> optionValues)
	{
		this.parameters = parameters;
		this.options = options;
		this.optionValues = optionValues;
	}
  
  /**
   * @return As a convenience the parameter in the desired value type, if possible. 
   */
  public <V> Optional<Parameter<V>> getParameter(int index, Class<V> valueType)
  {
    return ofNullable(parameters.get(index)).map(parameter -> convert(parameter, valueType));
  }

	/**
	 * @return The list of parameters that are not bound to options, in order. 
	 */
	public List<Parameter<?>> getParameters() { return parameters; }
	
	/**
	 * @return a list of all parameters with index in range, and type valueType.
	 */
  public <V> List<Parameter<V>> getParameters(Range<Integer> range, Class<V> valueType)
  {
    List<Parameter<V>> selectedParameters = new ArrayList<>();
    
    int i = 0;
    for (Parameter<?> parameter : parameters)
    {
      if (range.contains(i)) { selectedParameters.add(convert(parameter, valueType)); }
      i++;
    }
    
    return selectedParameters;
  }

	public Set<Option> getOptions() { return options; }

  public <V> Optional<V> getOptionValue(Option option, Class<V> valueType)
  {
    return ofNullable(optionValues.get(option))
              .map(value -> convert(value, valueType).getValue());
  }
  
  boolean hasOption(Option option) { return options.contains(option); }
  
  private <T> Parameter<T> convert(Parameter<?> parameter, Class<T> targetType)
  {
    return new Parameter<T>(converter.convert(parameter.getValue(), targetType));
  }
}
