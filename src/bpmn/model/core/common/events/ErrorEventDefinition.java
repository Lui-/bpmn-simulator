/*
 * Copyright (C) 2012 Stefan Schweitzer
 *
 * This software was created by Stefan Schweitzer as a student's project at
 * Fachhochschule Kaiserslautern (University of Applied Sciences).
 * Supervisor: Professor Dr. Thomas Allweyer. For more information please see
 * http://www.fh-kl.de/~allweyer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this Software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bpmn.model.core.common.events;

import javax.swing.Icon;

import bpmn.model.ElementRef;
import bpmn.model.core.common.Error;
import bpmn.model.core.common.Visualization;
import bpmn.token.Token;

public final class ErrorEventDefinition extends EventDefinition {

	private final ElementRef<Error> errorRef;

	public ErrorEventDefinition(final AbstractEvent event,
			final ElementRef<Error> errorRef) {
		super(event);
		this.errorRef = errorRef;
	}

	private ElementRef<Error> getErrorRef() {
		return errorRef;
	}

	@Override
	public Icon getIcon(final Visualization visualization, final boolean inverse) {
		return visualization.getIcon(inverse
				? Visualization.ICON_ERROR_INVERSE
				: Visualization.ICON_ERROR);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof ErrorEventDefinition) {
			final ErrorEventDefinition definition = (ErrorEventDefinition)obj;
			return super.equals(obj)
					|| (getErrorRef().equals(definition.getErrorRef()));
		}
		return false;
	}

	@Override
	public void throwTrigger(final Token token) {
		super.throwTrigger(token);

		throwTriggerToEqualEvents(token);
	}

}
