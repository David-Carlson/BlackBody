package org.sunflow.core.shader;

import java.lang.String;
import org.sunflow.SunflowAPI;
import org.sunflow.core.ParameterList;
import org.sunflow.core.Ray;
import org.sunflow.core.Shader;
import org.sunflow.core.ShadingState;
import org.sunflow.image.Color;
import org.sunflow.image.RGBSpace;
import org.sunflow.image.SpectralCurve;
import org.sunflow.image.XYZColor;
import org.sunflow.math.OrthoNormalBasis;
import org.sunflow.math.Vector3;

public class DiffuseHeatShader extends SpectralCurve implements Shader  {
    private Color diff;
    private double temp;
    private Color heatColor;
    private Color absorption;
    private Color IAbsorbed;

    public DiffuseHeatShader() {
        diff = Color.WHITE;
    }

    public boolean update(ParameterList pl, SunflowAPI api) {
        diff = pl.getColor("heatdiff", diff);
        temp = (double)pl.getFloat("temp", 1000f);
        heatColor = getHeatColor();
        absorption = Color.white().sub(diff);
        //System.out.println("Abs: " + absorption.toString());

        //System.out.println("HeatShader. temp: " + temp);
        float scale = (float)temp/1000*10;
        heatColor = heatColor.normalize().mul(scale);
        //heatColor = heatColor.normalize().mul(10);

        IAbsorbed = heatColor.mul(absorption);
        System.out.println("Diffuse Iabs: " + IAbsorbed);
        //System.out.println("IAbsorbed: " + IAbsorbed.toString());
        //System.out.pri//ntln("heatColor: " + heatColor.toString());
        return true;
    }

    public Color ScaleLight(Color light, float scalePoint){
        float intensity = light.getLuminance();
        float disToScale = intensity - scalePoint;
        float scale = 1;
        //if(intensity > 0)
        return null;
    }

    public Color getDiffuse(ShadingState state) {
        return diff;
    }

    public Color getRadiance(ShadingState state) {
        // make sure we are on the right side of the material
        state.faceforward();
        // setup lighting
        state.initLightSamples();
        state.initCausticSamples();

        // I *(1-f)
        //Color I = heatColor.mul(Color.WHITE.sub(diff));
        Color lr = state.diffuse(getDiffuse(state));
        if(state.getDiffuseDepth() >= 0){
            //lr.add(I);
            //System.out.println("added light: " + I.toString());
            return lr.add(IAbsorbed);
        }

        return  lr;
    }
    public XYZColor getXYZ(){
        XYZColor XYZ = toXYZ();
        System.out.println("XYZ: " + XYZ.toString());
        float xyzLen = XYZ.getX() + XYZ.getY() + XYZ.getZ();
        System.out.println("XYZ Len: " + xyzLen);
        //XYZ.normalize();
        //XYZ.mul(10);
        return XYZ;
    }
    public Color getHeatColor(){
        XYZColor XYZ = getXYZ();
        return RGBSpace.CIE.convertXYZtoRGB(XYZ);
    }

    public void scatterPhoton(ShadingState state, Color power) {
        Color diffuse;
        // make sure we are on the right side of the material
        if (Vector3.dot(state.getNormal(), state.getRay().getDirection()) > 0.0) {
            state.getNormal().negate();
            state.getGeoNormal().negate();
        }
        diffuse = getDiffuse(state);
        state.storePhoton(state.getRay().getDirection(), power, diffuse);
        float avg = diffuse.getAverage();
        double rnd = state.getRandom(0, 0, 1);
        if (rnd < avg) {
            // photon is scattered
            power.mul(diffuse).mul(1.0f / avg);
            OrthoNormalBasis onb = state.getBasis();
            double u = 2 * Math.PI * rnd / avg;
            double v = state.getRandom(0, 1, 1);
            float s = (float) Math.sqrt(v);
            float s1 = (float) Math.sqrt(1.0 - v);
            Vector3 w = new Vector3((float) Math.cos(u) * s, (float) Math.sin(u) * s, s1);
            w = onb.transform(w, new Vector3());
            state.traceDiffusePhoton(new Ray(state.getPoint(), w), power);
        }
    }

    public float sample(float lamda){
        double wavelength = lamda * 1e-9;
        double c =  299792458d;
        double h = 6.6260693e-34;
        double k = 1.38064852e-23;
        // 2hc^2 *      1
        // lambda^5 *(e^(hc/lkT)
        double num = 2*h*Math.pow(c,2);
        double denom = Math.pow(wavelength,5) * (Math.exp(h*c/(wavelength*k*temp))-1);
        return (float)(num/denom);
    }
    public float sample2(float lambda){
        if(temp < 140)
            return 0.0001f;

        double wavelength = lambda * 1e-9;
        return (float) ((3.74183e-16 * Math.pow(wavelength, -5.0)) / (Math.exp(1.4388e-2 / (wavelength * temp)) - 1.0));
    }

}