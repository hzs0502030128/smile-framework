package org.smile.script.groovy;

import groovy.util.ResourceConnector;
import groovy.util.ResourceException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.smile.collection.CollectionUtils;

public class GroovyResourceConnector implements ResourceConnector {

	protected Set<URL> roots = new LinkedHashSet<URL>();

	public GroovyResourceConnector(String[] root) throws MalformedURLException {
		CollectionUtils.add(roots, createRoots(root));
	}

	public GroovyResourceConnector(Collection<URL> urls) {
		roots.addAll(urls);
	}

	@Override
	public URLConnection getResourceConnection(String scriptName) throws ResourceException {
		URL url = getGroovyScriptUrl(scriptName);
		if (url == null) {
			return getGroovyResourceConnection(scriptName);
		}
		try {
			return url.openConnection();
		} catch (IOException e) {
			throw new ResourceException("can't load groovy script " + scriptName);
		}
	}

	protected URL getGroovyScriptUrl(String scriptName) {
		return null;
	}

	protected URLConnection getGroovyResourceConnection(String resourceName) throws ResourceException {
		URLConnection groovyScriptConn = null;
		ResourceException se = null;
		for (URL root : this.roots) {
			URL scriptURL = null;
			try {
				scriptURL = new URL(root, resourceName);
				groovyScriptConn = scriptURL.openConnection();
				groovyScriptConn.getInputStream();
			} catch (MalformedURLException e) {
				String message = "Malformed URL: " + root + ", " + resourceName;
				if (se == null) {
					se = new ResourceException(message);
				} else {
					se = new ResourceException(message, se);
				}
			} catch (IOException e1) {
				groovyScriptConn = null;
				String message = "Cannot open URL: " + root + resourceName;
				groovyScriptConn = null;
				if (se == null) {
					se = new ResourceException(message);
				} else {
					se = new ResourceException(message, se);
				}
			}
		}
		if (groovyScriptConn == null) {
			if (se != null) {
				throw se;
			}
			throw new ResourceException("No resource for " + resourceName + " was found");
		}
		return groovyScriptConn;
	}

	private static URL[] createRoots(String[] urls) throws MalformedURLException {
		if (urls == null) {
			return null;
		}
		URL[] roots = new URL[urls.length];
		for (int i = 0; i < roots.length; i++) {
			if (urls[i].indexOf("://") != -1) {
				roots[i] = new URL(urls[i]);
			} else {
				roots[i] = new File(urls[i]).toURI().toURL();
			}
		}
		return roots;
	}
}
