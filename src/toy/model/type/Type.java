package toy.model.type;

import toy.model.value.Value;

public interface Type {
    boolean equals(Object another);
    Value defaultValue();
}
