package io.github.compilerstuck.Visual;

import io.github.compilerstuck.Control.ArrayController;
import io.github.compilerstuck.Control.MainController;
import io.github.compilerstuck.Sound.Sound;
import io.github.compilerstuck.Visual.Gradient.ColorGradient;
import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.floor;
import static java.lang.Math.min;

public class Sphere extends Visualization {

    int radius;
    float squareRoot;
    static float aa = 0;

    public Sphere(ArrayController arrayController, ColorGradient colorGradient, Sound sound) {
        super(arrayController, colorGradient, sound);
        name = "3D - Sphere";
    }

    @Override
    public void update() {
        super.update();
        
        proc.lights();
        
        if (Math.pow(floor(Math.pow(arrayController.getLength(), 1 / 2.)), 2.) != arrayController.getLength()) {

//            code to update for non perfect squares
            int nextN = (int) (floor(Math.pow(arrayController.getLength(), 1 / 2.) + 0.1)); //next lower

            MainController.updateArraySize(nextN * nextN); // Update arraySize
        }

        squareRoot = (int) Math.pow(arrayController.getLength(), 1 / 2.);

        aa -= PApplet.PI / (10 * proc.frameRate);

        float m = 0;
        float n = 0;

        ArrayList<Color> colors = new ArrayList<>();
        //ArrayList<Float> sizes = new ArrayList<>();
        ArrayList<Float> xCords = new ArrayList<>();
        ArrayList<Float> yCords = new ArrayList<>();
        ArrayList<Float> zCords = new ArrayList<>();

        for (int i = 0; i < arrayController.getLength(); i++) {


            if (arrayController.getMarker(arrayController.get(i)) == Marker.SET) {
                sound.playSound(arrayController.get(i));
            }


            arrayController.setMarker(arrayController.get(i), Marker.NORMAL);


            float barHeight = (((float) 100000 / arrayController.getLength() * (arrayController.getLength() - 2 * Math.min(Math.min(Math.abs(i - arrayController.get(i)), Math.abs(i - arrayController.getLength() - arrayController.get(i))), Math.abs(i + arrayController.getLength() - arrayController.get(i))))));

            radius = (int) PApplet.map(barHeight, 0, 100000, 0, (int) (min(screenHeight, screenWidth) / 2.3));

            float u = (float) ((m / squareRoot) * 2 * Math.PI);
            float v = (float) ((n / squareRoot) * Math.PI);

            float zSphere = (float) (Math.cos(u) * Math.sin(v));
            float xSphere = (float) (Math.sin(u) * Math.sin(v));
            float ySphere = (float) Math.cos(v);

            float zMapped = PApplet.map(zSphere, -1, 1, -radius, radius); //to front
            float yMapped = PApplet.map(ySphere, -1, 1, -radius, radius); //to side
            float xMapped = PApplet.map(xSphere, -1, 1, -radius, radius); //height


            //rotate z and x
            float zb = (float) (Math.sin(aa) * xMapped + Math.cos(aa) * zMapped);
            float x = (float) ((float) Math.cos(aa) * xMapped - Math.sin(aa) * zMapped);


            //change perspective
            float y = (float) (Math.cos(aa) * yMapped - Math.sin(aa) * zb);
            //float y = (float) (screenHeight * 0.5 - 20 + Math.cos(-10) * yMapped - Math.sin(-10) * zb);

            //calc sircle distance from viewpoint
            float z = (float) (Math.sin(aa) * yMapped + Math.cos(aa) * zb);
            //float z = (float) (Math.sin(-10) * yMapped + Math.cos(-10) * zb);

            //calc circle sizes
            //float size = PApplet.map(z, -radius, radius, 20, 35);

            //size = PApplet.map(barHeight, 0, arrayController.getLength(), 0, size);


            //float x = xMapped;
            //float y = yMapped;
            //float z = zb;

            //sort for size
            Color color = colorGradient.getMarkerColor(arrayController.get(i), arrayController.getMarker(i));

            zCords.add(z);
            colors.add(color);
            xCords.add(x);
            yCords.add(y);

            if (m == squareRoot - 1) {
                if (n == squareRoot - 1) {
                    n = 0;
                } else {
                    n++;
                }
                m = 0;
            } else {
                m++;
            }
        }

        for (int i = 0; i < arrayController.getLength(); i++) {
            if (colors.size() != arrayController.getLength()) return;
            Color color = colors.get(i);

            proc.noStroke();
            proc.fill(color.getRGB(), (float) (255.));


            proc.pushMatrix();

            //set screen center
            proc.translate((float) screenWidth / 2, (float) screenHeight / 2, -(int) (min(screenHeight, screenWidth) / 10));

            //set circle position
            proc.translate(xCords.get(i), yCords.get(i), zCords.get(i));
            proc.circle(0, 0, 3);

            proc.popMatrix();
        }
    }

}
