package epmc.util;

public final class Pair<TypeLeft, TypeRight> {
    private final TypeLeft elementLeft;
    private final TypeRight elementRight;
    
    public Pair(TypeLeft elementLeft, TypeRight elementRight) {
            this.elementLeft = elementLeft;
            this.elementRight = elementRight;
    }
    
    public TypeLeft getLeftElement() {
            return elementLeft;
    }
    
    public TypeRight getRightElement() {
            return elementRight;
    }
}