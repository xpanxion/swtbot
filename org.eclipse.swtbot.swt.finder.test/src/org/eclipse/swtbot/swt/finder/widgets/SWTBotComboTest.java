/*******************************************************************************
 * Copyright (c) 2008, 2017 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Cédric Chabanois - http://swtbot.org/bugzilla/show_bug.cgi?id=17
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.utils.SWTUtils.isCocoa;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotComboTest extends AbstractControlExampleTest {

	@Test
	public void findsCombo() throws Exception {
		SWTBotCombo combo = bot.comboBoxInGroup("Background Mode on Parent");
		assertNotNull(combo.widget);
	}

	@Test
	public void getItems() throws Exception {
		bot.tabItem("Combo").activate();
		SWTBotCombo comboBox = bot.comboBoxInGroup("Combo");
		assertEquals(9, comboBox.itemCount());
		List<String> items = Arrays.asList(comboBox.items());
		assertEquals(9, items.size());
		for (int i = 1; i < 9; i++)
			assertTrue("Expected to contain: " + "Line " + i, items.contains("Line " + i));
		assertTrue("Expected to contain: " + "Longest Line In List", items.contains("Longest Line In List"));
	}

	@Test
	public void changeAndVerifiesSelectionInComboByText() throws Exception {
		bot.tabItem("Combo").activate();
		SWTBotCombo comboBox = bot.comboBoxInGroup("Combo");
		comboBox.setSelection("Line 7");
		assertEquals("Line 7", comboBox.selection());
	}

	@Test
	public void changeAndVerifiesSelectionInComboByIndex() throws Exception {
		SWTBotCombo comboBox = bot.comboBoxInGroup("Combo");
		comboBox.setSelection(2);
		assertEquals(2, comboBox.selectionIndex());
		assertEquals("Line 3", comboBox.selection());
	}

	@Test
	public void throwsExceptionInCaseOfInvalidIndexBasedSelection() throws Exception {
		SWTBotCombo comboBox = bot.comboBoxInGroup("Combo");
		try {
			comboBox.setSelection(100);
			fail("Was expecting an exception");
		} catch (RuntimeException e) {
			assertEquals("The index (100) is more than the number of items (9) in the combo.", e.getMessage());
		}
	}

	@Test
	public void throwsExceptionInCaseOfInvalidTextBasedSelection() throws Exception {
		SWTBotCombo comboBox = bot.comboBoxInGroup("Combo");
		try {
			comboBox.setSelection("non existent item");
			fail("Was expecting an exception");
		} catch (RuntimeException e) {
			assertEquals("Item `non existent item' not found in combo box.", e.getMessage());
		}
	}

	@Test
	public void changeAndVerifiesTextInCombo() throws Exception {
		bot.checkBox("SWT.READ_ONLY").deselect();
		SWTBotCombo comboBox = bot.comboBoxInGroup("Combo");
		comboBox.setText("New Text");
		assertEquals("New Text", comboBox.getText());
	}

	@Test
	public void typeAndVerifiesTextInCombo() throws Exception {
		bot.checkBox("SWT.READ_ONLY").deselect();

		bot.checkBox("Listen").select();
		bot.button("Clear").click();

		SWTBotCombo comboBox = bot.comboBoxInGroup("Combo");
		comboBox.setText("");
		comboBox.typeText("New typed Text");
		assertEquals("New typed Text", comboBox.getText());

		if (!isCocoa())
			assertEventMatches(bot.textInGroup("Listeners"), "KeyDown [1]: KeyEvent{Combo {} time=490981104 data=null character=" + toCharacter('\0', comboBox.widget) + " keyCode=" + toKeyCode(131072, comboBox.widget) + " stateMask=" + toStateMask(0, comboBox.widget) + " doit=true}");
		assertEventMatches(bot.textInGroup("Listeners"), "KeyDown [1]: KeyEvent{Combo {} time=490981272 data=null character=" + toCharacter('N', comboBox.widget) + " keyCode=" + toKeyCode(110, comboBox.widget) + " stateMask=" + toStateMask(131072, comboBox.widget) + " doit=true}");
		assertEventMatches(bot.textInGroup("Listeners"), "Verify [25]: VerifyEvent{Combo {} time=490981272 data=null character=" + toCharacter('N', comboBox.widget) + " keyCode=" + toKeyCode(110, comboBox.widget) + " stateMask=" + toStateMask(131072, comboBox.widget) + " doit=true start=0 end=0 text=N}");
		assertEventMatches(bot.textInGroup("Listeners"), "KeyUp [2]: KeyEvent{Combo {N} time=490981352 data=null character=" + toCharacter('N', comboBox.widget) + " keyCode=" + toKeyCode(110, comboBox.widget) + " stateMask=" + toStateMask(131072, comboBox.widget) + " doit=true}");
		if (!isCocoa())
			assertEventMatches(bot.textInGroup("Listeners"), "KeyUp [2]: KeyEvent{Combo {N} time=490981457 data=null character=" + toCharacter('\0', comboBox.widget) + " keyCode=" + toKeyCode(131072, comboBox.widget) + " stateMask=" + toStateMask(131072, comboBox.widget) + " doit=true}");
	}

	@Test
	public void throwsExceptionInCaseOfChangeTextOfReadOnlyCombo() throws Exception {
		bot.checkBox("SWT.READ_ONLY").select();
		SWTBotCombo comboBox = bot.comboBoxInGroup("Combo");
		try {
			comboBox.setText("Forbidden Text");
			fail("Was expecting an exception");
		} catch (RuntimeException e) {
			assertEquals("This combo box is read-only.", e.getMessage());
		} finally {
			bot.checkBox("SWT.READ_ONLY").deselect();
		}
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("Combo").activate();
	}

}
