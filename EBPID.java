/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.ebuilder;

import static java.lang.Math.abs;
import java.util.HashMap;
import org.jlab.clas.physics.Vector3;
import org.jlab.detector.base.DetectorType;
import org.jlab.geom.prim.Point3D;
import static java.lang.Math.abs;







/**
 *
 * @author Joseph Newton
 */
public class EBPID {
    

    
    
    public static void doTimeBasedPID(DetectorEvent event) {
        int nparticles = event.getParticles().size(); //Charged Particle Identification for TB Tracks
        for(int i = 0; i < nparticles; i++){ 
        
 
                TBElectron electron = new TBElectron();
                if(abs(electron.getPIDResult(event.getParticles().get(i)).getFinalID())==11){ //Is it electron or positron?
                    event.getParticles().get(i).setPIDResult(electron.getPIDResult(event.getParticles().get(i)));
                }
                
                TBPion pion = new TBPion();
                if(abs(pion.getPIDResult(event.getParticles().get(i)).getFinalID())==211){ //Is it a charged pion?
                    event.getParticles().get(i).setPIDResult(pion.getPIDResult(event.getParticles().get(i)));
                }
                
                TBKaon kaon = new TBKaon();
                if(abs(kaon.getPIDResult(event.getParticles().get(i)).getFinalID())==321){ //Is it a charged kaon?
                    event.getParticles().get(i).setPIDResult(kaon.getPIDResult(event.getParticles().get(i)));
                }
                
                TBProton proton = new TBProton();
                if(abs(proton.getPIDResult(event.getParticles().get(i)).getFinalID())==2212){  //Is it a proton or antiproton?
                    event.getParticles().get(i).setPIDResult(proton.getPIDResult(event.getParticles().get(i)));
                }
                
            }
    }



 
}


class TBElectron implements ParticleID {

            public PIDResult getPIDResult(DetectorParticle particle) {  
               
                PIDExamination PID = new PIDExamination(); //This is the "DetectorParticle"s PID properties.
                PID.setClosestBeta(PID.GetBeta(particle, 11));
                PID.setHTCC(PID.HTCCSignal(particle));
                PID.setCorrectSF(PID.SamplingFractionCheck(particle));
                PID.setHTCCThreshold(PID.HTCCThreshold(particle));
                

                
                HashMap<Integer,PIDExamination> ElectronTests= new HashMap<Integer,PIDExamination>();
                ElectronTests = PID.getElectronTests(particle);
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
                PID.setClosestBeta(PID.GetBeta(particle, 211));
                PID.setHTCC(PID.HTCCSignal(particle));
                PID.setCorrectSF(PID.SamplingFractionCheck(particle));
                PID.setHTCCThreshold(PID.HTCCThreshold(particle));
                
                HashMap<Integer,PIDExamination> PionTests= new HashMap<Integer,PIDExamination>();
                PionTests = PID.getPionTests(particle);
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
                PID.setClosestBeta(PID.GetBeta(particle, 321));
                PID.setHTCC(PID.HTCCSignal(particle));
                //PID.setLTCC(PIDAssignment.LTCCSignal(particle));
                
                HashMap<Integer,PIDExamination> KaonTests= new HashMap<Integer,PIDExamination>();
                KaonTests = PID.getKaonTests(particle);
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
                PID.setClosestBeta(PID.GetBeta(particle, 2212));
                PID.setHTCC(PID.HTCCSignal(particle));
                //PID.setLTCC(PIDAssignment.LTCCSignal(particle));
                
                HashMap<Integer,PIDExamination> ProtonTests= new HashMap<Integer,PIDExamination>();
                ProtonTests = PID.getProtonTests(particle);
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
