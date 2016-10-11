package org.jlab.clas.detector;

import java.util.ArrayList;
import java.util.List;
import org.jlab.clas.physics.Vector3;
import org.jlab.detector.base.DetectorDescriptor;
import org.jlab.geom.prim.Line3D;
import org.jlab.geom.prim.Path3D;
import org.jlab.geom.prim.Point3D;
import org.jlab.clas.detector.DetectorEvent;


/**
 *
 * @author jnewton
 */
public class EventTrigger {
    
 private double zt=0.0;
 private double rftime=0.0;
 private double starttime;
 private List<Integer> UserTriggerIDs = new ArrayList<Integer>();//List of Trigger Particles Inputted By The User
 private DetectorParticle triggerparticle = new DetectorParticle();
    

    
    public EventTrigger(){
        
    }
    
    public void   setzt(double z_t){ this.zt = z_t;}
    public void   setRFTime(float rf){this.rftime = rf;}
    public void   setStartTime(double start){this.starttime = start;}
    public void   setTriggerParticle(DetectorParticle particle){this.triggerparticle=particle;}
    public void   setUserTriggerIDs(List<Integer> user){this.UserTriggerIDs = user;}



    
    public double getZt(){ return this.zt;}
    public double getRFTime(){ return this.rftime; }
    public double getStartTime(){ return this.starttime;}
    public DetectorParticle getTriggerParticle(){return this.triggerparticle;}
    public List<Integer> getUserTriggerIDs(){return this.UserTriggerIDs;}

    

}
