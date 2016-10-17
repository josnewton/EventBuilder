package org.jlab.clas.ebuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jlab.clas.physics.Vector3;
import org.jlab.detector.base.DetectorDescriptor;
import org.jlab.geom.prim.Line3D;
import org.jlab.geom.prim.Path3D;
import org.jlab.geom.prim.Point3D;
import org.jlab.clas.detector.DetectorEvent;


/**
 *
 * @author Joseph Newton
 */
public class EventTrigger {
    
 private double zt=0.0;
 private double rftime=0.0;
 private double starttime=0.0;
 private List<Integer> UserTriggerIDs = new ArrayList<Integer>();//List of Trigger Particles Inputted By The User
 private DetectorParticle triggerparticle = new DetectorParticle();
    

    
    public EventTrigger(){
        
    }
    
    public void   setzt(double z_t){ this.zt = z_t;}
    public void   setRFTime(float rf){this.rftime = rf;}
    public void   setStartTime(double start){this.starttime = start;}
    public void   setTriggerParticle(DetectorParticle particle){this.triggerparticle=particle;}
    public void   setUserTriggerIDs(List<Integer> user){this.UserTriggerIDs = user;}


    public DetectorParticle GetBestTriggerParticle(HashMap<Integer,DetectorParticle> TriggerCandidates) {
            DetectorParticle BestTrigger = new DetectorParticle();
            int SizeOfMap = TriggerCandidates.size();
            HashMap<Integer,Integer> Scores = new HashMap<Integer,Integer>();

            for(int i = 0 ; i < SizeOfMap; i++){
              Scores.put(i,TriggerCandidates.get(i).getTIDResult().getScore()); 
            }
            int HighestScore = GetHighestScore(Scores);
            int SizeofMap = TriggerCandidates.size();
            List<Integer> IndicesThatMatchWithHighestScore = new ArrayList<Integer>();
            for(int i = 0 ; i < SizeofMap; i++){ 
                if(Scores.get(i)==HighestScore){
                    IndicesThatMatchWithHighestScore.add(i);
                }
            }
            HashMap<Integer,Double> Momenta = new HashMap<Integer,Double>();
            HashMap<Integer,Integer> IndexTranslation = new HashMap<Integer,Integer>();
            for(int i = 0 ; i < IndicesThatMatchWithHighestScore.size() ; i++){
                int index = IndicesThatMatchWithHighestScore.get(i);
                Momenta.put(i,TriggerCandidates.get(index).vector().mag());
                IndexTranslation.put(i,index);
            }
            int HighestMomentumIndex = GetHighestMomentumIndex(Momenta);
            int CorrectHighestMomentumIndex = IndexTranslation.get(HighestMomentumIndex);
            BestTrigger = TriggerCandidates.get(CorrectHighestMomentumIndex);
            
            return BestTrigger;
        }
        
    public DetectorParticle GetFastestTrack(HashMap<Integer,DetectorParticle> TriggerCandidates) {
            DetectorParticle FastestParticle = new DetectorParticle();
            HashMap<Integer,Double> momenta = new HashMap<Integer,Double>();
            int SizeOfMap = TriggerCandidates.size();
            for(int i = 0 ; i < SizeOfMap ; i++){
                momenta.put(i,TriggerCandidates.get(i).vector().mag());
            }
            FastestParticle = TriggerCandidates.get(GetHighestMomentumIndex(momenta));
            return FastestParticle;
        }
            
    public int GetHighestScore(HashMap<Integer,Integer> Sc){
           int max = Sc.get(0);

            for (int i = 1; i < Sc.size(); i++) {
                if (Sc.get(i) > max) {
                max = Sc.get(i);
                }
            }
            
            return max;
        }
        
    public int GetHighestMomentumIndex(HashMap<Integer,Double> momentum){
            Double max = momentum.get(0);
            int MaximumIndex = 0;
            for (int i = 1; i < momentum.size(); i++) {
                if (momentum.get(i) > max) {
                max = momentum.get(i);
                MaximumIndex = i;
                }
            }
            
            return MaximumIndex;
        }
    
    public double getZt(){ return this.zt;}
    public double getRFTime(){ return this.rftime; }
    public double getStartTime(){ return this.starttime;}
    public DetectorParticle getTriggerParticle(){return this.triggerparticle;}
    public List<Integer> getUserTriggerIDs(){return this.UserTriggerIDs;}

    

}
