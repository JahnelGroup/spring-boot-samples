package com.jahnelgroup.spel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpelApplicationTests {

	@Test
	public void stringTest() {
		ExpressionParser expressionParser = new SpelExpressionParser();
		Expression expression = expressionParser.parseExpression("'Hello, World!'");
		String result = (String) expression.getValue();
		assertThat(result).isEqualTo("Hello, World!");
	}

	@Test
	public void simpleProperties() {
		User user = new User(1L, "Steven", "Zgaljic");
		ExpressionParser expressionParser = new SpelExpressionParser();
		{
			Expression expression = expressionParser.parseExpression("id");
			EvaluationContext context = new StandardEvaluationContext(user);
			Long result = (Long) expression.getValue(context);
			assertThat(result).isEqualTo(1L);
		}
		{
			Expression expression = expressionParser.parseExpression("firstName");
			EvaluationContext context = new StandardEvaluationContext(user);
			String result = (String) expression.getValue(context);
			assertThat(result).isEqualTo("Steven");
		}
	}

	@Test
	public void rootTest() {
		User user = new User(1L, "Steven", "Zgaljic");
		ExpressionParser expressionParser = new SpelExpressionParser();

		Expression expression = expressionParser.parseExpression("#root.id");
		EvaluationContext context = new StandardEvaluationContext(user);
		Long result = (Long) expression.getValue(context);
		assertThat(result).isEqualTo(1L);

	}

}

