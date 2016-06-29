package mattmunz.cli.commandline;


public class Parameter<V> implements Argument
{
	private final V value;
  
  Parameter(V value) { this.value = value; }

	@Override
	public String toString() { return value.toString(); }

	public V getValue() { return value; }
}
