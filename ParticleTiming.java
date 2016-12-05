
package org.jlab.clas.ebuilder;

import org.jlab.detector.base.DetectorType;

/**
 *
 * @author jnewton
 */
public interface ParticleTiming {
    void CoincidenceCheck(DetectorEvent event, DetectorParticle particle, DetectorType type, int layer);
}
