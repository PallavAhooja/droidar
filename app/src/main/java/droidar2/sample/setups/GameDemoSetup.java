package droidar2.sample.setups;

import com.droidar2.gamelogic.ActionThrowFireball;
import com.droidar2.gamelogic.GameParticipant;
import com.droidar2.gamelogic.Stat;
import com.droidar2.gl.GL1Renderer;
import com.droidar2.gl.GLFactory;
import com.droidar2.gui.GuiSetup;
import com.droidar2.system.DefaultARSetup;
import com.droidar2.worldData.SystemUpdater;
import com.droidar2.worldData.World;
import android.app.Activity;

import droidar2.sample.R;

public class GameDemoSetup extends DefaultARSetup {
	private GameParticipant p;
	private ActionThrowFireball e;

	public GameDemoSetup() {
		p = new GameParticipant("Player", "Karlo", R.drawable.hippopotamus64);
		p.addStat(new Stat(Stat.INTELLIGENCE, R.drawable.icon, 2));
		e = new ActionThrowFireball("Fireball");
		p.addAction(e);
	}

	@Override
	public void _d_addElementsToUpdateThread(SystemUpdater updater) {
		super._d_addElementsToUpdateThread(updater);
		updater.addObjectToUpdateCycle(p);
	}

	@Override
	public void addObjectsTo(GL1Renderer renderer, World world,
			GLFactory objectFactory) {

	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		super._e2_addElementsToGuiSetup(guiSetup, activity);
		guiSetup.addViewToTop(e.getNewDefaultView(getActivity()));
	}
}
