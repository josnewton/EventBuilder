/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.service.eb;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static java.lang.Math.abs;

/**
 *
 * @author jnewt018
 */

class TriggerElectron implements BestTrigger {
    
    public void collectBestTriggerInfo(DetectorEvent event, HashMap<Integer,DetectorParticle> triggercandidates){
                   DetectorParticle BestTrigger = BestTriggerCollection.GetBestTriggerParticle(triggercandidates);
                   event.getEventTrigger().setTriggerParticle(BestTrigger);
                   event.getEventTrigger().setStartTime(event.CalculateStartTime(11));
                   event.getEventTrigger().setzt(BestTrigger.vertex().z());
                   
    }
}

  class TriggerPion implements BestTrigger { 
      public void collectBestTriggerInfo(DetectorEvent event, HashMap<Integer,DetectorParticle> triggercandidates){
                   DetectorParticle BestTrigger = BestTriggerCollection.GetFastestTrack(triggercandidates);
                   event.getEventTrigger().setTriggerParticle(BestTrigger);
                   event.getEventTrigger().setStartTime(event.CalculateStartTime(211));
                   event.getEventTrigger().setzt(BestTrigger.vertex().z());
          
    }
}

public class BestTriggerCollection {
        public static DetectorParticle GetBestTriggerParticle(HashMap<Integer,DetectorParticle> TriggerCandidates) {
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
        
     public static DetectorParticle GetFastestTrack(HashMap<Integer,DetectorParticle> TriggerCandidates) {
            DetectorParticle FastestParticle = new DetectorParticle();
            HashMap<Integer,Double> momenta = new HashMap<Integer,Double>();
            int SizeOfMap = TriggerCandidates.size();
            for(int i = 0 ; i < SizeOfMap ; i++){
                momenta.put(i,TriggerCandidates.get(i).vector().mag());
            }
            FastestParticle = TriggerCandidates.get(GetHighestMomentumIndex(momenta));
            return FastestParticle;
        }
            
    public static int GetHighestScore(HashMap<Integer,Integer> Sc){
           int max = Sc.get(0);

            for (int i = 1; i < Sc.size(); i++) {
                if (Sc.get(i) > max) {
                max = Sc.get(i);
                }
            }
            
            return max;
        }
        
    public static int GetHighestMomentumIndex(HashMap<Integer,Double> momentum){
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
}




            
