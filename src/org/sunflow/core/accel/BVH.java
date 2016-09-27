package org.sunflow.core.accel;

import org.sunflow.core.AccelerationStructure;
import org.sunflow.core.IntersectionState;
import org.sunflow.core.PrimitiveList;
import org.sunflow.core.Ray;
import org.sunflow.math.BoundingBox;
import org.sunflow.math.Point3;

import java.util.ArrayList;

/**
 * Created by DoctorSalt on 9/24/2015.
 */
public class BVH implements AccelerationStructure {

    private PrimitiveList primitives;
    private BVH_Node rootNode;

    /**
     * Construct an acceleration structure for the specified primitive list.
     *
     * @param primitives
     */
    public void build(PrimitiveList primitives)
    {
       /* int mb = 1024*1024;
        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();

        System.out.println("##### Heap utilization statistics [MB] #####");

        //Print used memory
        System.out.println("Used Memory:"
                + (runtime.totalMemory() - runtime.freeMemory()) / mb);

        //Print free memory
        System.out.println("Free Memory:"
                + runtime.freeMemory() / mb);

        //Print total available memory
        System.out.println("Total Memory:" + runtime.totalMemory() / mb);

        //Print Maximum available memory
        System.out.println("Max Memory:" + runtime.maxMemory() / mb);*/


        //Create int list for all indices
        //create/pass to root
        this.primitives = primitives;
        BoundingBox[] Bboxes = new BoundingBox[primitives.getNumPrimitives()];
        ArrayList<Integer> myPrimitives = new ArrayList<Integer>();


        for(int idx = 0; idx < primitives.getNumPrimitives(); idx++){
            BoundingBox bbox = new BoundingBox();
            bbox.include(PrimitiveMin(idx));
            bbox.include(PrimitiveMax(idx));
            Bboxes[idx] = bbox;

            myPrimitives.add(idx);
        }

        rootNode = new BVH_Node(0);
        rootNode.RecursiveBuild(myPrimitives, Bboxes, true);

    }

    public void intersect(Ray r, IntersectionState state) {
        //System.out.println("In intersect");
        rootNode.Intersect(r, state, primitives);
    }

    private Point3 PrimitiveMin(int idx){
        return new Point3(
                primitives.getPrimitiveBound(idx, 0),
                primitives.getPrimitiveBound(idx, 2),
                primitives.getPrimitiveBound(idx, 4));
    }
    private Point3 PrimitiveMax(int idx){
        return new Point3(
                primitives.getPrimitiveBound(idx, 1),
                primitives.getPrimitiveBound(idx, 3),
                primitives.getPrimitiveBound(idx, 5));
    }


}
