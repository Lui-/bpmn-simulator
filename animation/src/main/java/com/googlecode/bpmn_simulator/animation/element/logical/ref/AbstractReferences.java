/*
 * Copyright (C) 2014 Stefan Schweitzer
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
package com.googlecode.bpmn_simulator.animation.element.logical.ref;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

abstract class AbstractReferences<E>
		implements References<E> {

	private final Set<Reference<E>> references = new HashSet<Reference<E>>();

	protected void add(final Reference<E> reference) {
		references.add(reference);
	}

	@Override
	public Iterator<E> iterator() {
		final Collection<E> elements = new ArrayList<E>();
		for (final Reference<E> reference : references) {
			final E element = reference.getReferenced();
			if (element != null) {
				elements.add(element);
			}
		}
		return elements.iterator();
	}

	@Override
	public boolean isEmpty() {
		return getReferencedCount() > 0;
	}

	public boolean hasInvalidReferences() {
		for (final Reference<E> reference : references) {
			if ((reference == null) || !reference.hasReference()) {
				return true;
			}
		}
		return false;
	}

	private int getReferencedCount() {
		int count = 0;
		for (final Reference<E> reference : references) {
			if (reference.hasReference()) {
				++count;
			}
		}
		return count;
	}

}
