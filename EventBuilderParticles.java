/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.ebuilder;

import java.util.ArrayList;
import java.util.List;
import org.jlab.clas.physics.GenericKinematicFitter;

import org.jlab.clas.physics.PhysicsEvent;
import org.jlab.geom.prim.Vector3D;
import org.jlab.io.evio.EvioDataBank;
import org.jlab.io.evio.EvioDataEvent;
import org.jlab.io.evio.EvioFactory;


/**
 *
 * @author gavalian
 */
public class EventBuilderParticles {
    
    public static GenericKinematicFitter   KinematicFitter = new GenericKinematicFitter(11.0);
    
    /**
     * reads all particles from the event. returns a list with 
     * forward and central particles.
     * @param event
     * @return 
     */
    public static List<DetectorParticle>  getParticles(EvioDataEvent event){
        List<DetectorParticle> particles = new ArrayList<DetectorParticle>();
        List<DetectorParticle> forward = EventBuilderParticles.getParticlesForward(event);
      //  List<DetectorParticle> central = EventBuilderParticles.getParticlesCentral(event);
       // List<DetectorParticle> tagger = EventBuilderParticles.getParticlesTagger(event);
        
        particles.addAll(forward);
      //  particles.addAll(central);
     //   particles.addAll(tagger);
        return particles;
    }
    /**
     * returns a list of particles from the forward detectors.
     * @param event
     * @return 
     */
    public static List<DetectorParticle> getParticlesForward(EvioDataEvent event){
        double gp = 0.0,theta=0.0,phi=0.0;
                if(event.hasBank("GenPart::true")==true)  {
          EvioDataBank trueBank = (EvioDataBank) event.getBank("GenPart::true");
	  int nrowsTrue = trueBank.rows();
	  if(nrowsTrue==1) {
	  // Vector3D gen = Vector3D(px,py,pz);
	   double px = trueBank.getDouble("px",0)/1000;
	   double py = trueBank.getDouble("py",0)/1000;
	   double pz = trueBank.getDouble("pz",0)/1000;
	   //System.out.println(px);
           Vector3D gen = new Vector3D(px,py,pz);
	   gp = gen.mag();
           theta = gen.theta();
           phi = gen.phi();
           
//	   System.out.println(gp);
	  }
	}
        List<DetectorParticle> particles = new ArrayList<DetectorParticle>();
        if(event.hasBank("TimeBasedTrkg::TBTracks")==true){ 
            //System.out.println("  FOUND TBTRACKS ");
            EvioDataBank bank = (EvioDataBank) event.getBank("TimeBasedTrkg::TBTracks");
            int nrows = bank.rows();
            for(int loop = 0; loop < nrows; loop++){
                
                DetectorParticle   particle = new DetectorParticle();
                particle.setPid(0);
                particle.setStatus(100);
                
                particle.setPath(bank.getDouble("pathlength", loop));
                
                particle.setCharge(bank.getInt("q", loop));
                
                particle.setCross(
				  bank.getDouble("c3_x", loop), 
				  bank.getDouble("c3_y", loop),
				  bank.getDouble("c3_z", loop),
				  bank.getDouble("c3_ux", loop), 
				  bank.getDouble("c3_uy", loop),
				  bank.getDouble("c3_uz", loop)
				  );
                //part.charge = bank.getInt("q", loop);
                particle.vector().setXYZ(
					 bank.getDouble("p0_x",loop),
					 bank.getDouble("p0_y",loop),
					 bank.getDouble("p0_z",loop)
					 );

                particle.vertex().setXYZ(
					 bank.getDouble("Vtx0_x",loop),
					 bank.getDouble("Vtx0_y",loop),
					 bank.getDouble("Vtx0_z",loop)
					 );
  
                
                particles.add(particle);
            }            
        }
        return particles;
    }
    /**
     * returns list of particles from the central detector.
     * @param event
     * @return 
     */
    public static List<DetectorParticle> getParticlesCentral(EvioDataEvent event){
        List<DetectorParticle> particles = new ArrayList<DetectorParticle>();
        if(event.hasBank("CVTRec::Tracks")==true){
            EvioDataBank bankSVT = (EvioDataBank) event.getBank("CVTRec::Tracks");
            int nrows = bankSVT.rows();
            for(int loop = 0; loop < nrows; loop++){
                
                DetectorParticle  particle = new DetectorParticle();

                particle.setCross(
				  bankSVT.getDouble("c_x", loop), 
				  bankSVT.getDouble("c_y", loop),
				  bankSVT.getDouble("c_z", loop),
				  bankSVT.getDouble("c_ux", loop), 
				  bankSVT.getDouble("c_uy", loop),
				  bankSVT.getDouble("c_uz", loop)
				  );

                particle.setCharge( bankSVT.getInt("q", loop));
                
                double tandip = bankSVT.getDouble("tandip", loop);
                double phi    = bankSVT.getDouble("phi0", loop);
                double pt     = bankSVT.getDouble("pt", loop);
                double p      = bankSVT.getDouble("p" , loop);
                //double sinth  = tandip/Math.sqrt(1+tandip*tandip);
                double pz     = pt*tandip;
                double px     = pt*Math.cos(phi);
                double py     = pt*Math.sin(phi);
                particle.vector().setXYZ(
					 px,py,pz
					 );
                particle.vertex().setXYZ(0.0,0.0,
					 bankSVT.getDouble("z0", loop)
					 );
                particle.setPath(bankSVT.getDouble("pathlength", loop));
                particle.setStatus(200);
                particles.add(particle);
            }
        }
        return particles;
    }
    
        public static List<DetectorParticle> getParticlesTagger(EvioDataEvent event){
        List<DetectorParticle> particles = new ArrayList<DetectorParticle>();
        if(event.hasBank("FTRec::Tracks")==true){ 
            EvioDataBank bank = (EvioDataBank) event.getBank("FTRec::Tracks");
            int nrows = bank.rows();
            for(int loop = 0; loop < nrows; loop++){
                
                DetectorParticle   particle = new DetectorParticle();
                particle.setPid(0);
                particle.setStatus(100);
                
                //particle.setPath(bank.getDouble("pathlength", loop));
                
                particle.setCharge(bank.getInt("charge", loop));
                
                particle.setCross(
				  0.0, 
				  0.0,
				  0.0,
				  bank.getFloat("Cx", loop), 
				  bank.getDouble("Cy", loop),
				  bank.getDouble("Cz", loop)
				  );
                //part.charge = bank.getInt("q", loop);
                particle.vector().setXYZ(
                                  bank.getFloat("Cx", loop), 
				  bank.getDouble("Cy", loop),
				  bank.getDouble("Cz", loop)
					 );
                particle.vertex().setXYZ(
					 0.0,
					 0.0,
					 0.0
					 );
                
                particles.add(particle);
            }            
        }
        return particles;
    }
             
    public static DetectorEvent  getDetectorEvent(EvioDataEvent event){
        List<DetectorParticle> particles = EventBuilderParticles.getParticles(event);
        DetectorEvent  de = new DetectorEvent();
        for(DetectorParticle p : particles){
            de.addParticle(p);
        }
        return de;
    }
    
    /**
     * creates a bank with EVENT particles.
     * @param event
     * @return 
     */
    public static EvioDataBank  createBank(DetectorEvent event){
        int nrows = event.getParticles().size();
        EvioDataBank bank = EvioFactory.createBank("EVENTHB::particle", nrows);
        for(int i = 0; i < nrows; i++){
            bank.setInt("status",i, event.getParticles().get(i).getStatus());
            bank.setInt("charge",i, event.getParticles().get(i).getCharge());
            bank.setInt("pid", i,event.getParticles().get(i).getPid());
            bank.setFloat("px", i,(float) event.getParticles().get(i).vector().x());
            bank.setFloat("py", i,(float) event.getParticles().get(i).vector().y());
            bank.setFloat("pz", i,(float) event.getParticles().get(i).vector().z());
            bank.setFloat("vx", i,(float) event.getParticles().get(i).vertex().x());
            bank.setFloat("vy", i,(float) event.getParticles().get(i).vertex().y());
            bank.setFloat("vz", i,(float) event.getParticles().get(i).vertex().z());
            bank.setFloat("beta", i,(float) event.getParticles().get(i).getBeta());
            bank.setFloat("mass", i,(float) event.getParticles().get(i).getMass());            
        }
        return bank;
    }
    
    /*
    public static DetectorEvent  readDetectorEvent(EvioDataEvent event){
                
        DetectorEvent detectorEvent = new DetectorEvent();
        if(event.hasBank("EVENTHB::particle")==true){
            EvioDataBank  bank = (EvioDataBank) event.getBank("EVENTHB::particle");
            int nrows = bank.rows();
            for(int i = 0; i < nrows; i++){
                DetectorParticle particle = new DetectorParticle();
                particle.setStatus(bank.getInt("status", i));
                particle.setPid(bank.getInt("pid", i));
                particle.setCharge(bank.getInt("charge", i));
                particle.setBeta(bank.getFloat("beta", i));
                particle.setMass(bank.getFloat("mass", i));
                particle.vector().setXYZ(
					 bank.getFloat("px", i),
					 bank.getFloat("py", i),
					 bank.getFloat("pz", i)
					 );
                particle.vertex().setXYZ(
					 bank.getFloat("vx", i),
					 bank.getFloat("vy", i),
					 bank.getFloat("vz", i)
					 );
                detectorEvent.addParticle(particle);
            }
        }
        
        List<DetectorResponse>   responses = EventBuilderDetectors.readDetectorResponses(event);
        for(DetectorResponse res : responses){
            int index = res.getAssociation();
            if(index>=0&&index<detectorEvent.getParticles().size()){
                detectorEvent.getParticles().get(index).addResponse(res);
            }
        }
        
        PhysicsEvent  generatedEvent = EventBuilderParticles.KinematicFitter.getGeneratedEvent(event);
        int count = generatedEvent.count();
        detectorEvent.getGeneratedEvent().clear();
        for(int j = 0; j < count; j++){
            detectorEvent.getGeneratedEvent().addParticle(generatedEvent.getParticle(j));
        }
        
        return detectorEvent;
    }*/
}