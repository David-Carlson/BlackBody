package org.sunflow.core.camera;

import org.sunflow.SunflowAPI;
import org.sunflow.core.CameraLens;
import org.sunflow.core.ParameterList;
import org.sunflow.core.Ray;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Random;


public class ThinLens2 implements CameraLens {

    // assume the unit of distance/length is 'inch'

    private float focalLength;   // focal length (f)
    //where lens should be focused in object space
    private float Zi; // focus distance (zi)
    private float aFstop;        // aperture F-stop = f/aperture
    private float lensRadius;    // len's radius
    private float lensRadiusSquared;
    private float aspectRatio;   // aspect ratio (ratio of image width to height)

    private float filmDiagonal;  // diagonal length of the film
    private float filmWidth, filmHeight;  // width and height (physical size) of the film
    private float filmZ;

    private Random rand;
    // ***** define other variables you may need here *****

    public ThinLens2() {
        focalLength = 1.0f;                     // focal length defaults to 1 inch
        Zi = Float.MAX_VALUE;        // focus distance defaults to infinity
        filmDiagonal = 1.4f;  // diagonal of the film defaults to 1.4 inch (~35mm)
        aspectRatio = 1.333f; // aspect ratio defaults to 1.333
        aFstop = 100;         // aperture f-stop defaults to 100

        rand = new Random();
        filmZ = 1f / (1f / focalLength - 1f / Zi);
    }

    private void update() {
        lensRadius = focalLength / aFstop / 2.0f;  // calculate len's radius
        lensRadiusSquared = lensRadius * lensRadius;
        filmHeight = filmDiagonal / (float) Math.sqrt(1.0 + aspectRatio * aspectRatio);
        filmWidth = filmHeight * aspectRatio;

        // ***** calculate other variables you may need here *****
    }

    @Override
    public boolean update(ParameterList pl, SunflowAPI api) {
        // TODO Auto-generated method stub
        focalLength = pl.getFloat("flength", focalLength);
        Zi = pl.getFloat("fdistance", Zi);
        aspectRatio = pl.getFloat("aspect", aspectRatio);
        aFstop = pl.getFloat("afstop", aFstop);
        update();
        return true;
    }

    boolean Fits(float x, float y, float[][] points, int pointCount, float rSquare) {
        for (int i = 0; i < pointCount; i++) {
            if (((points[i][1] - x) * (points[i][1] - x) + (points[i][2] - y) * (points[i][2] - y)) < rSquare)
                return false;
        }
        return true;
    }

    void parse(String T) {
    }

    ;
    public void build2() {
        Random rand1 = new Random(1); // these create two independent, uniform random variables
        Random rand2 = new Random(2);
        int nrandom_samples = 500;   // this is only used for random sampling
        float spherer = 0.005f;      // sphere radius (for display purpose, no need to modify this variable)

        float pd_radius = 0.05f;     // Poisson disk radius (decrease this value to generate more samples)

        try {
            FileOutputStream fos = new FileOutputStream("poisson.sampling.sc");
            PrintWriter out = new PrintWriter(fos);

            int i;
            for(i=0;i<nrandom_samples;i++) {
                // compute a random sample point inside the disk
                double r = 0.5*Math.sqrt(rand1.nextFloat());
                float theta = rand2.nextFloat();
                float x = 0.5f+(float)(r*Math.cos(2*Math.PI*theta));
                float y = 0.5f+(float)(r*Math.sin(2*Math.PI*theta));

                // output the sample point as a small sphere
                out.println("object {");
                out.println("shader diffuse_gray");
                out.println("type sphere");
                out.println("c "+x+" "+y+" "+spherer);
                out.println("r "+spherer);
                out.println("}");
            }

            out.println("//Number of samples:"+nrandom_samples);
            out.flush();
            out.close();
        }
        catch(Exception e) {
        }

        parse("poisson.setting.sc");
        parse("poisson.sampling.sc");
    }


    public void build() {
        Random rand1 = new Random(1);  // these create two independent, uniform random variables
        Random rand2 = new Random(2);
        int nrandom_samples = 300;    // this is only used for random sampling
        float spherer = 0.005f;         // sphere radius (for display purpose, no need to modify this variable)

        int SIZE = 1000;
        int nextFreeIdx = 0;
        float[][] Samples = new float[SIZE][2];

        int fails = 0;
        int totalFails = 50;
        float pd_radius = 0.05f;     // Poisson disk radius (decrease this value to generate more
        float RSquare = pd_radius * pd_radius;

        try {
            FileOutputStream fos = new FileOutputStream("poisson.sampling.sc");
            PrintWriter out = new PrintWriter(fos);

            int i;
            while (fails < totalFails) {
                double r = 0.5*Math.sqrt(rand1.nextFloat());
                float theta = rand2.nextFloat();
                theta = (float)Math.sqrt(theta);
                float x = 0.5f+(float)(r*Math.cos(2*Math.PI*theta));
                float y = 0.5f+(float)(r*Math.sin(2*Math.PI*theta));

                boolean accept = true;
                for (int p = 0; p < nextFreeIdx; p++) {
                    float x2 = Samples[p][0];
                    float y2 = Samples[p][1];
                    if (((x - x2) * (x - x2) + (y - y2) * (y - y2)) < RSquare)
                        accept = false;
                }
                if (accept) {
                    Samples[nextFreeIdx][0] = x;
                    Samples[nextFreeIdx][1] = y;
                    nextFreeIdx++;
                    fails = 0;
                    // output the sample point as a small sphere
                    out.println("object {");
                    out.println("shader diffuse_gray");
                    out.println("type sphere");
                    out.println("c "+x+" "+y+" "+spherer);
                    out.println("r "+spherer);
                    out.println("}");
                }
                else
                    fails++;
                // output the sample point as a small sphere

            }

            out.println("//Number of samples:" + nrandom_samples);
            out.println("//Accepted:"+nextFreeIdx);
            out.flush();
            out.close();
        } catch (Exception e) {
        }

        parse("poisson.setting.sc");
        parse("poisson.sampling.sc");
    }


    @Override
    public Ray getRay(float x, float y, int imageWidth, int imageHeight,
                      double random1, double random2, double time) {
        // TODO Auto-generated method stub
        float filmX = -filmWidth / 2.0f + ((filmWidth * x) / (imageWidth - 1.0f));
        float filmY = -filmHeight / 2.0f + ((filmHeight * y) / (imageHeight - 1.0f));


        // ***** calculate the correct camera ray below *****
        float lensX, lensY, Xi, Yi;


        // Calculate lensX,Y based on random number and len's radius

        do {
            //Make sure this is in range
            //TODO
            lensX = (rand.nextFloat() * 2 - 1) * lensRadius;
            lensY = (rand.nextFloat() * 2 - 1) * lensRadius;
        } while (Math.pow(lensX, 2) + Math.pow(lensY, 2) > lensRadiusSquared);

        //find Xi Yi Zi using proportions
        Xi = filmX / filmZ * Zi;
        Yi = filmY / filmZ * Zi;
        return new Ray(lensX, lensY, 0, Xi - lensX, Yi - lensY, -Zi);
        //return new Ray(0, 0, 0, filmX, filmY, -1);
    }
}
