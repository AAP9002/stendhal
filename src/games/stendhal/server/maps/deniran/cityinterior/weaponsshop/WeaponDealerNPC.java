/***************************************************************************
 *                      (C) Copyright 2019 - Stendhal                      *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.maps.deniran.cityinterior.weaponsshop;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;

public class WeaponDealerNPC implements ZoneConfigurator  {


	@Override
	public void configureZone(StendhalRPZone zone, Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("D J Smith") {

		@Override
		public void createDialog() {
			addGreeting("Hello, and welcome to the deniran weapon shop.");
			addJob("I am the local weapons dealer.");
			addOffer("I need nothing at this time, but come back later I'm sure I'll need something.");
			addGoodbye();
		}

		@Override
		protected void createPath() {
			final List<Node> nodes = new LinkedList<Node>();
			nodes.add(new Node(29, 5));
			nodes.add(new Node(11, 5));
			setPath(new FixedPath(nodes, true));
		}

		};
		npc.setPosition(11, 5);
		npc.setEntityClass("wellroundedguynpc");
		npc.setDescription("You see D J Smith, the weapon dealer.");
		zone.add(npc);
	}
}