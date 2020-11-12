package org.finalframework.aop.interceptor;


import org.finalframework.aop.OperationContext;
import org.finalframework.aop.OperationExpressionEvaluator;
import org.finalframework.aop.OperationHandlerSupport;
import org.finalframework.util.Asserts;
import org.springframework.expression.EvaluationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author likly
 * @version 1.0
 * @date 2019-03-27 21:13:02
 * @since 1.0
 */
public class AbsOperationHandlerSupport implements OperationHandlerSupport {

    /**
     * 表达式的开头标记
     */
    private static final String EXPRESSION_PREFIX = "${";
    /**
     * 表达式的结尾标记
     */
    private static final String EXPRESSION_SUFFIX = "}";


    private static final Pattern EXPRESSION_PATTEN = Pattern.compile("\\$\\{[^\\{\\}]*\\}");


    private final OperationExpressionEvaluator evaluator;

    public AbsOperationHandlerSupport(OperationExpressionEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public EvaluationContext createEvaluationContext(OperationContext context, Object result, Throwable e) {
        return evaluator.createEvaluationContext(context.metadata().getMethod(), context.args(),
                context.target(), context.metadata().getTargetClass(), context.metadata().getTargetMethod(), result, e);

    }

    public static void main(String[] args) {
        String str = "select * from order where createdUser = ${#1currentUser1} and  depart = ${'currentOrg' + #id} and status = 'VALID'";
        Matcher matcher = EXPRESSION_PATTEN.matcher(str);// 指定要匹配的字符串

        List<String> matchStrs = new ArrayList<>();

        while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            matchStrs.add(matcher.group());//获取当前匹配的值
        }

        for (int i = 0; i < matchStrs.size(); i++) {
            System.out.println(matchStrs.get(i));
        }

    }

    @Override
    public List<String> findExpressions(String expression) {
        Matcher matcher = EXPRESSION_PATTEN.matcher(expression);// 指定要匹配的字符串
        List<String> matchStrs = new ArrayList<>();
        while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            matchStrs.add(matcher.group());//获取当前匹配的值
        }
        return matchStrs;
    }

    /**
     * 以{@link AbsOperationHandlerSupport#EXPRESSION_PREFIX}开头，并且以{@link AbsOperationHandlerSupport#EXPRESSION_SUFFIX}结尾的字符串，为一个表达式。
     *
     * @param expression 表达式字符串
     */
    @Override
    public boolean isExpression(@Nullable String expression) {
        return StringUtils.hasText(expression) && expression.startsWith(EXPRESSION_PREFIX) && expression.endsWith(EXPRESSION_SUFFIX);
    }

    @Override
    public String generateExpression(@NonNull String expression) {
        Asserts.isEmpty(expression, "expression is empty");
        return expression.trim().substring(EXPRESSION_PREFIX.length(), expression.length() - EXPRESSION_SUFFIX.length());
    }
}