package ru.otus.library.util;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OAuth2ExHandler extends OAuth2MethodSecurityExpressionHandler {
    @Override
    public Object filter(Object filterTarget, Expression filterExpression,
                         EvaluationContext ctx) {
        MethodSecurityExpressionOperations rootObject = (MethodSecurityExpressionOperations) ctx
                .getRootObject().getValue();
        final boolean debug = logger.isDebugEnabled();
        List retainList;

        if (debug) {
            logger.debug("Filtering with expression: "
                    + filterExpression.getExpressionString());
        }

        if (filterTarget instanceof Collection) {
            Collection collection = (Collection) filterTarget;
            retainList = new ArrayList(collection.size());

            if (debug) {
                logger.debug("Filtering collection with " + collection.size()
                        + " elements");
            }


            for (Object filterObject : (Collection) filterTarget) {
                rootObject.setFilterObject(filterObject);

                if (ExpressionUtils.evaluateAsBoolean(filterExpression, ctx)) {
                    retainList.add(filterObject);
                }
            }

            if (debug) {
                logger.debug("Retaining elements: " + retainList);
            }

            collection.clear();
            collection.addAll(retainList);

            return filterTarget;
        }

        if (filterTarget.getClass().isArray()) {
            Object[] array = (Object[]) filterTarget;
            retainList = new ArrayList(array.length);

            if (debug) {
                logger.debug("Filtering array with " + array.length + " elements");
            }

            for (Object o : array) {
                rootObject.setFilterObject(o);

                if (ExpressionUtils.evaluateAsBoolean(filterExpression, ctx)) {
                    retainList.add(o);
                }
            }

            if (debug) {
                logger.debug("Retaining elements: " + retainList);
            }

            Object[] filtered = (Object[]) Array.newInstance(filterTarget.getClass()
                    .getComponentType(), retainList.size());
            for (int i = 0; i < retainList.size(); i++) {
                filtered[i] = retainList.get(i);
            }

            return filtered;
        }

        throw new IllegalArgumentException(
                "Filter target must be a collection or array type, but was "
                        + filterTarget);
    }

}