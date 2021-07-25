package org.smile.db.criteria;

import lombok.Getter;
import lombok.Setter;

public class SetCriterion implements Criterion{

    @Getter
    protected String fieldName;

    @Getter
    protected Object value;

    public SetCriterion(String fieldName,Object value){
        this.fieldName= fieldName;
        this.value=value;
    }

    @Override
    public void accept(CriterionVisitor visitor) {
        visitor.visit(this);
    }

}
