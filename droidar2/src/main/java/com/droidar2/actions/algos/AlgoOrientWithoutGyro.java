package com.droidar2.actions.algos;

import android.view.Surface;

import com.droidar2.util.Log;

/**
 * Created by vishwasrao on 18/09/16.
 */
public class AlgoOrientWithoutGyro extends Algo {

    public AlgoOrientWithoutGyro() {
    }

    double[][] x = new double[3][5];
    double[][] y = new double[3][5];

    double cutOffFreq = 0.5;  //# hz
    double T = 0.1; // # Sample time interval in sec;
    double omegaC = 2 * Math.PI * cutOffFreq; // # cutoff frequency in radians per sec

    double omegaST = 2 * Math.tan((omegaC * T) / 2.0);
    double factorP1 = 4 + 1.53 * omegaST + Math.pow(omegaST, 2);
    double factorP2 = 2 * Math.pow(omegaST, 2) - 8;
    double factorP3 = 4 + Math.pow(omegaST, 2) - 1.53 * omegaST;
    double factorP4 = 4 + Math.pow(omegaST, 2) + 3.69 * omegaST;
    double factorP5 = 4 + Math.pow(omegaST, 2) - 3.69 * omegaST;
    double multiplier1 = factorP2 * (factorP1 + factorP4);
    double multiplier2 = factorP1 * factorP5 + factorP4 * factorP3 + Math.pow(factorP2, 2);
    double multiplier3 = factorP2 * (factorP3 + factorP5);
    double multiplier4 = factorP3 * factorP5;


    @Override
    public float[] execute(float[] values) {

        boolean allInitialized = true;
        for (int i = 0; i < 3; i++) {


            for (int j = 0; j < 5; j++) {
                if (x[i][j] == (double) 0) {
                    x[i][j] = values[i];
                    y[i][j] = values[i];
                    allInitialized = false;
                    break;
                }
            }
            if (allInitialized) {
                for (int j = 4; j > 0; j--) {
                    x[i][j] = x[i][j - 1];
                    y[i][j] = y[i][j - 1];

                }
                x[i][0] = values[i];
                double outputNumerator = Math.pow(omegaST, 4) *
                        (
                                x[i][0] + 4 * x[i][1] + 6 * x[i][2] + 4 * x[i][3] + x[i][4]) - (
                        multiplier1 * y[i][1] +
                                multiplier2 * y[i][2] +
                                multiplier3 * y[i][3] +
                                multiplier4 * y[i][4]
                );
                double outputDeno = factorP1 * factorP4;

                double newY = (outputNumerator) / outputDeno;
                y[i][0] = newY;
            }

        }

        float[] returnValue = {(float) y[0][0], (float) y[1][0], (float) y[2][0]};
        return returnValue;
    }

}
