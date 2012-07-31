package bowling;

/**
 * This enum exists to restrict the possible values to a valid range and to
 * avoid possible confusion between adjacent int arguments.
 */
public enum Ball {
    ONE, TWO, THREE;
    
    public int getNumber() {
        return ordinal() + 1;
    }
    
}
