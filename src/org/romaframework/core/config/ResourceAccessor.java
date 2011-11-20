package org.romaframework.core.config;

import java.io.InputStream;
import java.net.URL;
import java.util.Set;

public interface ResourceAccessor {

	/**
	 * Retrieve a set of resouces in an context.
	 * 
	 * @param name
	 *          the base path of resources.
	 * @return the set of found resources.
	 */
	@SuppressWarnings("rawtypes")
	public Set getResourcePaths(String name);

	/**
	 * Retrieve the stream of a resource from it's name
	 * 
	 * @param name
	 *          of resource.
	 * @return the stream of resource content.
	 */
	public InputStream getResourceAsStream(String name);

	/**
	 * Find a resource.
	 * 
	 * @param name
	 *          the name of resource to find.
	 * @return the url of found resource or null il resource was not found.
	 */
	public URL getResource(String name);

}
