/***************************************************************************
 *                 Copyright © 2022-2024 - Faiumoni e. V.                  *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package org.stendhalgame.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toolbar;


public class Menu {

	private static Menu instance;

	private final Context ctx;
	private final Toolbar nav;

	private Button btn_connect;
	private Button btn_title;
	private Button btn_reload;


	public static Menu get() {
		return instance;
	}

	public Menu(final Context ctx) {
		instance = this;
		this.ctx = ctx;

		nav = (Toolbar) ((Activity) ctx).findViewById(R.id.menu_main);

		nav.setTag(nav.getVisibility());
		nav.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (nav.getVisibility() == View.VISIBLE) {
					updateButtons();
				}
			}
		});

		initButtonHandlers();

		DebugLog.debug("menu initialized");
	}

	public Toolbar getInternal() {
		return nav;
	}

	public void setVisibility(final int vType) {
		nav.setVisibility(vType);
	}

	public void toggleVisibility() {
		if (nav.getVisibility() == View.GONE) {
			nav.setVisibility(View.VISIBLE);
		} else {
			nav.setVisibility(View.GONE);
		}
	}

	public void show() {
		nav.setVisibility(View.VISIBLE);
	}

	public void hide() {
		nav.setVisibility(View.GONE);
	}

	public void updateButtons() {
		final PageId page = ClientView.getCurrentPageId();

		btn_connect.setVisibility(View.GONE);
		btn_title.setVisibility(View.GONE);
		btn_reload.setVisibility(View.GONE);

		if (page == PageId.TITLE) {
			btn_connect.setVisibility(View.VISIBLE);
		} else {
			btn_title.setVisibility(View.VISIBLE);
			btn_reload.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Sets actions when buttons are pressed.
	 */
	private void initButtonHandlers() {
		final MainActivity activity = (MainActivity) ctx;

		btn_connect = (Button) activity.findViewById(R.id.btn_connect);
		btn_connect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				activity.loadLogin();
			}
		});

		btn_title = (Button) activity.findViewById(R.id.btn_title);
		btn_title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				nav.setVisibility(View.GONE);

				Notifier.showPrompt("Return to title screen?",
					new Notifier.Action() {
						@Override
						protected void onCall() {
							ClientView.get().loadTitleScreen();
							updateButtons();
						}
					},
					new Notifier.Action() {
						@Override
						protected void onCall() {/* do nothing */}
					}
				);
			}
		});

		btn_reload = (Button) activity.findViewById(R.id.btn_reload);
		btn_reload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				nav.setVisibility(View.GONE);

				Notifier.showPrompt("Reload page?",
					new Notifier.Action() {
						@Override
						protected void onCall() {
							ClientView.get().reload();
						}
					},
					new Notifier.Action() {
						@Override
						protected void onCall() {
							// do nothing
						}
				});
			}
		});

		final Button btn_settings = (Button) activity.findViewById(R.id.btn_settings);
		btn_settings.setOnClickListener(new ClickListener() {
			@Override
			public void onClick(final View v) {
				super.onClick(v);

				((MainActivity) ctx).showSettings();
			}
		});

		final Button btn_about = (Button) activity.findViewById(R.id.btn_about);
		btn_about.setOnClickListener(new ClickListener() {
			@Override
			public void onClick(final View v) {
				super.onClick(v);

				// TODO: create proper about activity

				final ClientView client = ClientView.get();
				final WebClientInfo wci = WebClientInfo.get();
				final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.get());
				final String buildType = AppInfo.getBuildType();
				String msg = "Android client version: " + AppInfo.getBuildVersion();
				if (!"prod".equals(buildType)) {
					msg += "\nAndroid client build: " + AppInfo.getBuildType();
				}
				msg += "\nWeb client version: " + wci.getVersion()
						+ "\nWeb client build: " + wci.getBuild();
				if (client.debugEnabled()) {
					msg += "\nClient type: " + client.getSelectedClient()
							+ "\nServer type: " + client.getSelectedServer()
							+ "\nLogs directory: " + DebugLog.getLogsDir();
				}
				msg += "\n\nTitle Music:"
						+ "\n\n- Treasure Hunter"
						+ "\n  by Tad Miller (TAD)"
						+ "\n  - opengameart.org/node/127124"
						+ "\n\n- Woodland Fantasy"
						+ "\n  by Matthew Pablo"
						+ "\n  - matthewpablo.com"
						+ "\n  - opengameart.org/node/31929"
						+ "\n\n- Land of Fearless"
						+ "\n  by Alexandr Zhelanov"
						+ "\n  - soundcloud.com/alexandr-zhelanov"
						+ "\n  - opengameart.org/node/79931"
						+ "\n\n- Rejoicing"
						+ "\n  by RandomMind"
						+ "\n  - opengameart.org/node/100213"
						+ "\n\n- The Old Tower Inn"
						+ "\n  by RandomMind"
						+ "\n  - opengameart.org/node/87510";
				builder.setMessage(msg);

				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, final int id) {
						dialog.cancel();
					}
				});

				builder.create().show();
			}
		});

		final Button btn_quit = (Button) activity.findViewById(R.id.btn_quit);
		btn_quit.setOnClickListener(new ClickListener() {
			@Override
			public void onClick(final View v) {
				super.onClick(v);
				activity.onRequestQuit();
			}
		});

		updateButtons();
	}

	private class ClickListener implements View.OnClickListener {
		@Override
		public void onClick(final View v) {
			if (!ClientView.onTitleScreen()) {
				nav.setVisibility(View.GONE);
			}
		}
	}
}
