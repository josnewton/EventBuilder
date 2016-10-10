/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.ebuilder;

import java.util.ArrayList;
import java.util.List;
import org.jlab.detector.base.DetectorType;



import org.jlab.geom.prim.Line3D;
import org.jlab.geom.prim.Point3D;
import org.jlab.geom.prim.Vector3D;

/**
 *
 * @author gavalian
 */
public class EventBuilderMatching {
    /**
     * Matching particles with FTOF hits
     * @param particle
     * @param index
     * @param responses 
     */
    public static void particleMatchTOF(DetectorParticle particle, int index,
					List<DetectorResponse>  responses){
        
        int bestCandidate = -1;
        if(particle.getStatus()==100){
            bestCandidate = particle.getDetectorHit(responses, DetectorType.FTOF, 2, 25.0);
            if(bestCandidate<0){
                bestCandidate = particle.getDetectorHit(responses, DetectorType.FTOF, 3, 25.0);
                if(bestCandidate<0){
                    bestCandidate = particle.getDetectorHit(responses, DetectorType.FTOF, 1, 25.0);
                }
            }
            
        } else {
            bestCandidate = particle.getDetectorHit(responses, DetectorType.CTOF, 1, 25.0);
        }
        
        if(bestCandidate>=0){
            DetectorResponse res = responses.get(bestCandidate);
            if(res.getAssociation()<=0){
                Line3D distance = particle.getDistance(res); //DOCA
                FTOFIntersect ftof = new FTOFIntersect();
                Point3D pt = new Point3D();
                pt = ftof.getIntersection(particle);
                res.setAssociation(index);
                res.setPath(particle.getPathLength(res.getPosition()));
//                res.getMatchedPosition().setXYZ(
//						distance.midpoint().x(), 
//						distance.midpoint().y(), 
//						distance.midpoint().z()
//						);
                res.getMatchedPosition().setXYZ(
						pt.x(), 
						pt.y(), 
						pt.z()
						);
                particle.addResponse(res);
            }
        }                    
    }
    /**
     * Matching particles with Calorimeter Hits
     * @param particle
     * @param index
     * @param responses 
     */
    public static void particleMatchEC(DetectorParticle particle, int index,
				       List<DetectorResponse>  responses){
        if(particle.getStatus()==100){
            int bestCandidatePCAL =  particle.getDetectorHit(responses, DetectorType.EC, 1, 35.0);
            //System.out.println(bestCandidatePCAL);
            if(bestCandidatePCAL>=0){
                DetectorResponse res = responses.get(bestCandidatePCAL);
                if(res.getAssociation()<=0){
                    Line3D distance = particle.getDistance(res);
                    res.setAssociation(index);
                    res.setPath(particle.getPathLength(res.getPosition()));
                    res.getMatchedPosition().setXYZ(
						    distance.midpoint().x(), 
						    distance.midpoint().y(), 
						    distance.midpoint().z()
						    );
                    particle.addResponse(res);
                }
                
                int ecIN  = particle.getDetectorHit(responses, DetectorType.EC, 4, 50.0);
                int ecOUT = particle.getDetectorHit(responses, DetectorType.EC, 7, 50.0);
                if(ecIN>=0){
                    DetectorResponse resIN = responses.get(ecIN);
                    if(res.getAssociation()<=0){
                        Line3D distance = particle.getDistance(resIN);
                        resIN.setAssociation(index);
                        resIN.setPath(particle.getPathLength(resIN.getPosition()));
                        resIN.getMatchedPosition().setXYZ(
							  distance.midpoint().x(), 
							  distance.midpoint().y(), 
							  distance.midpoint().z()
							  );
                        particle.addResponse(resIN);
                    }
                }
                
                if(ecOUT>=0){
                    DetectorResponse resOUT = responses.get(ecOUT);
                    if(res.getAssociation()<=0){
                        Line3D distance = particle.getDistance(resOUT);
                        resOUT.setAssociation(index);
                        resOUT.setPath(particle.getPathLength(resOUT.getPosition()));
                        resOUT.getMatchedPosition().setXYZ(
							   distance.midpoint().x(), 
							   distance.midpoint().y(), 
							   distance.midpoint().z()
							   );
                        particle.addResponse(resOUT);
                    }
                }
                
            }
        }        
                                
    }
    
    
    public static void particleMatchHTCC(DetectorParticle particle, int index,
					List<CherenkovSignal>  signals){
        
        int bestCandidate = -1;
       
            bestCandidate = particle.getCherenkovSignal(signals, "htcc", 0.1);

            
        if(bestCandidate>=0){
            CherenkovSignal che = signals.get(bestCandidate);
            if(che.getAssociation()<=0){
                particle.addSignal(che);
                che.setAssociation(index);
            }
        }                    
    }
    
    public static List<DetectorParticle>  createNeutralParticles(List<DetectorResponse>  responses){        
        List<DetectorParticle> neutrals = new ArrayList<DetectorParticle>();
        for(DetectorResponse response : responses){
            if(response.getDescriptor().getType()==DetectorType.EC&&
	       response.getDescriptor().getLayer()==1&&response.getAssociation()<0){
                DetectorParticle particle = new DetectorParticle();
                particle.setCharge(0);
                particle.setStatus(300);
                particle.setPid(22);
                Vector3D  direction = new Vector3D();
                direction.setXYZ(
				 response.getPosition().x(),
				 response.getPosition().y(),
				 response.getPosition().z()
				 );
                direction.unit();
                particle.setCross(0.0,0.0,0.0,
				  direction.x(),direction.y(),direction.z());
                particle.vertex().setXYZ(0.0,0.0,0.0);
                neutrals.add(particle);
            }
        }
        return neutrals;
    }
    
    
    public static void  particleMatchNeutral(DetectorParticle particle, int index,
					     List<DetectorResponse>  responses){
        if(particle.getStatus()==300){
            
            int bestCandidatePCAL =  particle.getDetectorHit(responses, DetectorType.EC, 1, 35.0);
            if(bestCandidatePCAL>=0){
                double  energy = 0.0;
                
                DetectorResponse res = responses.get(bestCandidatePCAL);
                if(res.getAssociation()<=0){
                    Line3D distance = particle.getDistance(res);
                    
                    res.setAssociation(index);
                    
                    res.setPath(particle.getPathLength(res.getPosition()));
                    res.getMatchedPosition().setXYZ(
						    distance.midpoint().x(), 
						    distance.midpoint().y(), 
						    distance.midpoint().z()
						    );
                    energy += res.getEnergy();
                    particle.addResponse(res);
                    int ecIN  = particle.getDetectorHit(responses, DetectorType.EC, 4, 50.0);
                    int ecOUT = particle.getDetectorHit(responses, DetectorType.EC, 7, 50.0);
                    if(ecIN>=0){
                        particle.addResponse(responses.get(ecIN));
                        responses.get(ecIN).setAssociation(index);
                        energy += responses.get(ecIN).getEnergy();
                    }
                    
                    if(ecOUT>=0){
                        particle.addResponse(responses.get(ecOUT));
                        responses.get(ecOUT).setAssociation(index);
                        energy += responses.get(ecOUT).getEnergy();
                    }
                    
                    particle.vector().setXYZ(
					     energy*particle.getCrossDir().x()/particle.CalculatedSF(), 
					     energy*particle.getCrossDir().y()/particle.CalculatedSF(), 
					     energy*particle.getCrossDir().z()/particle.CalculatedSF());
                }
            }
        }
    }
}