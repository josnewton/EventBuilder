package org.jlab.clas.ebuilder;

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
 * @author gavalian
 */
public class EventTrigger {
    
 private double zt=0.0;
 private double rftime=0.0;
 private double t_0r=0.0;
 private double starttime;
 private List<Integer> UserTriggerIDs = new ArrayList<Integer>();//List of Trigger Particles Inputted By The User
 private DetectorParticle triggerparticle = new DetectorParticle();
    

    
    public EventTrigger(){
        
    }
    
    public void   setzt(double z_t){ this.zt = z_t;}
    public void   setRFTime(float rf){this.rftime = rf;}
    public void   setLTCC(double t_0r){this.t_0r = t_0r;}
    public void   setStartTime(double start){this.starttime = start;}
    public void   setTriggerParticle(DetectorParticle particle){this.triggerparticle=particle;}
    public void   setUserTriggerIDs(List<Integer> user){this.UserTriggerIDs = user;}



    
    public double getZt(){ return this.zt;}
    public double getRFTime(){ return this.rftime; }
    public double getT0r(){ return this.t_0r;}
    public double getStartTime(){ return this.starttime;}
    public DetectorParticle getTriggerParticle(){return this.triggerparticle;}
    public List<Integer> getUserTriggerIDs(){return this.UserTriggerIDs;}

    

    
    
   /* @Override
	public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("\t [%8s] [%3d %3d %3d] ", 
				 this.descriptor.getType().toString(),
				 this.descriptor.getSector(),
				 this.descriptor.getLayer(),
				 this.descriptor.getComponent()
				 ));
        str.append(String.format(" PINDX [%3d] ", 
				 this.getAssociation()
				 ));
        str.append(String.format(" T/P/E %8.4f %8.4f %8.4f", this.detectorTime,
				 this.particlePath,
				 this.detectorEnergy));
        str.append(String.format(" POS [ %9.3f %9.3f %9.3f ]", 
				 this.hitPosition.x(),this.hitPosition.y(),this.hitPosition.z()));
        str.append(String.format(" ACCURACY [ %9.3f %9.3f %9.3f ] ",
				 this.hitPosition.x()-this.hitPositionMatched.x(),
				 this.hitPosition.y()-this.hitPositionMatched.y(),
				 this.hitPosition.z()-this.hitPositionMatched.z()
				 ));
        return str.toString();
    }*/
}