/*******************************************************************************
 * Copyright (c) 2008 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.finders;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertNotSameWidget;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertText;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.eclipse.swtbot.swt.finder.utils.TreePath;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTabItem;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class ControlFinderTest extends AbstractControlExampleTest {

	@Test
	public void findsAGroup() throws Exception {
		final List<Group> frames = controlFinder.findControls(widgetOfType(Group.class));
		assertEquals(12, frames.size());
		assertText("Image Buttons", frames.get(2));
	}

	@Test
	public void findsAllTabItem() throws Exception {
		List<TabItem> tabItems = controlFinder.findControls(widgetOfType(TabItem.class));
		assertEquals(24, tabItems.size());
	}

	@Test
	public void findsAShell() throws Exception {
		List<Shell> shells = controlFinder.findShells("SWT Controls");
		assertFalse(shells.isEmpty());
		assertEquals(1, shells.size());
		assertEquals(shell, shells.get(0));
	}

	@Test
	public void findsATabItem() throws Exception {
		Matcher<TabItem> withText = withText("Dialog");
		List<TabItem> tabItems = controlFinder.findControls(allOf(widgetOfType(TabItem.class), withText));
		final TabItem items[] = new TabItem[] { null };
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				items[0] = ((TabFolder) shell.getChildren()[0]).getItems()[5];
			}
		});

		assertEquals(1, tabItems.size());
		assertEquals(items[0], tabItems.get(0));
	}

	@Test
	public void findsText() throws Exception {
		List<Text> textBoxes = controlFinder.findControls(widgetOfType(Text.class));
		assertEquals(1, textBoxes.size());
	}

	@Test
	public void findsTwoButtons() throws Exception {
		Matcher<Button> withText = withText("One");
		final List<Button> buttons = controlFinder.findControls(allOf(widgetOfType(Button.class), withText));
		assertEquals(2, buttons.size());
		assertText("One", buttons.get(0));
		assertText("One", buttons.get(1));
	}

	@Test
	public void getsControlPath() throws Exception {
		Matcher<Button> withText = withText("One");
		List<Button> labels = controlFinder.findControls(allOf(widgetOfType(Button.class), withText));
		Widget w = labels.get(0);
		TreePath path = controlFinder.getPath(w);
		assertEquals(7, path.getSegmentCount());
	}

	@Test
	public void getsControlPathToTabItem() throws Exception {
		Matcher<TabItem> withText = withText("Dialog");
		List<TabItem> tabItems = controlFinder.findControls(allOf(widgetOfType(TabItem.class), withText));
		TreePath path = controlFinder.getPath(tabItems.get(0));
		assertEquals(3, path.getSegmentCount());
	}

	@Test
	public void getsOnlyVisibleControls() throws Exception {
		// use the default tab
		List<Text> textBoxesOnButtonTab = controlFinder.findControls(widgetOfType(Text.class));
		assertEquals(1, textBoxesOnButtonTab.size());
		assertText("", textBoxesOnButtonTab.get(0));

		// switch to another tab
		Matcher<TabItem> withText = withText("Text");
		List<TabItem> tabItems = controlFinder.findControls(allOf(widgetOfType(TabItem.class), withText));
		new SWTBotTabItem(tabItems.get(0)).activate();

		// should get different tabs this time
		List<Text> textBoxesOnTextTab = controlFinder.findControls(widgetOfType(Text.class));
		assertEquals(2, textBoxesOnTextTab.size());
		assertNotSameWidget(textBoxesOnButtonTab.get(0), textBoxesOnTextTab.get(0));
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("Button").activate();
	}

}
