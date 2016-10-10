/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.ebuilder;

import org.jlab.detector.base.DetectorType;
import org.jlab.geom.prim.Point3D;

/**
 *
 * @author jnewt018
 */
public interface TrackIntersect {
    Point3D getIntersection(DetectorParticle p);
    DetectorType getdetectortype();


}
