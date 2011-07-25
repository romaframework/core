package org.romaframework.aspect.chart;

import java.io.OutputStream;

import org.romaframework.core.aspect.Aspect;

public interface ChartAspect extends Aspect {

	/**
	 * Render an object as chart.
	 * 
	 * @param obj
	 *          the object to render.
	 * @return the byte array correspondent to chart.
	 */
	public byte[] toChart(Object obj);

	/**
	 * Render an object as chart.
	 * 
	 * @param obj
	 *          the object to render.
	 * @param outputStream
	 *          where write chart.
	 */
	public void toChart(Object obj, OutputStream outputStream);

}
