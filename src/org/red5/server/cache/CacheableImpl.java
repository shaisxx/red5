package org.red5.server.cache;

/*
 * RED5 Open Source Flash Server - http://www.osflash.org/red5
 * 
 * Copyright (c) 2006 by respective authors (see below). All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License as published by the Free Software 
 * Foundation; either version 2.1 of the License, or (at your option) any later 
 * version. 
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along 
 * with this library; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.red5.server.api.cache.ICacheable;
import org.springframework.context.ApplicationContext;

/**
 * Provides an implementation of a cacheable object.
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Paul Gregoire (mondain@gmail.com)
 */
public class CacheableImpl implements ICacheable {

	protected static Log log = LogFactory.getLog(CacheableImpl.class.getName());

	protected ApplicationContext applicationContext;

	private byte[] bytes;

	private String name;

	private boolean cached;

	public CacheableImpl(Object obj) {
		ByteBuffer tmp = ByteBuffer.allocate(1024, true);
		tmp.setAutoExpand(true);
		tmp.putObject(obj);
		bytes = new byte[tmp.capacity()];
		tmp.get(bytes);
		tmp.release();
		cached = true;
	}

	public CacheableImpl(ByteBuffer buffer) {
		log.debug("Buffer is direct: " + buffer.isDirect() + " capacity: "
				+ buffer.capacity());
		log.debug("Buffer limit: " + buffer.limit() + " remaining: "
				+ buffer.remaining() + " position: " + buffer.position());
		bytes = new byte[buffer.capacity()];
		buffer.rewind();
		int i = 0;
		while (i < buffer.limit()) {
			buffer.position(i);
			while (buffer.remaining() > 0) {
				bytes[i++] = buffer.get();
			}
		}
		cached = true;
		log.debug("Buffer size: " + buffer.capacity());
		buffer.release();
	}

	public void addRequest() {
		log.info("Adding request for: " + name);
	}

	public byte[] getBytes() {
		return bytes;
	}

	public ByteBuffer getByteBuffer() {
		return ByteBuffer.wrap(bytes).asReadOnlyBuffer();
	}

	public String getName() {
		return name;
	}

	public boolean isCached() {
		return cached;
	}

	public void setCached(boolean cached) {
		this.cached = cached;
	}

	public void setName(String name) {
		this.name = name;
	}

}