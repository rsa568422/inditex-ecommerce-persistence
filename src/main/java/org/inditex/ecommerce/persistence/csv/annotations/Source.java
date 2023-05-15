package org.inditex.ecommerce.persistence.csv.annotations;

import org.inditex.ecommerce.persistence.csv.Files;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Source {

    Files fileName();

}
