package rukia.type;

/**
 * Created by niejia on 16/1/28.
 */
public class RukiaVariable {

    private String type;
    private String identifer;

    public RukiaVariable(String type, String variableName) {
        this.setType(type);
        this.setIdentifer(variableName);
    }


    public String toString() {
        return getType() + "#" + getIdentifer();
    }

    @Override
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        RukiaVariable that = (RukiaVariable) y;
        if (!this.getType().equals(that.getType())) return false;
        if (!this.getIdentifer().equals(that.getIdentifer())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + getType().hashCode();
        hash = 31 * hash + getIdentifer().hashCode();
        return hash;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentifer() {
        return identifer;
    }

    public void setIdentifer(String identifer) {
        this.identifer = identifer;
    }
}
