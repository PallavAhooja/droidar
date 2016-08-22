package com.droidar2.actions.algos;

import com.droidar2.util.Log;

public abstract class Algo {

	public float[] execute(float[] values) {
		Log.e("algo class error", "execute(one param) not catched");
		return null;
	}

	public boolean execute(float[] targetValues, float[] newValues,
			float bufferSize) {
		Log.e("algo class error", "execute(3 params) not catched");
		return false;
	}

}
