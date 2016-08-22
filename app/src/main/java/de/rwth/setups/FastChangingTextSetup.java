package de.rwth.setups;

import com.droidar2.gl.GL1Renderer;
import com.droidar2.gl.GLFactory;
import com.droidar2.gl.GLText;
import com.droidar2.gl.scenegraph.MeshComponent;
import com.droidar2.gui.GuiSetup;

import java.util.HashMap;

import com.droidar2.system.DefaultARSetup;
import com.droidar2.worldData.Obj;
import com.droidar2.worldData.World;
import android.app.Activity;

import com.droidar2.commands.Command;

public class FastChangingTextSetup extends DefaultARSetup {

	HashMap<String, MeshComponent> textMap = new HashMap<String, MeshComponent>();
	private GLText text;

	@Override
	public void addObjectsTo(GL1Renderer renderer, World world,
			GLFactory objectFactory) {

		text = new GLText("11223344swrvgweln@@@@", myTargetActivity, textMap,
				getCamera());

		Obj o = new Obj();
		o.setComp(text);
		world.add(o);
	}

	@Override
	public void _e2_addElementsToGuiSetup(GuiSetup guiSetup, Activity activity) {
		super._e2_addElementsToGuiSetup(guiSetup, activity);
		guiSetup.addSearchbarToView(guiSetup.getBottomView(), new Command() {

			@Override
			public boolean execute() {
				return false;
			}

			@Override
			public boolean execute(Object transfairObject) {
				if (transfairObject instanceof String) {
					String s = (String) transfairObject;
					if (text != null)
						text.changeTextTo(s);
				}
				return true;
			}
		}, "Enter text");
	}

}
