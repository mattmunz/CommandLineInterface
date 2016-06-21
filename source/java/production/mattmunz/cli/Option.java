package mattmunz.cli;

/**
 * A model of the option itself which does not include any associated parameter value. For 
 * example an option like -h or --hostname which requires a following parameter like foo.org 
 * would need to be represented as an Option paired with a Parameter.  
 */
public class Option implements Argument
{
	private final char shortFlag;
	private final String longFlag;
	private final boolean hasValue;
	
	public Option(char shortFlag, String longFlag, boolean hasValue)
  {
	  this.shortFlag = shortFlag;
	  this.longFlag = longFlag;
	  this.hasValue = hasValue;
  }

  public String getLongFlag() { return longFlag; }

  char getShortFlag() { return shortFlag; }

  boolean hasValue() { return hasValue; }
}
