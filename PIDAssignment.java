/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.ebuilder;

import static java.lang.Math.abs;
import java.util.HashMap;
import org.jlab.detector.base.DetectorType;





/**
 *
 * @author Joseph Newton
 */
public class PIDAssignment {

    public static Boolean SamplingFractionCheck(DetectorParticle particle){ //Checks if particle falls within electron sampling fraction
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

    public static Boolean HTCCSignal(DetectorParticle particle) {
        String str = "htcc";
        Boolean truth = false;
        if(particle.getNphe(str)>0){
            truth = true;
        }
        return truth;
    }
    
    public static Boolean LTCCSignal(DetectorParticle particle) {
        String str = "ltcc";
        Boolean truth = false;
        if(particle.getNphe(str)>0){
            truth = true;
        }
        return truth;
    }
    
    public static Boolean HTCCThreshold(DetectorParticle particle) {
        Boolean truth = false;
        if(particle.vector().mag()>4.9){
            truth = true;
        }
         return truth;
    }
    
   public static Boolean LTCCThreshold(DetectorParticle particle) {
        Boolean truth = false;
        if(particle.vector().mag()>3 && particle.vector().mag()<5){
            truth = true;
        }
         return truth;
    }
    
    public static Boolean GetBeta(DetectorParticle particle , int pid){
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

  }

class TBElectron implements ParticleID {

            public PIDResult getPIDResult(DetectorParticle particle) {  
               
                PIDExamination PID = new PIDExamination(); //This is the "DetectorParticle"s PID properties.
                PID.setClosestBeta(PIDAssignment.GetBeta(particle, 11));
                PID.setHTCC(PIDAssignment.HTCCSignal(particle));
                PID.setCorrectSF(PIDAssignment.SamplingFractionCheck(particle));
                PID.setHTCCThreshold(PIDAssignment.HTCCThreshold(particle));
                

                
                HashMap<Integer,PIDExamination> ElectronTests= new HashMap<Integer,PIDExamination>();
                ElectronTests = PIDTestMaps.getElectronTests(particle);
                PIDResult Result = new PIDResult();
                for(int i = 0 ; i < ElectronTests.size() ; i++){
                    if(PID.compareExams(ElectronTests.get(i))==true){
                        Result.setFinalID(11*-particle.getCharge());
                        Result.setPIDExamination(PID);
                        break;
                    }
                    if(i == (ElectronTests.size()-1)){
                        Result.setFinalID(0);
                        Result.setPIDExamination(PID);
                    }
                }
                
                return Result;
            }
                        
    }


class TBPion implements ParticleID {

            public PIDResult getPIDResult(DetectorParticle particle) {  
               
                PIDExamination PID = new PIDExamination(); //This is the "DetectorParticle"s PID properties.
                PID.setClosestBeta(PIDAssignment.GetBeta(particle, 211));
                PID.setHTCC(PIDAssignment.HTCCSignal(particle));
                PID.setCorrectSF(PIDAssignment.SamplingFractionCheck(particle));
                PID.setHTCCThreshold(PIDAssignment.HTCCThreshold(particle));
                
                HashMap<Integer,PIDExamination> PionTests= new HashMap<Integer,PIDExamination>();
                PionTests = PIDTestMaps.getPionTests(particle);
                PIDResult Result = new PIDResult();
                for(int i = 0 ; i < PionTests.size() ; i++){
                
                    if(PID.compareExams(PionTests.get(i))==true){
                        Result.setFinalID(211*particle.getCharge());
                        Result.setPIDExamination(PID);
                        break;
                    }
                    if(i == (PionTests.size()-1)){
                        Result.setFinalID(0);
                        Result.setPIDExamination(PID);
                    }
                }
                
                return Result;
            }
                        
    }

class TBKaon implements ParticleID {

            public PIDResult getPIDResult(DetectorParticle particle) {  
               
                PIDExamination PID = new PIDExamination(); //This is the "DetectorParticle"s PID properties.
                PID.setClosestBeta(PIDAssignment.GetBeta(particle, 321));
                PID.setHTCC(PIDAssignment.HTCCSignal(particle));
                //PID.setLTCC(PIDAssignment.LTCCSignal(particle));
                
                HashMap<Integer,PIDExamination> KaonTests= new HashMap<Integer,PIDExamination>();
                KaonTests = PIDTestMaps.getKaonTests(particle);
                PIDResult Result = new PIDResult();
                for(int i = 0 ; i < KaonTests.size() ; i++){
                    if(PID.compareExams(KaonTests.get(i))==true){
                        Result.setFinalID(321*particle.getCharge());
                        Result.setPIDExamination(PID);
                        break;
                    }
                    if(i == (KaonTests.size()-1)){
                        Result.setFinalID(0);
                        Result.setPIDExamination(PID);
                    }
                }
                
                return Result;
            }
                        
    }

class TBProton implements ParticleID {

            public PIDResult getPIDResult(DetectorParticle particle) {  
               
                PIDExamination PID = new PIDExamination(); //This is the "DetectorParticle"s PID properties.
                PID.setClosestBeta(PIDAssignment.GetBeta(particle, 2212));
                PID.setHTCC(PIDAssignment.HTCCSignal(particle));
                //PID.setLTCC(PIDAssignment.LTCCSignal(particle));
                
                HashMap<Integer,PIDExamination> ProtonTests= new HashMap<Integer,PIDExamination>();
                ProtonTests = PIDTestMaps.getProtonTests(particle);
                PIDResult Result = new PIDResult();
                for(int i = 0 ; i < ProtonTests.size() ; i++){
                    if(PID.compareExams(ProtonTests.get(i))==true){
                        Result.setFinalID(2212*particle.getCharge());
                        Result.setPIDExamination(PID);
                        break;
                    }
                    if(i == (ProtonTests.size()-1)){
                        Result.setFinalID(0);
                        Result.setPIDExamination(PID);
                    }
                }
                
                return Result;
            }
                        
    }