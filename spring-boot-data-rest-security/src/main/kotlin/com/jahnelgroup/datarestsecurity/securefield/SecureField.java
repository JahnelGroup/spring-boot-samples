package com.jahnelgroup.datarestsecurity.securefield;

import com.jahnelgroup.datarestsecurity.securefield.policy.CreatedByFieldSecurityPolicy;
import com.jahnelgroup.datarestsecurity.securefield.policy.FieldSecurityPolicy;
import com.jahnelgroup.datarestsecurity.securefield.policy.PolicyLogic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value= {ElementType.FIELD})
public @interface SecureField {

    Class<? extends FieldSecurityPolicy>[] policies() default CreatedByFieldSecurityPolicy.class;

    PolicyLogic[] logic() default PolicyLogic.AND;

}
