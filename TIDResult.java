package org.jlab.service.eb;

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
public class TIDResult {
    
   private int finalid;
   private int score;
   private TIDExamination tidexam = new TIDExamination();


    
    public TIDResult(){
        
    }
    
    public void   setFinalID(int id){ this.finalid = id;}
    public void   setTIDExamination(TIDExamination exam){this.tidexam = exam;}
    public void   setScore(int x ){this.score = x;} 

    
    public int getScore(){return this.score;}
    public int getFinalID(){ return this.finalid;}
    public TIDExamination getTIDExamination(){return this.tidexam;}

    

    
    
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
