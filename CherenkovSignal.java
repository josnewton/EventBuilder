/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.detector;

import org.jlab.clas.physics.Vector3;
import org.jlab.detector.base.DetectorDescriptor;
import org.jlab.geom.prim.Line3D;
import org.jlab.geom.prim.Path3D;
import org.jlab.geom.prim.Point3D;
import org.jlab.clas.detector.DetectorEvent;
import org.jlab.clas.detector.DetectorParticle;
import org.jlab.detector.base.DetectorType;

/**
 *
 * @author jnewton
 */
public class CherenkovSignal {
    
    private String              signaltype;
    private Double              Theta = 0.0;
    private Double              Phi = 0.0;
    private Double             Time = 0.0;
    private int           Nphe = 0;
    private int              association    = -1;
    
    public CherenkovSignal(){
        
    }
    
    public void   setTime(double time){ this.Time = time;}
    public void   setTheta(double theta) {this.Theta = theta;}
    public void   setPhi(double phi) {this.Phi = phi;}
    public void   setNphe(int nphe) {this.Nphe = nphe;}
    public void   setSignalType(String signal){this.signaltype = signal;}
    public String getSignalType(){return this.signaltype;}
    public double getTime(){ return this.Time;}
    public int getNphe(){ return this.Nphe; }
    public double getTheta(){ return this.Theta;}
    public double getPhi(){return this.Phi;}
    public int getAssociation(){ return this.association;}
    public void setAssociation(int asc){ this.association = asc;}

    

}
