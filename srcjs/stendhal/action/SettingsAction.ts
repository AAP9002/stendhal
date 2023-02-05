/***************************************************************************
 *                       Copyright © 2023 - Stendhal                       *
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Affero General Public License as        *
 *   published by the Free Software Foundation; either version 3 of the    *
 *   License, or (at your option) any later version.                       *
 *                                                                         *
 ***************************************************************************/

declare var stendhal: any;

import { Action } from "./Action";

import { ui } from "../ui/UI";

import { SettingsDialog } from "../ui/dialog/SettingsDialog";


export class SettingsAction extends Action {
	readonly minParams = 0;
	readonly maxParams = 0;

	execute(_type: string, _params: string[], _remainder: string): void {
		const wstate = stendhal.config.dialogstates["settings"];
		const offset = stendhal.ui.getPageOffset();

		const content = new SettingsDialog();
		const dialog = ui.createSingletonFloatingWindow(
			"Settings", content,
			wstate.x - offset.x, wstate.y - offset.y);
		content.setFrame(dialog);
	}
}
