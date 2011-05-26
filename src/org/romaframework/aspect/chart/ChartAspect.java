package org.romaframework.aspect.chart;

import org.romaframework.core.aspect.Aspect;

public interface ChartAspect extends Aspect{
  public byte[] toChart(Object obj);

}
