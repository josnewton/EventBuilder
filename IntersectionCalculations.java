/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.ebuilder;

import org.jlab.detector.base.DetectorType;
import org.jlab.geom.prim.Line3D;
import org.jlab.geom.prim.Plane3D;
import org.jlab.geom.prim.Point3D;
import org.jlab.geom.prim.Triangle3D;
import org.jlab.geom.prim.Vector3D;







/**
 *
 * @author jnewt018
 */
public class IntersectionCalculations {
    
}

   class FTOFIntersect implements TrackIntersect {


            public Point3D getIntersection(DetectorParticle p) {
                
                Point3D intersect = new Point3D(0.0,0.0,0.0);
                Point3D crosspoint = new Point3D(p.getCross().x(),p.getCross().y(),p.getCross().z());
                Vector3D crossvector = new Vector3D(p.getCrossDir().x(),p.getCrossDir().y(),p.getCrossDir().z());
                Line3D crossline = new Line3D(crosspoint,crossvector);
                
                if(p.hasHit(DetectorType.FTOF, 2)==true){
                Point3D point1 =  new Point3D(134.66,0.0,674.36533); 
                Point3D point2 = new Point3D(206.11077,0.0,641.04727); 
                Point3D point3 = new Point3D(305.04260,5.0,594.91461);

                Triangle3D tri = new Triangle3D(point1,point2,point3);
                Plane3D plane = tri.plane();
                Point3D thepoint = new Point3D();
                plane.intersectionSegment(crossline,intersect);
                }
                
                if(p.hasHit(DetectorType.FTOF,1)==true){
                Point3D point1 =  new Point3D(107.05817,0.0,705.68237);
                Point3D point2 = new Point3D(175.89225,0.0,673.58452); 
                Point3D point3 = new Point3D(244.72633,5.0,641.48665); 

                Triangle3D tri = new Triangle3D(point1,point2,point3);
                Plane3D plane = tri.plane();
                Point3D thepoint = new Point3D();
                plane.intersectionSegment(crossline,intersect);
                }
                
                

                
                
                    
               return intersect;
            }
            
            public DetectorType getdetectortype(){
                
                return DetectorType.FTOF;
            }

        }
