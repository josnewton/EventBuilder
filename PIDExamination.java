package org.jlab.service.eb;

import static java.lang.Math.abs;
import java.util.HashMap;




/**
 *
 * @author Joseph Newton
 */
public class PIDExamination {
    
 

    private Integer CandidateID = -1;
    
    private Boolean closestBeta = null;
    private Boolean hasHTCC = null;
    private Boolean hasLTCC = null;
    private Boolean hasFTOF = null;
    private Boolean hasCND = null;
    private Boolean correctSF = null;
    private Boolean HTCCThreshold = null;
    private Boolean LTCCThreshold = null;

    public PIDExamination(){
        
    }
    

    public void   setCandidateID(int x){this.CandidateID = x;}
    public void   setClosestBeta(Boolean beta){this.closestBeta = beta;}
    public void   setHTCC(Boolean htcc){this.hasHTCC = htcc;}
    public void   setLTCC(Boolean ltcc){this.hasLTCC = ltcc;}
    public void   setFTOF(Boolean ftof){this.hasFTOF = ftof;}
    public void   setCorrectSF(Boolean sf){this.correctSF = sf;}
    public void   setHTCCThreshold(Boolean htcc){this.HTCCThreshold = htcc;}
    public void   setLTCCThreshold(Boolean ltcc){this.LTCCThreshold = ltcc;}
    
    public Boolean compareExams(PIDExamination mapexam){
        Boolean truth = true;
        if(mapexam.closestBeta!=null){
            if(this.closestBeta!=mapexam.closestBeta){
                truth = false;
            }
        }
        
        if(mapexam.hasHTCC!=null){
            if(this.hasHTCC!=mapexam.hasHTCC){
                truth = false;
            }
        }
        
        if(mapexam.HTCCThreshold!=null){
            if(mapexam.HTCCThreshold!=this.HTCCThreshold){
                truth = false;
            }
        }
        
        if(mapexam.hasLTCC!=null){
            if(this.hasLTCC!=mapexam.hasLTCC){
                truth = false;
            }
        }
        
        if(mapexam.LTCCThreshold!=null){
            if(this.LTCCThreshold!=mapexam.LTCCThreshold){
                truth = false;
            }
        }
        
        if(mapexam.correctSF!=null){
            if(this.correctSF!=mapexam.correctSF){
                truth = false;
            }
        }

        return truth;
    }

    public Boolean SamplingFractionCheck(DetectorParticle particle){ //Checks if particle falls within electron sampling fraction
        double p =  particle.vector().mag();
        Boolean check = false;
        double sf_calc =  particle.CalculatedSF();
        double sf_expect =  particle.ParametrizedSF();
        double sf_sigma = particle.ParametrizedSigma();
        if(sf_calc<=(sf_expect+5*sf_sigma) && sf_calc>=(sf_expect-5*sf_sigma) && p!=0.0){ //Is the calculated sampling fraction within 5 sigma
              check = true;
              }
           return check;
        }

    public Boolean HTCCSignal(DetectorParticle particle) {
        String str = "htcc";
        Boolean truth = false;
        if(particle.getNphe(str)>0){
            truth = true;
        }
        return truth;
    }
    
    public Boolean LTCCSignal(DetectorParticle particle) {
        String str = "ltcc";
        Boolean truth = false;
        if(particle.getNphe(str)>0){
            truth = true;
        }
        return truth;
    }
    
    public Boolean HTCCThreshold(DetectorParticle particle) {
        Boolean truth = false;
        if(particle.vector().mag()>4.9){
            truth = true;
        }
         return truth;
    }
    
   public Boolean LTCCThreshold(DetectorParticle particle) {
        Boolean truth = false;
        if(particle.vector().mag()>3 && particle.vector().mag()<5){
            truth = true;
        }
         return truth;
    }
    
    public Boolean GetBeta(DetectorParticle particle , int pid){
            Boolean truth = false;
            HashMap<Integer,Double> dBetas= new HashMap<Integer,Double>();
            dBetas.put(0,abs(particle.getTheoryBeta(11) - particle.getBeta()));
            dBetas.put(1,abs(particle.getTheoryBeta(2212) - particle.getBeta()));
            dBetas.put(2,abs(particle.getTheoryBeta(211) - particle.getBeta()));
            dBetas.put(3,abs(particle.getTheoryBeta(321) - particle.getBeta()));
            double min = dBetas.get(0);
            int index = 0,id=0;
            for (int i = 0; i <= 3; i++) {
             //   System.out.println(dBetas.get(i));
                if (dBetas.get(i) < min) {
                min = dBetas.get(i);
                index = i;
                }
            }
            if(index==0){
                id=11;
            }
            if(index==1){
                id=2212;
            }
            if(index==2){
                id=211;
            }
            if(index==3){
                id=321;
            }
            if(id==pid){
                truth = true;
            }
            return truth;
    }

  
    
    public Integer getCandidateID(){return this.CandidateID;}
    public Boolean getClosestBeta(){return this.closestBeta;}
    public Boolean getHTCC(){ return this.hasHTCC; }
    public Boolean getLTCC(){ return this.hasLTCC;}
    public Boolean getFTOF(){return this.hasFTOF;}
    public Boolean getCorrectSF() {return this.correctSF;}
    public Boolean getHTCCThreshold() {return this.HTCCThreshold;}
    public Boolean getLTCCThreshold() {return this.LTCCThreshold;}



}
