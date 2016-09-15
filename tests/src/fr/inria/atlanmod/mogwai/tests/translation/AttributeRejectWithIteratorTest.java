package fr.inria.atlanmod.mogwai.tests.translation;

import org.junit.Test;

import fr.inria.atlanmod.mogwai.gremlin.Closure;
import fr.inria.atlanmod.mogwai.gremlin.ClosureIt;
import fr.inria.atlanmod.mogwai.gremlin.EqualityExpression;
import fr.inria.atlanmod.mogwai.gremlin.FilterStep;
import fr.inria.atlanmod.mogwai.gremlin.InEStep;
import fr.inria.atlanmod.mogwai.gremlin.NotExpression;
import fr.inria.atlanmod.mogwai.gremlin.OutVStep;
import fr.inria.atlanmod.mogwai.gremlin.PropertyStep;
import fr.inria.atlanmod.mogwai.gremlin.StringLiteral;
import fr.inria.atlanmod.mogwai.gremlin.VariableAccess;
import fr.inria.atlanmod.mogwai.gremlin.VariableDeclaration;

public class AttributeRejectWithIteratorTest extends MogwaiTranslationTest {
	
	/**
	 * Check the translation of a reject() call on an attribute with a declared iterator 
	 * to Gremlin steps
	 * A reject call generates a Gremlin filter step containing the
	 * select body in a negative expression:
	 * [previous steps].<b>filter{!<select body>}</b>
	 */
	@Test
	public void test() {
		assert gScript.getInstructions().size() == 3;
		// Ignore 1st and 2nd instructions, they are the same as in TypeAccess test
		assert gScript.getInstructions().get(2) instanceof VariableAccess;
		VariableAccess va = (VariableAccess)gScript.getInstructions().get(2);
		// Do not check the name of the variable access, it is already done in TypeAccess test
		// InEStep and OutVStep types are not checked, it is already done in AllInstances test
		InEStep inE = (InEStep)va.getNextElement();
		OutVStep outV = (OutVStep)inE.getNextElement();
		// Check the filter pipe associated to the select() call
		assert outV.getNextElement() instanceof FilterStep;
		FilterStep FilterStep = (FilterStep)outV.getNextElement();
		// Check the filter closure
		assert FilterStep.getClosure() instanceof Closure;
		// Check the content of the closure
		Closure filterClosure = (Closure)FilterStep.getClosure();
		assert filterClosure.getInstructions().size() == 2;
		// Check the local iterator declaration
		assert filterClosure.getInstructions().get(0) instanceof VariableDeclaration;
		VariableDeclaration vaClosure = (VariableDeclaration)filterClosure.getInstructions().get(0);
		assert vaClosure.getName().equals("each");
		assert vaClosure.getValue() instanceof ClosureIt;
		ClosureIt vaClosureIt = (ClosureIt)vaClosure.getValue();
		assert vaClosureIt.getNextElement() == null;
		// Check the negative expression
		assert filterClosure.getInstructions().get(1) instanceof NotExpression;
		NotExpression closureNot = (NotExpression)filterClosure.getInstructions().get(1);
		// Check equality in the negative expression
		assert closureNot.getExp() instanceof EqualityExpression;
		EqualityExpression closureEq = (EqualityExpression)closureNot.getExp();
		// Check equality left part
		assert closureEq.getLeft() instanceof VariableAccess;
		VariableAccess vaEqLeft = (VariableAccess)closureEq.getLeft();
		assert vaEqLeft.getName().equals("each");
		// Attribute access in left part
		assert vaEqLeft.getNextElement() instanceof PropertyStep;
		PropertyStep propertyEqLeft = (PropertyStep)vaEqLeft.getNextElement();
		assert propertyEqLeft.getName().equals("name");
		assert propertyEqLeft.getNextElement() == null;
		// Check equality right part
		assert closureEq.getRight() instanceof StringLiteral;
		StringLiteral stringLitEqRight = (StringLiteral)closureEq.getRight();
		assert stringLitEqRight.getValue().equals("format");
		// Check there is nothing generated after the filter pipe
		assert FilterStep.getNextElement() == null;
	}

}