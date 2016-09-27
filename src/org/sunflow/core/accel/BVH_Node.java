package org.sunflow.core.accel;

import org.sunflow.core.IntersectionState;
import org.sunflow.core.PrimitiveList;
import org.sunflow.math.BoundingBox;
import org.sunflow.core.Ray;
import org.sunflow.math.Point3;

import java.util.ArrayList;

public class BVH_Node {
    private BoundingBox bounds;
    private BVH_Node leftChild, rightChild;
    private ArrayList<Integer> myPrimitives;

    private int level;

    public BVH_Node(int level){
        this.level = level;
    }

    BoundingBox RecursiveBuild(ArrayList<Integer> myPrimitives, BoundingBox[] primBounds, boolean output){
        this.myPrimitives = myPrimitives;
        bounds = new BoundingBox();

        if(myPrimitives.size() <= 4){
            bounds = new BoundingBox();
            for(Integer prim : myPrimitives){
                bounds.include(primBounds[prim]);
            }
            return bounds;
        }
        else{
            //Calculate medium point
             float valueToSplitOn = 0;
            for(Integer prim : myPrimitives){
                if(level % 3 == 0)
                    valueToSplitOn += primBounds[prim].getCenter().x;
                if(level % 3 == 1)
                    valueToSplitOn += primBounds[prim].getCenter().y;
                if(level % 3 == 2)
                    valueToSplitOn += primBounds[prim].getCenter().z;
            }

            //Now divide list into two sublists
            valueToSplitOn /= myPrimitives.size();
            ArrayList<Integer> leftChildList = new ArrayList<Integer>();
            ArrayList<Integer> rightChildList = new ArrayList<Integer>();
            for(Integer prim : myPrimitives){
                if(level % 3 == 0) {
                    if (primBounds[prim].getCenter().x <= valueToSplitOn)
                        leftChildList.add(prim);
                    else
                        rightChildList.add(prim);
                }
                if(level % 3 == 1) {
                    if (primBounds[prim].getCenter().y <= valueToSplitOn)
                        leftChildList.add(prim);
                    else
                        rightChildList.add(prim);
                }
                if(level % 3 == 2) {
                    if (primBounds[prim].getCenter().z <= valueToSplitOn)
                        leftChildList.add(prim);
                    else
                        rightChildList.add(prim);
                }
            }
/*            if(output)
                System.out.println("Level: " + level + ". LSize: " + leftChildList.size() + " vs " + rightChildList.size());*/
            if(leftChildList.size() == 0)
            {
                int diff = rightChildList.size()/2;
                for(int i = 0; i < diff; i++)
                    leftChildList.add(rightChildList.remove(0));
            }
            if(rightChildList.size() == 0)
            {
                int diff = leftChildList.size()/2;
                for(int i = 0; i < diff; i++)
                    rightChildList.add(leftChildList.remove(0));
            }
            // Give those lists to children nodes and
            leftChild = new BVH_Node(level + 1);
            bounds.include(leftChild.RecursiveBuild(leftChildList, primBounds, output));
            rightChild = new BVH_Node(level + 1);
            bounds.include(rightChild.RecursiveBuild(rightChildList, primBounds, false));

            return bounds;
        }

    }

    void Intersect(Ray r, IntersectionState state, PrimitiveList primitives){

        if(myPrimitives.size() <= 4) {
            for (Integer prim : myPrimitives)
                primitives.intersectPrimitive(r, prim, state);
        }
        else if (IntersectBounds(r)){
            leftChild.Intersect(r, state, primitives);
            rightChild.Intersect(r, state, primitives);
        }
    }



    private boolean IntersectBounds(Ray r){
        float intervalMin = r.getMin();
        float intervalMax = r.getMax();
        float orgX = r.ox;
        float dirX = r.dx, invDirX = 1 / dirX;
        float t1, t2;
        t1 = (bounds.getMinimum().x - orgX) * invDirX;
        t2 = (bounds.getMaximum().x - orgX) * invDirX;
        if (invDirX > 0) {
            if (t1 > intervalMin)
                intervalMin = t1;
            if (t2 < intervalMax)
                intervalMax = t2;
        } else {
            if (t2 > intervalMin)
                intervalMin = t2;
            if (t1 < intervalMax)
                intervalMax = t1;
        }
        if (intervalMin > intervalMax)
            return false;
        float orgY = r.oy;
        float dirY = r.dy, invDirY = 1 / dirY;
        t1 = (bounds.getMinimum().y - orgY) * invDirY;
        t2 = (bounds.getMaximum().y - orgY) * invDirY;
        if (invDirY > 0) {
            if (t1 > intervalMin)
                intervalMin = t1;
            if (t2 < intervalMax)
                intervalMax = t2;
        } else {
            if (t2 > intervalMin)
                intervalMin = t2;
            if (t1 < intervalMax)
                intervalMax = t1;
        }
        if (intervalMin > intervalMax)
            return false;
        float orgZ = r.oz;
        float dirZ = r.dz, invDirZ = 1 / dirZ;
        t1 = (bounds.getMinimum().z - orgZ) * invDirZ;
        t2 = (bounds.getMaximum().z - orgZ) * invDirZ;
        if (invDirZ > 0) {
            if (t1 > intervalMin)
                intervalMin = t1;
            if (t2 < intervalMax)
                intervalMax = t2;
        } else {
            if (t2 > intervalMin)
                intervalMin = t2;
            if (t1 < intervalMax)
                intervalMax = t1;
        }
        if (intervalMin > intervalMax)
            return false;
        // box is hit at [intervalMin, intervalMax]
/*        orgX += intervalMin * dirX;
        orgY += intervalMin * dirY;
        orgZ += intervalMin * dirZ;*/
        return true;

        //TODO mark ray with point here
    }
}
