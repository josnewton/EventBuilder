/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.ebuilder;



import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jlab.clas.physics.Particle;
import org.jlab.clas.physics.PhysicsEvent;
import org.jlab.detector.base.DetectorType;
import org.jlab.geom.prim.Point3D;


/**
 *
 * @author gavalian 
 */
public class DetectorEvent {
    
    private List<DetectorParticle>  particleList = new ArrayList<DetectorParticle>();
    private EventTrigger trigger = new EventTrigger();
    private PhysicsEvent   generatedEvent = new PhysicsEvent();
    private PhysicsEvent   reconstructedEvent = new PhysicsEvent();

    
    public DetectorEvent(){
        
    }
    
    public PhysicsEvent getGeneratedEvent(){
        return this.generatedEvent;
    }
    
    public PhysicsEvent getPhysicsEvent(){
        return this.reconstructedEvent;
    }
    
    public DetectorParticle matchedParticle(int pid, int skip){
        Particle particle = generatedEvent.getParticleByPid(pid, skip);
        if(particle.p()<0.0000001) return new DetectorParticle();
        return matchedParticle(particle);
    }
    
    public DetectorParticle matchedParticle(Particle p){
        double compare = 100.0;
        int index = -1;
        for(int i = 0; i < particleList.size();i++){
            if(p.charge()==particleList.get(i).getCharge()){
		//System.out.println("index = " + i + "  compare = " + particleList.get(i).compare(p.vector().vect()));
                if(particleList.get(i).compare(p.vector().vect())<compare){
                    compare = particleList.get(i).compare(p.vector().vect());
                    index   = i; 
                }
            }
        }
        if(index<0&&compare>0.2) return new DetectorParticle();
        return this.particleList.get(index);
    }
    
    public void clear(){
        this.particleList.clear();
    }
    
    public void addParticle(DetectorParticle particle){
        
        this.particleList.add(particle);
    }
    
    public List<DetectorParticle> getParticles(){ return this.particleList;}
    
    public void addParticle(double px, double py, double pz,
			    double vx, double vy, double vz){
        DetectorParticle particle = new DetectorParticle();
        particle.vector().setXYZ(px, py, pz);
        particle.vertex().setXYZ(vx, vy, vz);
        this.addParticle(particle);
    }
    

    public void   setEventTrigger(EventTrigger trig){this.trigger = trig;}
    
    public EventTrigger getEventTrigger(){return this.trigger;}
   
    public HashMap<Integer,DetectorParticle> ObtainTriggerCandidates(int particleid)  {
        HashMap<Integer,DetectorParticle> map = new HashMap<Integer,DetectorParticle>();
        if(particleid==11){
            int mapiteration = 0;
            for(int i = 0 ; i < this.particleList.size() ; i++){
                if(this.particleList.get(i).getTIDResult().getTIDExamination().getHTCC()==true && 
                        this.particleList.get(i).getTIDResult().getTIDExamination().getFTOF()==true
                        && this.particleList.get(i).getCharge()==-1){
                    map.put(mapiteration,this.particleList.get(i));
                    mapiteration = mapiteration + 1;
                }
            }
        }
        if(particleid==-11){
            int mapiteration = 0;
            for(int i = 0 ; i < this.particleList.size() ; i++){
                if(this.particleList.get(i).getTIDResult().getTIDExamination().getHTCC()==true &&
                        this.particleList.get(i).getTIDResult().getTIDExamination().getFTOF()==true 
                        && this.particleList.get(i).getCharge()==1){
                    map.put(mapiteration,this.particleList.get(i));
                    mapiteration = mapiteration + 1;
                }
            }
        }
        if(particleid==211 || particleid==2212 || particleid==321){
            int mapiteration = 0;
            for(int i = 0 ; i < this.particleList.size() ; i++){
                if(this.particleList.get(i).getCharge()==1){
                    map.put(mapiteration,this.particleList.get(i));
                    mapiteration = mapiteration + 1;
                }
            }
        }
        if(particleid==-211 || particleid==-2212 || particleid==-321){
            int mapiteration = 0;
            for(int i = 0 ; i < this.particleList.size() ; i++){
                if(this.particleList.get(i).getCharge()==-1){
                    map.put(mapiteration,this.particleList.get(i));
                    mapiteration = mapiteration + 1;
                }
            }
        }
        if(particleid==22){
             int mapiteration = 0;
            for(int i = 0 ; i < this.particleList.size() ; i++){
                if(this.particleList.get(i).getCharge()==0 && this.particleList.get(i).getTIDResult().getTIDExamination().getCorrectSF()==true){
                    map.put(mapiteration,this.particleList.get(i));
                    mapiteration = mapiteration + 1;
                }
            }           
        }
        return map;
    }        
    
       public double CalculateStartTime(int usertriggerid){
            double t_0r = 0;
            DetectorParticle particle = new DetectorParticle();
            particle = this.trigger.getTriggerParticle();
            if(particle.hasHit(DetectorType.FTOF, 2)==true){
            double beta = 0.0;
            if(abs(usertriggerid)==11){
                beta = 1;//We assign electron beta if at least one track has responses in FTOF/HTCC
            }
            if(abs(usertriggerid)==211){
                beta = particle.getTheoryBeta(211); //We Assign Pion mass to the fastest negative track
            }
            if(abs(usertriggerid)==22){
                beta = particle.getTheoryBeta(22);
            }

            double t_0 = 0,deltatr = 0, t_rf = 0, z_0 = 0, m = 1000000;
            t_0r = particle.getTime(DetectorType.FTOF) - (particle.getPathLength(DetectorType.FTOF))/(beta*29.9792);
            deltatr = t_0r - t_rf - (particle.vertex().z() - z_0)/beta + m*2.0004;
            t_0 = deltatr%2.0004 - 2.0004/2;

            }
   
      
            return t_0r;
        }
    
    public boolean hasElectron(){
        boolean x = false;
        int i = 0;
        while(i < particleList.size()){
            if(particleList.get(i).getPid()==11){
                x = true;
                break;
            }
        }
        return x;
    }
    
    public DetectorParticle getElectron() {
        DetectorParticle part = new DetectorParticle();
        int i = 0;
           while(i < particleList.size()){
            if(particleList.get(i).getPid()==11){
                part = particleList.get(i); 
                break;
            }
        }
        
        return part;
    }
    
    @Override
	public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(" === [ DETECTOR EVENT ] === \n");
        for(DetectorParticle particle : this.particleList){
            str.append(particle.toString());
            str.append("\n");
        }
        return str.toString();
    }


}
