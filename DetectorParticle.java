/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.service.eb;



import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jlab.clas.detector.DetectorResponse;
import org.jlab.clas.physics.Particle;
import org.jlab.clas.physics.PhysicsEvent;
import org.jlab.clas.physics.Vector3;
import org.jlab.detector.base.DetectorType;
import org.jlab.geom.prim.Line3D;
import org.jlab.geom.prim.Path3D;
import org.jlab.geom.prim.Point3D;


/**
 *
 * @author gavalian
 */
public class DetectorParticle {
    
    private Vector3 particleMomenta = new Vector3();
    private Vector3 particleVertex  = new Vector3();
    private Integer particleCharge  = 0;
    private Integer particlePID     = 0;
    private Integer particleStatus  = 1;
    private Integer particleScore   = 0;
    private Boolean particleTiming = null;
    private Double  particleBeta    = 0.0;
    private Double  particleMass    = 0.0;
    private Double  particlePath    = 0.0;
    private Vector3 particleCrossPosition  = new Vector3();
    private Vector3 particleCrossDirection = new Vector3();
    

    
    private List<DetectorResponse>  responseStore = new ArrayList<DetectorResponse>();
    private List<CherenkovSignal> cherenkovStore = new ArrayList<CherenkovSignal>();
    
    private TreeMap<DetectorType,TrackIntersect>  projectedHit = new  TreeMap<DetectorType,TrackIntersect>();
    

    private PIDResult pidresult = new PIDResult();
    
    
    
    public DetectorParticle(){
        
    }
    
    public void clear(){
        this.responseStore.clear();
    }
    
    public void addResponse(DetectorResponse res, boolean match){
        if(match==false){
            this.responseStore.add(res);
        }
    }
    
    public void addSignal(CherenkovSignal che) {
      
            this.cherenkovStore.add(che);
       
    }
    
    
    public Particle getPhysicsParticle(int pid){
        Particle  particle = new Particle(pid,
					  this.vector().x(),this.vector().y(),this.vector().z(),
					  this.vertex().x(),this.vertex().y(),this.vertex().z()
					  );
        return particle;
    }
    
    public double compare(Vector3 vec){
        return this.vector().compare(vec);
    }
    
    public double compare(double x, double y, double z){
        return this.vector().compare(new Vector3(x,y,z));
    }
    
    public void addResponse(DetectorResponse res){
        /*
        double distance = Math.sqrt(
                (this.particleCrossPosition.x()-res.getPosition().x())*
                        (this.particleCrossPosition.x()-res.getPosition().x())
                +
                        (this.particleCrossPosition.y()-res.getPosition().y())*
                                (this.particleCrossPosition.y()-res.getPosition().y())
                +
                        (this.particleCrossPosition.z()-res.getPosition().z())*
                                (this.particleCrossPosition.z()-res.getPosition().z())
        );
        
        Line3D   crossLine = new Line3D(this.particleCrossPosition.x(),
                this.particleCrossPosition.y(),
                this.particleCrossPosition.z(),
                this.particleCrossDirection.x()*1500.0,
                this.particleCrossDirection.y()*1500.0,
                this.particleCrossDirection.z()*1500.0);
        
        Line3D distanceLine = crossLine.distance(new Point3D
                (res.getPosition().x(), res.getPosition().y(),res.getPosition().z()));
        
        res.getMatchedPosition().setXYZ(
                distanceLine.origin().x(),
                distanceLine.origin().y(),
                distanceLine.origin().z()
                );
        
        res.setPath(distance+this.particlePath);
        this.responseStore.add(res);*/
        this.responseStore.add(res);
    }
    
    public boolean getParticleTimeCheck(){
            return this.particleTiming;
            }
    
    public double CalculatedSF() {
                //System.out.println(this.getEnergy(DetectorType.EC)/this.vector().mag());
                return this.getEnergy(DetectorType.EC)/this.vector().mag();
            }
            
     public double ParametrizedSF() {
                double sf = 0.0;
                double p = this.vector().mag();
                if(this.vector().mag()<=3){
                    sf = -0.0035*pow(p,4) + 0.0271*pow(p,3) - 0.077*pow(p,2) + 0.0985*pow(p,1) + 0.2241;
                }
                
                if(this.vector().mag()>3){
                    sf = 0.0004*p + 0.2738;
                }
                return sf;
            }
            
    public double ParametrizedSigma(){
                double p = this.vector().mag();
                double sigma = 0.02468*pow(p,-0.51);
                
           return sigma;
                
    }
    
    public DetectorResponse  getResponse(DetectorType type, int layer){
        for(DetectorResponse res : this.responseStore){
            if(res.getDescriptor().getType()==type&&res.getDescriptor().getLayer()==layer){
                return res;
            }
        }
        return null;
    }
    
    
    public boolean hasHit(DetectorType type){
        int hits = 0;
        for( DetectorResponse res : this.responseStore){
            if(res.getDescriptor().getType()==type) hits++;
        }
        if(hits==0) return false;
        if(hits>1) System.out.println("[Warning] Too many hits for detector type = " + type);
        return true;
    }
    public boolean hasHit(DetectorType type, int layer){
        int hits = 0;
        for( DetectorResponse res : this.responseStore){
            if(res.getDescriptor().getType()==type&&res.getDescriptor().getLayer()==layer) hits++;
        }
        if(hits==0) return false;
        if(hits>1) System.out.println("[Warning] Too many hits for detector type = " + type);
        return true;
    }
    
    public List<DetectorResponse>  getDetectorResponses(){
        return this.responseStore;
    }
    
    public List<CherenkovSignal>  getCherenkovSignals(){
        return this.cherenkovStore;
    }
    
    public DetectorResponse getHit(DetectorType type){
        for(DetectorResponse res : this.responseStore){
            if(res.getDescriptor().getType()==type) return res;
        }
        return null;
    }
    
    
    public DetectorResponse getHit(DetectorType type, int layer){
        for(DetectorResponse res : this.responseStore){
            if(res.getDescriptor().getType()==type&&res.getDescriptor().getLayer()==layer) return res;
        }
        return null;
    }
    
    public double getBeta(){ return this.particleBeta;}
    public double getTheoryBeta(int id){
        double beta = 0.0;
        if(id==11 || id==-11){
            beta = this.particleMomenta.mag()/sqrt(this.particleMomenta.mag()*this.particleMomenta.mag() + 0.00051*0.00051);
            //beta = 1.0;
            //System.out.println("Beta is  " + beta);
        }
        if(id==-211 || id==211){
            beta = this.particleMomenta.mag()/sqrt(this.particleMomenta.mag()*this.particleMomenta.mag() + 0.13957*0.13957);

        }
        if(id==2212 || id==-2212){
            beta = this.particleMomenta.mag()/sqrt(this.particleMomenta.mag()*this.particleMomenta.mag() + 0.938*0.938);
            //System.out.println("Beta is  " + beta);
        }
        if(id==-321 || id==321){
            beta = this.particleMomenta.mag()/sqrt(this.particleMomenta.mag()*this.particleMomenta.mag() + 0.493667*0.493667);

        }
        return beta;
    }
    public int    getStatus(){ return this.particleStatus;}
    public double getMass(){ return this.particleMass;}
    public int    getPid(){ return this.particlePID;}
    
    public Path3D getTrajectory(){
        Path3D  path = new Path3D();
        //path.addPoint(this.particleCrossPosition.x(), 
        //        this.particleCrossPosition.y()
        //        , this.particleCrossPosition.z());
        path.generate(
		      this.particleCrossPosition.x(),
		      this.particleCrossPosition.y(),
		      this.particleCrossPosition.z(),
		      this.particleCrossDirection.x(), 
		      this.particleCrossDirection.y(), 
		      this.particleCrossDirection.z(),                               
		      1500.0, 2);
        return path;
    }
    
    public Vector3  vector(){return this.particleMomenta;}    
    public Vector3  vertex(){return this.particleVertex;}    
    public Vector3  getCross(){ return this.particleCrossPosition;}    
    public Vector3  getCrossDir(){ return this.particleCrossDirection;}    
    public double   getPathLength(){ return this.particlePath;}
    public int      getCharge(){ return this.particleCharge;}

    
    public double   getPathLength(DetectorType type){
        DetectorResponse response = this.getHit(type);
        if(response==null) return -1.0;
        return this.getPathLength(response.getPosition());
    }
    
    public double  getPathLength(DetectorType type, int layer){
        DetectorResponse response = this.getHit(type,layer);
        if(response==null) return -1.0;
        return this.getPathLength(response.getPosition());
    }  
    
    public double   getPathLength(Vector3 vec){
        return this.getPathLength(vec.x(), vec.y(), vec.z());
    }
    
    public double   getPathLength(double x, double y, double z){
        double crosspath = Math.sqrt(
				     (this.particleCrossPosition.x()-x)*(this.particleCrossPosition.x()-x)
				     + (this.particleCrossPosition.y()-y)*(this.particleCrossPosition.y()-y)
				     + (this.particleCrossPosition.z()-z)*(this.particleCrossPosition.z()-z)
				     );
        return this.particlePath + crosspath;
    }
    
    public double getTime(DetectorType type){
        DetectorResponse response = this.getHit(type);
        if(response==null) return -1.0;
        return response.getTime();
    }
    
    public double getTime(DetectorType type, int layer) {
        DetectorResponse response = this.getHit(type,layer);
        if(response==null) return -1.0;
        return response.getTime();
    }
    

    
    public double getEnergy(DetectorType type){
        double energy = 0.0;
        for(DetectorResponse r : this.responseStore){

            if(r.getDescriptor().getType()==type){
                energy += r.getEnergy();
            }
        }
        /*
        DetectorResponse response = this.getHit(type);
        if(response==null) return -1.0;
        return response.getEnergy();*/

        return energy;
    }
    
    public int getNphe(String che){
       int nphe = 0;
            for(CherenkovSignal c : this.cherenkovStore){
            if(c.getSignalType()==che){
                nphe = c.getNphe();
            }
        }
             return nphe;
    }
    
    public double getBeta(DetectorType type){
        DetectorResponse response = this.getHit(type);
        if(response==null) return -1.0;
        double cpath = this.getPathLength(response.getPosition());
        double ctime = response.getTime();
        double beta  = cpath/ctime/30.0;
        return beta;
    }
    
    public double getMass(DetectorType type){
        double mass2 = this.getMass2(type);
        if(mass2<0) return Math.sqrt(-mass2);
        return Math.sqrt(mass2);
    }
    
    public double getMass2(DetectorType type){
        double beta   = this.getBeta(type);
        double energy = this.getEnergy(type);
        double mass2  = this.particleMomenta.mag2()/(beta*beta) - this.particleMomenta.mag2();
        return mass2;
    }
    
    public double getVertexTime(DetectorType type, int layer){
        double vertex_time = this.getTime(type,layer) - this.getPathLength(type, layer)/(this.getTheoryBeta(this.getPid())*29.9792);
        return vertex_time;
    }
    
    public void setStatus(int status){this.particleStatus = status;}
    public void setBeta(double beta){ this.particleBeta = beta;}
    public void setMass(double mass){ this.particleMass = mass;}
    public void setPid(int pid){this.particlePID = pid;}
    public void setCharge(int charge) { this.particleCharge = charge;}
    
    public void setCross(double x, double y, double z,
			 double ux, double uy, double uz){
        this.particleCrossPosition.setXYZ(x, y, z);
        this.particleCrossDirection.setXYZ(ux, uy, uz);
    }
    
    public void setScore(int sc){
        this.particleScore = sc;
    }
    
    public void setParticleTimeCheck(boolean x) {
        this.particleTiming = x;
    }
    
    public int getScore(){
        return this.particleScore;
    }
    
    public void setPIDResult(PIDResult result){
        this.pidresult = result;
    }
    
    public PIDResult getPIDResult(){
        return this.pidresult;
    }
    
    public int getDetectorHit(List<org.jlab.clas.detector.DetectorResponse>  hitList, DetectorType type,
			      int detectorLayer,
			      double distanceThreshold){
        
        Line3D   trajectory = new Line3D(
					 this.particleCrossPosition.x(),
					 this.particleCrossPosition.y(),
					 this.particleCrossPosition.z(),
					 this.particleCrossDirection.x()*1500.0,
					 this.particleCrossDirection.y()*1500.0,
					 this.particleCrossDirection.z()*1500.0
					 );
        
        Point3D  hitPoint = new Point3D();
        double   minimumDistance = 500.0;
        int      bestIndex       = -1;
        for(int loop = 0; loop < hitList.size(); loop++){
            //for(DetectorResponse response : hitList){
            org.jlab.clas.detector.DetectorResponse response = hitList.get(loop);
            if(response.getDescriptor().getType()==type&&
	       response.getDescriptor().getLayer()==detectorLayer){
     
                hitPoint.set(
			     response.getPosition().x(),
			     response.getPosition().y(),
			     response.getPosition().z()
			     );
                double hitdistance = trajectory.distance(hitPoint).length();
               if(type==DetectorType.EC){ 
               //System.out.println(" LOOP = " + loop + "   distance = " + hitdistance);
               //System.out.println(hitPoint.x() + " , " + hitPoint.y() + " , " + hitPoint.z());
               }
                if(hitdistance<distanceThreshold&&hitdistance<minimumDistance){
                    minimumDistance = hitdistance;
                    bestIndex       = loop;
                }
            }
        }
        return bestIndex;
    }
    
        public int getCherenkovSignal(List<CherenkovSignal>  signalList, String type,
			      double thetaThreshold){
        
        Line3D   trajectory = new Line3D(
					 this.particleVertex.x(),
					 this.particleVertex.y(),
					 this.particleVertex.z(),
					 this.particleMomenta.x()*1500.0,
					 this.particleMomenta.y()*1500.0,
					 this.particleMomenta.z()*1500.0
					 );
        
        double  signalDtheta;
        double   minimumDistance = 500.0;
        int      bestIndex       = -1;
        for(int loop = 0; loop < signalList.size(); loop++){
            //for(DetectorResponse response : hitList){
            CherenkovSignal signal = signalList.get(loop);
            if(signal.getSignalType()==type){
                signalDtheta = signal.getTheta() - this.particleVertex.theta();
                if(signalDtheta<thetaThreshold&&signalDtheta<minimumDistance){
                    minimumDistance = signalDtheta;
                    bestIndex       = loop;
                }
            }
        }
        return bestIndex;
    } 
    
    /**
     * returns DetectorResponse that matches closely with the trajectory
     * @param responses
     * @return 
     */
    public DetectorResponse getDetectorResponse(List<DetectorResponse> responses){
        int index = this.getDetectorHitIndex(responses);
        return responses.get(index);
    }
    /**
     * Finds the index of the best matching detector response object from the list.
     * @param responses
     * @return 
     */
    public int  getDetectorHitIndex(List<DetectorResponse> responses){
        Path3D   trajectory = this.getTrajectory();
        int       bestIndex = 0;
        Line3D    bestLine     = new Line3D(0.,0.,0.,1000.0,0.0,0.0);
        Point3D   hitPosition  = new Point3D();
        int       index        = 0;
        for(DetectorResponse res : responses){
            hitPosition.set(res.getPosition().x(), 
			    res.getPosition().y(),res.getPosition().z());
            Line3D distance = trajectory.distance(hitPosition);
            if(distance.length()<bestLine.length()){
                bestLine.copy(distance);
                bestIndex = index;
            }
            index++;
        }
        return bestIndex;
    }
    
    public Line3D  getDistance(DetectorResponse  response){
        Path3D trajectory = this.getTrajectory();
        Point3D hitPoint = new Point3D(
				       response.getPosition().x(),response.getPosition().y(),response.getPosition().z());
        return trajectory.distance(hitPoint);
    }
    
    public void setPath(double path){
        this.particlePath = path;
    }
    
    public void addProjectedHitMap(TrackIntersect intersect){
        this.projectedHit.put(intersect.getdetectortype(), intersect);
    }
    

    
    public TreeMap<DetectorType,Point3D>  getProjectedHitMap(){
        TreeMap<DetectorType,Point3D>  projectedHit = new TreeMap<DetectorType, Point3D>();
        for(Map.Entry<DetectorType,TrackIntersect>  entry : this.projectedHit.entrySet()){
            Point3D inter = entry.getValue().getIntersection(this);
            projectedHit.put(entry.getKey(), inter);
        }
        return projectedHit;
    }
    
    @Override
	public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("status = %4d  charge = %3d [pid/beta/mass] %5d %8.4f %8.4f",                 
				 this.particleStatus,
				 this.particleCharge,
				 this.particlePID,
				 this.particleBeta,this.particleMass));
        str.append(String.format("  P [ %8.4f %8.4f %8.4f ]  V [ %8.4f %8.4f %8.4f ] ",
				 this.particleMomenta.x(),this.particleMomenta.y(),
				 this.particleMomenta.z(),
				 this.particleVertex.x(),this.particleVertex.y(),
				 this.particleVertex.z()));
        str.append("\n");
        str.append(String.format("\t\t\t CROSS [%8.4f %8.4f %8.4f]  DIRECTION [%8.4f %8.4f %8.4f]\n",
				 this.particleCrossPosition.x(),this.particleCrossPosition.y(),
				 this.particleCrossPosition.z(),this.particleCrossDirection.x(),
				 this.particleCrossDirection.y(),this.particleCrossDirection.z()));
        for(DetectorResponse res : this.responseStore){
            str.append(res.toString());
            str.append("\n");
        }
        
        return str.toString();
    }
}
