/*******************************************************************************
 * Copyright (c) 2008, 2016 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Ralf Ebert www.ralfebert.de - (bug 271630) SWTBot Improved RCP / Workbench support
 *     Patrick Tasse - SWTBotView does not support dynamic view menus (Bug 489325)
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.widgets;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withMnemonic;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import javax.swing.text.View;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.finders.CommandFinder;
import org.eclipse.swtbot.eclipse.finder.finders.ViewMenuFinder;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.utils.PartLabelDescription;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.waits.WaitForObjectCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotRootMenu;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.hamcrest.SelfDescribing;

/**
 * This represents the eclipse {@link View} item.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @author Ralf Ebert www.ralfebert.de (bug 271630)
 * @version $Id$
 */
public class SWTBotView extends SWTBotWorkbenchPart<IViewReference> {

	private final ViewMenuFinder	menuFinder;
	/**
	 * Creates an instance of a view part.
	 * 
	 * @param partReference the view reference representing this view.
	 * @param bot the bot that's used to find controls within this view.
	 * @since 2.0
	 */
	public SWTBotView(IViewReference partReference, SWTWorkbenchBot bot) {
		this(partReference, bot, new PartLabelDescription<IViewReference>(partReference));
	}

	/**
	 * Creates an instance of a view part.
	 * 
	 * @param partReference the part reference.
	 * @param bot the helper bot.
	 * @param description the description of the workbench part.
	 */
	public SWTBotView(IViewReference partReference, SWTWorkbenchBot bot, SelfDescribing description) {
		super(partReference, bot, description);
		this.menuFinder = new ViewMenuFinder();
	}

	public void setFocus() {
		syncExec(new VoidResult() {
			public void run() {
				((Control) getWidget()).setFocus();
			}
		});
	}

	/**
	 * @return the view reference for this view.
	 */
	public IViewReference getViewReference() {
		return partReference;
	}

	public boolean isActive() {
		return partReference.getPage().getActivePartReference() == partReference;
	}

	/**
	 * Close the partReference.
	 */
	public void close() {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				partReference.getPage().hideView(partReference);
			}
		});
	}

	/**
	 * Gets the view menu of this view.
	 *
	 * @return the view menu.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 2.4
	 */
	public SWTBotRootMenu viewMenu() {
		WaitForObjectCondition<Menu> waitForMenu = Conditions.waitForViewMenu(partReference);
		new SWTBot().waitUntilWidgetAppears(waitForMenu);
		return new SWTBotRootMenu(waitForMenu.get(0));
	}

	/**
	 * Gets the view menu item matching the given text. It will attempt to
	 * find the menu item recursively in each of the sub-menus that are found.
	 * <p>
	 * This is equivalent to calling viewMenu().menu(text, true, 0);
	 *
	 * @param text the text on the view menu item.
	 * @return the view menu item that has the given text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 2.4
	 */
	public SWTBotMenu viewMenu(final String text) throws WidgetNotFoundException {
		return viewMenu().menu(text, true, 0);
	}

	/**
	 * Gets a list of all menus within the partReference. This will also include sub menus.
	 * 
	 * @return The list of menus
	 * @deprecated Use {@link #viewMenu()} and get specific menu items instead.
	 */
	@Deprecated
	public List<SWTBotViewMenu> menus() {
		return menuFinder.findMenus(partReference, anything(), true);
	}

	/**
	 * Gets a menu item matching the give label within the partReference menu if one exists.
	 * 
	 * @param label The label matching name in the menu.
	 * @return The {@link SWTBotMenu} item.
	 * @throws WidgetNotFoundException Thrown if the menu can not be found or if the partReference does not contain a
	 *             menu.
	 * @deprecated Use {@link #viewMenu(String)} instead.
	 */
	@Deprecated
	public SWTBotViewMenu menu(String label) throws WidgetNotFoundException {
		return menu(label, 0);
	}

	/**
	 * Gets a menu item matching the give label within the partReference menu if one exists.
	 * 
	 * @param label The label matching name in the menu.
	 * @param index The index of the menu to choose.
	 * @return The {@link SWTBotMenu} item.
	 * @throws WidgetNotFoundException Thrown if the menu can not be found or if the partReference does not contain a
	 *             menu.
	 * @deprecated Use {@link #viewMenu()}.menu(label, true, index) instead.
	 */
	@Deprecated
	public SWTBotViewMenu menu(String label, int index) throws WidgetNotFoundException {
		try {
			List<SWTBotViewMenu> menuItems = menuFinder.findMenus(partReference, withMnemonic(label), true);
			if ((menuItems == null) || (menuItems.size() < 1)) {
				CommandFinder finder = new CommandFinder();
				List<SWTBotCommand> command = finder.findCommand(equalTo(label));
				return command.get(index);
			}
			return menuItems.get(index);
		} catch (Exception e) {
			throw new WidgetNotFoundException("Could not find view menu with label " + label + " at index " + index, e); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	@Override
	public void show() {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				try {
					partReference.getPage().activate(partReference.getPart(true));
					partReference.getPage().showView(partReference.getId(), partReference.getSecondaryId(), IWorkbenchPage.VIEW_ACTIVATE);
				} catch (PartInitException e) {
					throw new RuntimeException("Could not show partReference - " + partReference.getPartName(), e); //$NON-NLS-1$
				}
			}
		});
	}
}
