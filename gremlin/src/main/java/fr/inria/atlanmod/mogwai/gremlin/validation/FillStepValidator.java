/**
 *
 * $Id$
 */
package fr.inria.atlanmod.mogwai.gremlin.validation;

import fr.inria.atlanmod.mogwai.gremlin.Instruction;

/**
 * A sample validator interface for {@link fr.inria.atlanmod.mogwai.gremlin.FillStep}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface FillStepValidator {
	boolean validate();

	boolean validateInstruction(Instruction value);
}
