/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.ebuilder;

import java.util.ArrayList;
import java.util.List;
import org.jlab.clas.physics.Vector3;
import org.jlab.detector.base.DetectorType;



/**
 *
 * @author gavalian
 */
public class EBProcessor {
    
    List<org.jlab.clas.ebuilder.DetectorParticle>      particles = new ArrayList<org.jlab.clas.ebuilder.DetectorParticle>();
    List<org.jlab.clas.detector.DetectorResponse>  responsesFTOF = new ArrayList<org.jlab.clas.detector.DetectorResponse>();
    List<org.jlab.clas.detector.DetectorResponse>  responsesECAL = new ArrayList<org.jlab.clas.detector.DetectorResponse>();
    List<CherenkovSignal>  signalsHTCC = new ArrayList<CherenkovSignal>();

    
    public EBProcessor(){
        
    }
    
    public EBProcessor(List<org.jlab.clas.ebuilder.DetectorParticle> list){
        particles.addAll(list);
    }
    
    public void addTOF(List<org.jlab.clas.detector.DetectorResponse> det_ftof){
        responsesFTOF.addAll(det_ftof);
    }
    
    public void addECAL(List<org.jlab.clas.detector.DetectorResponse> det_ecal){
        responsesECAL.addAll(det_ecal);
    }
    
    public void addHTCC(List<CherenkovSignal> che_htcc){
        signalsHTCC.addAll(che_htcc);
    }
    
    public void matchCalorimeter(){
        
        for(org.jlab.clas.detector.DetectorResponse resp : responsesECAL){
           // System.out.println("Response layer   " + resp.getDescriptor().getLayer());
            resp.setAssociation(-1);
        }
        
        List<org.jlab.clas.detector.DetectorResponse>  pcal = org.jlab.clas.detector.DetectorResponse.getListByLayer(responsesECAL, DetectorType.EC, 1);
      // System.out.println(" PCAL HITS SIZE = " + pcal.size());
        int iparticle = 0;
        for(org.jlab.clas.ebuilder.DetectorParticle p : particles){ 
            //System.out.println(p.vector().mag());
            int index = p.getDetectorHit(pcal,DetectorType.EC,1,15.0);
            if(index>=0){
                //System.out.println("Getdetectorhit works!!!");
                org.jlab.clas.detector.DetectorResponse response = pcal.get(index);
                response.setAssociation(iparticle);
                p.addResponse(response);
            }
            iparticle++;
        }
        
        
        List<org.jlab.clas.detector.DetectorResponse>  ecin = org.jlab.clas.detector.DetectorResponse.getListByLayer(responsesECAL, DetectorType.EC, 4);
       // System.out.println(" ECIN COUNTER = " + ecin.size());
        iparticle = 0;
        for(org.jlab.clas.ebuilder.DetectorParticle p : particles){            
            int index = p.getDetectorHit(ecin,DetectorType.EC,4,10.0);
            //System.out.println(index);
            if(index>=0){
                org.jlab.clas.detector.DetectorResponse response = ecin.get(index);
                response.setAssociation(iparticle);
                p.addResponse(response);
            }
            iparticle++;
        }
        
        List<org.jlab.clas.detector.DetectorResponse>  ecout = org.jlab.clas.detector.DetectorResponse.getListByLayer(responsesECAL, DetectorType.EC, 7);
        //System.out.println(" ECOUT COUNTER = " + ecout.size());
        iparticle = 0;
        for(org.jlab.clas.ebuilder.DetectorParticle p : particles){            
            int index = p.getDetectorHit(ecout,DetectorType.EC,7,10.0);
            if(index>=0){
                org.jlab.clas.detector.DetectorResponse response = ecout.get(index);
                response.setAssociation(iparticle);
                p.addResponse(response);
            }
            iparticle++;
        }
    }
    
    
    public List<org.jlab.clas.ebuilder.DetectorParticle>  getParticles(){return this.particles;}
    
    
    public void matchNeutral(){
        List<org.jlab.clas.detector.DetectorResponse>  pcal  = org.jlab.clas.detector.DetectorResponse.getListByLayer(responsesECAL, DetectorType.EC, 1);
        List<org.jlab.clas.ebuilder.DetectorParticle>  gamma = new ArrayList<org.jlab.clas.ebuilder.DetectorParticle>();
        
        for(int i = 0; i < pcal.size(); i++){
            if(pcal.get(i).getAssociation()<0){
                org.jlab.clas.ebuilder.DetectorParticle  p = new org.jlab.clas.ebuilder.DetectorParticle();
                Vector3  u = new Vector3(pcal.get(i).getPosition().x(),
                        pcal.get(i).getPosition().y(),pcal.get(i).getPosition().z());
                u.unit();
                p.setCross(0.0, 0.0, 0.0, u.x(),u.y(),u.z());
                p.setPid(22);
                p.setCharge(0);                
                p.addResponse(pcal.get(i));
                gamma.add(p);
                
            }
        }
        List<org.jlab.clas.detector.DetectorResponse>  ecin  = org.jlab.clas.detector.DetectorResponse.getListByLayer(responsesECAL, DetectorType.EC, 4);
        List<org.jlab.clas.detector.DetectorResponse>  ecout = org.jlab.clas.detector.DetectorResponse.getListByLayer(responsesECAL, DetectorType.EC, 7);
        
        int nparticles = this.particles.size();
        int iparticle  = 0;
        for(org.jlab.clas.ebuilder.DetectorParticle p : gamma){
            int index = p.getDetectorHit(ecin,DetectorType.EC,4,10.0);
           // System.out.println(index);
            if(index>=0){
                if(ecin.get(index).getAssociation()<0){
                    //System.out.println("hello");
                    ecin.get(index).setAssociation(nparticles+iparticle);
                    p.addResponse(ecin.get(index));
                }
            }
            
            index = p.getDetectorHit(ecout,DetectorType.EC,7,10.0);
            if(index>=0){
                if(ecout.get(index).getAssociation()<0){
                    ecout.get(index).setAssociation(nparticles+iparticle);
                    p.addResponse(ecout.get(index));
                }
            }
            iparticle++;
            
            double  energy = p.getEnergy(DetectorType.EC)/0.27;
            Vector3 dir    = p.getCrossDir();
            p.vector().setXYZ(dir.x()*energy, dir.y()*energy, dir.z()*energy);
            p.setBeta(1.0);
        }
        
        for(int i = 0; i < gamma.size(); i++){
            if(gamma.get(i).vector().mag()>0.5){
                particles.add(gamma.get(i));
            }
        }
    }
    
    public void matchTimeOfFlight(){
        
        for(org.jlab.clas.detector.DetectorResponse resp : responsesFTOF){
            resp.setAssociation(-1);
        }
        
        int iparticle = 0;
        for(org.jlab.clas.ebuilder.DetectorParticle p : particles){            
            int index = p.getDetectorHit(responsesFTOF,DetectorType.FTOF,2,15.0);
           // System.out.println(index);
            if(index>=0){
                org.jlab.clas.detector.DetectorResponse response = responsesFTOF.get(index);
                response.setAssociation(iparticle);
                p.addResponse(response);
                p.setBeta(p.getBeta(DetectorType.FTOF));
                p.setMass(p.getMass2(DetectorType.FTOF));
            }
            iparticle++;
        }        
        
    }

        
    public void matchHTCC(){
        
        for(CherenkovSignal che : signalsHTCC){
            che.setAssociation(-1);
        }
      
       for(org.jlab.clas.ebuilder.DetectorParticle p : particles){
       int bestCandidate = p.getCherenkovSignal(signalsHTCC, "htcc", 0.1);
       //System.out.println(bestCandidate);
       if(bestCandidate>=0) {
            CherenkovSignal che = signalsHTCC.get(bestCandidate);
                if(che.getAssociation()<0){
                p.addSignal(che);
                //System.out.println(che.getNphe());
                che.setAssociation(bestCandidate);
       }
         }  
       }
    }

    
    
    
    public void show(){
        System.out.println("------->  SHOW Event BUILDER Results <------");
        System.out.println(" ECAL HITS = " + responsesECAL.size());
        for(org.jlab.clas.ebuilder.DetectorParticle p : particles){
            System.out.println(p);
        }
    }
}