/**
 * Copyright (C) 2010, 2017 Bonitasoft S.A. and others.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Aurelien Pupier <aurelien.pupier@bonitasoft.com> - initial API and implementation
 */
package org.eclipse.swtbot.nebula.gallery.finder;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;


/**
 * This is a wrapper test case to the SWTBotTestCase that adds a Nebula bot
 * instead of the standard bot.
 *
 * @author Aurelien Pupier
 */
public class SWTBotNebulaTestCase extends SWTBotTestCase {
	protected SWTNebulaBot bot = new SWTNebulaBot();
}
