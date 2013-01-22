package org.bitbucket.shemnon.jfx.sample.fullyexpressed;

import javafx.scene.input.RotateEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.shape.Arc;

public class TheController {
    
    public void onRotate(RotateEvent re) {
        Arc a = (Arc)re.getSource();
        double delta = re.getAngle();
        a.setStartAngle(a.getStartAngle() + delta);
    }
    
    public void onRotateFinished(RotateEvent re) {
    }
    
    public void onRotateStarted(RotateEvent re) {
    }

    public void onSwipeDown(SwipeEvent se) {
        System.out.println(se);
    }

    public void onSwipeLeft(SwipeEvent se) {
        System.out.println(se);
    }

    public void onSwipeRight(SwipeEvent se) {
        System.out.println(se);
    }

    public void onSwipeUp(SwipeEvent se) {
        System.out.println(se);
    }

    public void onTouchMoved(TouchEvent te) {
        System.out.println(te);
    }

    public void onTouchPressed(TouchEvent te) {
        System.out.println(te);
    }

    public void onTouchReleased(TouchEvent te) {
        System.out.println(te);
    }

    public void onTouchStationary(TouchEvent te) {
        System.out.println(te);
    }

    public void onZoom(ZoomEvent ze) {
        Arc a = (Arc)ze.getSource();
        double delta = ze.getZoomFactor();
        double scale = a.getScaleX() * delta;
        a.setScaleX(scale);
        a.setScaleY(scale);
    }

    public void onZoomFinished(ZoomEvent ze) {
    }

    public void onZoomStarted(ZoomEvent ze) {
    }


}